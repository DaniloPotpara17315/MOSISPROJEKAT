package com.example.petpal.screens.map

import android.app.Activity
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

import com.example.petpal.R
import com.example.petpal.databinding.FragmentMapBinding
import com.example.petpal.models.Event
import com.example.petpal.models.Profile
import com.example.petpal.models.ProfileCoordinates
import com.example.petpal.shared_view_models.MainSharedViewModel
import com.example.petpal.shared_view_models.MapAddEventViewModel
import com.google.android.datatransport.BuildConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class FragmentMap : Fragment(), LocationListener {

    private lateinit var binding : FragmentMapBinding
    private lateinit var map : MapView
    private var user:FirebaseUser? = null
    private val sharedViewModel : MainSharedViewModel by activityViewModels()
    private val mapAddEventViewModel : MapAddEventViewModel by activityViewModels()
    private lateinit var myLocationOverlay : MyLocationNewOverlay
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted : Boolean ->
            if (isGranted)
                setUserLocationOverlay()

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = Firebase.auth.currentUser
        if(user == null){
            requireActivity().finish()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ||
            ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
        map.setMultiTouchControls(true)

        if (mapAddEventViewModel.placingCoordinates) {
            activity?.findViewById<FragmentContainerView>(R.id.fragment_navbar)?.visibility = View.GONE
            binding.buttonMapDropdown.visibility = View.GONE

            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                mapAddEventViewModel.placingCoordinates = false
                findNavController().popBackStack()
            }
            setOnMapClickOverlay()
            setUserLocationOverlay()
            prepareMap()
        }
        else {
            activity?.findViewById<FragmentContainerView>(R.id.fragment_navbar)?.visibility = View.VISIBLE

            setUserLocationOverlay()
            prepareMap()
            setOnClickListeners()
            val locManager : LocationManager = requireActivity().getSystemService(Activity.LOCATION_SERVICE) as LocationManager
            locManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5555,
                100f,
                this
            )

            if (mapAddEventViewModel.creatingEvent)
            {
                findNavController().navigate(R.id.action_map_to_addevent)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(layoutInflater)
        map = binding.map
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    private fun prepareMap() {
        map.controller.setZoom(16.5)
        map.minZoomLevel = 14.0
    }

    private fun setUserLocationOverlay() {

        myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(activity), map)
        myLocationOverlay.enableFollowLocation()
        val database =
            FirebaseDatabase.getInstance("https://paw-pal-7f105-default-rtdb.europe-west1.firebasedatabase.app/")

        myLocationOverlay.runOnFirstFix {
            //val currentLocation = myLocationOverlay.myLocation
            //Log.d("locationtest", "$currentLocation")
            // crtanje kruznice oko korisnika,
            // ako se NE postavljaju koordinate
        }

        map.overlays.add(myLocationOverlay)
        Log.d("locationtest", "${myLocationOverlay.myLocation}")
        map.controller.setCenter(myLocationOverlay.myLocation)



        val dataRef = database.getReference("map")

        dataRef.child("events").get().addOnSuccessListener {
            val temp:HashMap<Any,Any> = it.value as HashMap<Any, Any>

            val events : MutableList<Event> = mutableListOf()

            temp.forEach { entry ->
                val eventMap = entry.value as HashMap<String, Any>

                val event = Event(eventMap["eventName"] as String,
                    eventMap["eventDesc"] as String,
                    eventMap["eventDate"] as String,
                    eventMap["eventLon"] as Double,
                    eventMap["eventLat"]as Double,
                    entry.key as String
                    )

                events.add(event)
            }
            sharedViewModel.events = events

            drawMarkers()
        }

        dataRef.child("users").get().addOnSuccessListener {
            val temp:HashMap<Any,Any> = it.value as HashMap<Any, Any>

            val users : MutableList<ProfileCoordinates> = mutableListOf()

            temp.forEach { user ->

                val userMap = user.value as HashMap<String, Any>

                val newUser = ProfileCoordinates(user.key as String,
                    userMap["lat"] as Double,
                    userMap["lon"] as Double,
                    userMap["status"] as String
                )
                if (newUser.id != Firebase.auth.uid)
                users.add(newUser)
            }
            sharedViewModel.users = users
            drawMarkers()
        }

        dataRef.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                drawMarkers()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private fun drawMarkers() {
        map.overlays.clear()
        map.overlays.add(myLocationOverlay)

        for (event in sharedViewModel.events) {
            val eventMarker = Marker(map)
            //get a smallaer icon jesus fucking christ
            eventMarker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_marker_event)
            eventMarker.position = GeoPoint(event.lat, event.lon)
            eventMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            eventMarker.setOnMarkerClickListener { _, _ ->
                sharedViewModel.selectedEvent = event
                findNavController().navigate(R.id.action_map_to_event_info)
                true
            }
            map.overlays.add(eventMarker)
        }

        for (user in sharedViewModel.users) {
            val userMarker = Marker(map)

            userMarker.icon = when(user.status) {
                "Druzeljubiv" -> {
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_marker_user_druzeljubiv)
                }
                "Nezainteresovan" -> {
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_marker_user_nezainteresovan)
                }
                else -> {
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_marker_user_agresivan)
                }
            }
            userMarker.position = GeoPoint(user.lat, user.lon)
            userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            userMarker.setOnMarkerClickListener { _, _ ->
                sharedViewModel.selectedUserKey = user.id
                findNavController().navigate(R.id.action_map_to_dogprofile)
                true
            }
            map.overlays.add(userMarker)
        }



    }

    private fun setOnClickListeners() {
        val buttonDropdown = binding.buttonMapDropdown
        buttonDropdown.setOnClickListener {
            binding.constraintMapDropdown.visibility = View.VISIBLE
            it.visibility = View.GONE
        }

        val buttonAddEvent = binding.buttonMapAddEvent
        buttonAddEvent.setOnClickListener {
            findNavController().navigate(R.id.action_map_to_addevent)
        }
        binding.buttonMapFilter.setOnClickListener {
            findNavController().navigate(R.id.action_map_to_addfilter)
        }
    }

    private fun setOnMapClickOverlay() {
        var receive = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {

                mapAddEventViewModel.longitude.value = p?.longitude
                mapAddEventViewModel.latitude.value = p?.latitude


                mapAddEventViewModel.placingCoordinates = false
                findNavController().popBackStack()
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }

        }

        map.overlays.add(MapEventsOverlay(receive))
    }

    override fun onLocationChanged(location: Location) {
        val userMap = mapOf(
            "lat" to location.latitude,
            "lon" to location.longitude,
            "status" to sharedViewModel.userData["Status"]
        )

        Log.d("freshavacado","$userMap")
        val database = FirebaseDatabase.getInstance("https://paw-pal-7f105-default-rtdb.europe-west1.firebasedatabase.app/")

        database.getReference("map").child("users")
            .child(Firebase.auth.currentUser!!.uid).setValue(userMap)

        //ogranicavanje skrolovanja mape
        val constraintOffset = 0.1
        val scrollConstraints = BoundingBox(
            location.latitude+constraintOffset,
            location.longitude+constraintOffset,
            location.latitude-constraintOffset,
            location.longitude-constraintOffset
        )
        map.setScrollableAreaLimitDouble(scrollConstraints)
    }
}