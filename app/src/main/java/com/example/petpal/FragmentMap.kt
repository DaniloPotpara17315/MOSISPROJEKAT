package com.example.petpal

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Picture
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
import androidx.viewpager2.adapter.FragmentViewHolder
import com.example.petpal.databinding.FragmentMapBinding
import com.example.petpal.shared_view_models.MainSharedViewModel
import com.example.petpal.shared_view_models.MapAddEventViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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

class FragmentMap : Fragment() {

    private lateinit var binding : FragmentMapBinding
    private lateinit var map : MapView
    private var user:FirebaseUser? = null
    private val sharedViewModel : MainSharedViewModel by activityViewModels()
    private val mapAddEventViewModel : MapAddEventViewModel by activityViewModels()

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
        } else {
            setUserLocationOverlay()
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

        }
        else {
            activity?.findViewById<FragmentContainerView>(R.id.fragment_navbar)?.visibility = View.VISIBLE
            setOnClickListeners()
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

    private fun setUserLocationOverlay() {
        map.controller.setZoom(15.0)
        map.minZoomLevel = 14.0

        val myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(activity), map)
        myLocationOverlay.enableFollowLocation()
        myLocationOverlay.runOnFirstFix {
            val currentLocation = myLocationOverlay.myLocation
            Log.d("locationtest", "$currentLocation")
            //val userMarker = Marker(map)
            //get a smallaer icon jesus fucking christ
            /*userMarker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.kerber)
            userMarker.position = myLocationOverlay.myLocation
            userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
*/
            //map.overlays.add(userMarker)

            val oPolygon = Polygon(map)
            val radius = 1000.0;
            val circlePoints = ArrayList<GeoPoint>()
            for (f in 0..360){
                circlePoints.add(currentLocation.destinationPoint(radius, f.toDouble()))
            }
            oPolygon.strokeColor = ContextCompat.getColor(requireContext(), R.color.blue_medium)
            oPolygon.fillColor = ContextCompat.getColor(requireContext(), R.color.blue_pale_transparent)

            oPolygon.points = circlePoints
            map.overlays.add(oPolygon)

            val constraintOffset = 0.1

            val scrollConstraints = BoundingBox(
                currentLocation.latitude+constraintOffset,
                currentLocation.longitude+constraintOffset,
                currentLocation.latitude-constraintOffset,
                currentLocation.longitude-constraintOffset
            )
            map.setScrollableAreaLimitDouble(scrollConstraints)
        }

        map.overlays.add(myLocationOverlay)

        Log.d("locationtest", "${myLocationOverlay.myLocation}")
        map.controller.setCenter(myLocationOverlay.myLocation)
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
}