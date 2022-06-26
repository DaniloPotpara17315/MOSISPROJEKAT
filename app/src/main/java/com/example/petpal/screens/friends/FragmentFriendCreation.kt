package com.example.petpal.screens.friends

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.os.Process;
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.app.ActivityCompat
import com.example.petpal.R
import com.example.petpal.databinding.ActivityMainBinding.inflate
import com.example.petpal.databinding.FragmentFriendCreationBinding
import com.example.petpal.databinding.FragmentHomescreenBinding
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Process.setThreadPriority
import android.util.Log
import android.widget.Toast
import androidx.core.os.HandlerCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petpal.adapters.BluetoothDeviceAdapter
import com.example.petpal.shared_view_models.MainSharedViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FragmentFriendCreation(val handler: Handler = Handler()) : Fragment(),BluetoothDeviceAdapter.BluetoothListener {

    private lateinit var binding : FragmentFriendCreationBinding
    private val sharedView: MainSharedViewModel by activityViewModels()
    lateinit var recyclerView : RecyclerView
    val idFragmenta: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    var stringArrayList: MutableList<BluetoothDevice> = mutableListOf<BluetoothDevice>()
    lateinit var myBluetoothAdapter: BluetoothAdapter
    lateinit var bluetootDeviceAdapter:BluetoothDeviceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        myBluetoothAdapter.startDiscovery()

    }


    private var broadcastReceiver = object :BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            var action = p1?.action

            if(BluetoothDevice.ACTION_FOUND.equals(action)) {

                var device: BluetoothDevice = p1!!.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!
                Log.d("Bluedevice","${device!!.name}")
                if(device.name != null && !(device in stringArrayList))
                {
                    stringArrayList.add(device)
                    Log.d("Bluedevice","liststate${stringArrayList}")
                    bluetootDeviceAdapter.notifyDataSetChanged()
                }




            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFriendCreationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var bluetoothDevicesList = binding.recyclerBluetoothDevices
        recyclerView = binding.recyclerBluetoothDevices

        setOnClickListeners()

        var intentFilter:IntentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        this.requireContext().registerReceiver(broadcastReceiver,intentFilter)
        bluetootDeviceAdapter = BluetoothDeviceAdapter(requireContext(),sharedView,stringArrayList,this)
        recyclerView.adapter = bluetootDeviceAdapter

        recyclerView.layoutManager= LinearLayoutManager(requireContext())
        myBluetoothAdapter.startDiscovery()
    }




    fun setOnClickListeners(){
        var bluetoothRefreshButton = binding.buttonChatBluetoothRefresh
        var backButtuon = binding.imageBackToInvitations
        backButtuon.setOnClickListener {
            findNavController().popBackStack()
        }
        bluetoothRefreshButton.setOnClickListener{
            stringArrayList.clear()
            bluetootDeviceAdapter.notifyDataSetChanged()
            myBluetoothAdapter.startDiscovery()

        }



    }


    override fun onDestroy() {
        super.onDestroy()
        this.requireContext().unregisterReceiver(broadcastReceiver)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }



    override fun openConnection(device: BluetoothDevice) {

    }


}