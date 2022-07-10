package com.example.petpal.screens.map

import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.petpal.BuildConfig
import com.example.petpal.R
import com.example.petpal.databinding.FragmentMapBinding
import com.example.petpal.helpers.FirebaseHelper
import com.example.petpal.interfaces.MapComms
import com.example.petpal.models.Event
import com.example.petpal.shared_view_models.MainSharedViewModel
import com.example.petpal.shared_view_models.MapAddEventViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.text.SimpleDateFormat


class FragmentMap : Fragment(), LocationListener , MapComms {

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

        val passerbyId = requireActivity().intent.getStringExtra("passerbyId")
        if (passerbyId!=null && passerbyId!="") {
            sharedViewModel.selectedUserKey = passerbyId
            requireActivity().intent.putExtra("passerbyId", "")
            findNavController().navigate(R.id.action_map_to_dogprofile)
        }

        map.setMultiTouchControls(true)
        Log.d("testingbitch", "Hi you are setting the location! ${mapAddEventViewModel.placingCoordinates}")

        if (mapAddEventViewModel.placingCoordinates) {
            activity?.findViewById<FragmentContainerView>(R.id.fragment_navbar)?.visibility = View.GONE
            binding.buttonMapDropdown.visibility = View.GONE

            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                mapAddEventViewModel.placingCoordinates = false
                findNavController().popBackStack()
            }
            Log.d("testingbitch", "Just checking.")
            prepareMap()
            setOnMapClickOverlay()
            setUserLocationOverlay()

        }
        else {
            activity?.findViewById<FragmentContainerView>(R.id.fragment_navbar)?.visibility = View.VISIBLE
            prepareMap()
            setUserLocationOverlay()

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
        myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(activity), map)
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        val locManager : LocationManager = requireActivity().getSystemService(Activity.LOCATION_SERVICE) as LocationManager
        locManager.removeUpdates(this)
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    private fun prepareMap() {
        map.controller.setZoom(16.5)
        map.minZoomLevel = 14.0

        myLocationOverlay.enableFollowLocation()
        map.controller.setCenter(myLocationOverlay.myLocation)
    }

    private fun setUserLocationOverlay() {
        map.controller.setCenter(myLocationOverlay.myLocation)
        myLocationOverlay.enableFollowLocation()
        map.overlays.add(myLocationOverlay)
        //Log.d("locationtest", "${myLocationOverlay.myLocation}")


        if (!mapAddEventViewModel.placingCoordinates) {

            val currentFrag = this
            val dataRef = FirebaseHelper.database.getReference("map")

            FirebaseHelper.getMapData(sharedViewModel, currentFrag)
            dataRef.child("users").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    FirebaseHelper.getMapData(sharedViewModel, currentFrag)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }

    override fun drawEventMarkers(dataset : MutableList<Event>) {
        map.overlays.removeAll(map.overlays)
        Log.d("mapOverlays", "${map.overlays}")
        map.overlays.add(myLocationOverlay)
        myLocationOverlay.runOnFirstFix {
            if (map.isAttachedToWindow && sharedViewModel.eventsEnabled.value!!) {
                for (event in dataset) {

                    Log.d("eventDebug", "$event")

                    var results = FloatArray(3)
                    Location.distanceBetween(
                        myLocationOverlay.myLocation.latitude,
                        myLocationOverlay.myLocation.longitude,
                        event.lat,
                        event.lon,
                        results
                    )

                    if (sharedViewModel.cutoffDistance==0 || results[0]<sharedViewModel.cutoffDistance) {
                        val eventMarker = Marker(map)

                        //get a smallaer icon jesus fucking christ
                        eventMarker.icon =
                            ContextCompat.getDrawable(requireContext(), R.drawable.ic_marker_event)
                        eventMarker.position = GeoPoint(event.lat, event.lon)
                        eventMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                        eventMarker.setOnMarkerClickListener { _, _ ->
                            sharedViewModel.selectedEvent = event
                            findNavController().navigate(R.id.action_map_to_event_info)
                            true
                        }
                        map.overlays.add(eventMarker)
                    }
                }
            }
            drawUserMarkers()
        }

    }

    override fun drawUserMarkers() {

        if (map.isAttachedToWindow) {
            for (user in sharedViewModel.users) {
                val userMarker = Marker(map)

                //val ico = user.image.toUri().toIcon().loadDrawable(requireContext())

                //userMarker.icon = ico

                userMarker.icon = when (user.status) {
                    "Druzeljubiv" -> {
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_marker_user_druzeljubiv
                        )
                    }
                    "Nezainteresovan" -> {
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_marker_user_nezainteresovan
                        )
                    }
                    else -> {
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_marker_user_agresivan
                        )
                    }
                }
                userMarker.position = GeoPoint(user.lat, user.lon)
                userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                userMarker.setOnMarkerClickListener { _, _ ->
                    sharedViewModel.selectedUserKey = user.id
                    findNavController().navigate(R.id.action_map_to_dogprofile)
                    true
                }

                if (user.id in (sharedViewModel.userData["Friends"] as ArrayList<String>) &&
                        sharedViewModel.friendsEnabled.value!!) {
                map.overlays.add(userMarker)
                } else if (user.id !in (sharedViewModel.userData["Friends"] as ArrayList<String>)
                    && sharedViewModel.usersEnabled.value!!) {
                    map.overlays.add(userMarker)
                }
            }
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

        setFragmentResultListener("eventMarkers") { s, bundle ->
            drawEventMarkers(sharedViewModel.events)
        }
        setFragmentResultListener("userMarkers") { s, bundle ->
            drawEventMarkers(sharedViewModel.events)
        }
        setFragmentResultListener("friendMarkers") {_,_ ->
            drawEventMarkers(sharedViewModel.events)
        }

        setFragmentResultListener("eventsFilteredByName") { _, bundle ->
            val filterString = bundle.getString("eventsFilteredByName")
            filterByName(filterString ?: "")
        }

        setFragmentResultListener("filteredEventsByDate") {_, bundle ->
            val dateBegin = bundle.getString("dateBegin")
            val dateEnd = bundle.getString("dateEnd")
            filterByDate(dateBegin ?: "", dateEnd ?: "")
        }
        setFragmentResultListener("noviEvent") { _,_ ->
            FirebaseHelper.getMapData(sharedViewModel, this)
        }

    }

    private fun setOnMapClickOverlay() {

        Log.d("testingbitch", "On Click is set!")
        var receive = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                Log.d("testingbitch", "Hi i got your location: \n${p?.longitude} ${p?.latitude}")
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

    override fun filterByName(filterString : String) {
        if (filterString == "")
            drawEventMarkers(sharedViewModel.events)
        else {
            val dataset = mutableListOf<Event>()

            for (event in sharedViewModel.events) {
                if (filterString in event.name) {
                    dataset.add(event)

                }

            }
            drawEventMarkers(dataset)
            if (dataset.size == 1) {
                map.controller.animateTo(GeoPoint(dataset[0].lat, dataset[0].lon), 18.5, 1000L)
            }
        }
    }

    private fun filterByDate(dateBegin: String, dateEnd:String) {

        if (dateBegin == "" || dateEnd == "")
            drawEventMarkers(sharedViewModel.events)
        else {
            val dataset = mutableListOf<Event>()

            for (event in sharedViewModel.events) {

                val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                val dateB = dateFormat.parse(dateBegin)
                val dateE = dateFormat.parse(dateEnd)
                val dateEvent = dateFormat.parse(event.date)

                Log.d("eventyyy", "$dateB <= $dateEvent <= $dateE")

                if (dateEvent in dateB..dateE) dataset.add(event)

            }
            drawEventMarkers(dataset)
            if (dataset.size == 1) {
                map.controller.animateTo(GeoPoint(dataset[0].lat, dataset[0].lon), 18.5, 1000L)
            }
        }

    }



    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
}

