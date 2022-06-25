package com.example.petpal.screens.friends

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
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
import android.util.Log

class FragmentFriendCreation : Fragment() {

    private lateinit var binding : FragmentFriendCreationBinding
    lateinit var scanList : ListView
    var stringArrayList: ArrayList<String> = ArrayList<String>()
    lateinit var arrayAdapter:ArrayAdapter<String>
    lateinit var myBluetoothAdapter: BluetoothAdapter
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
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }

                arrayAdapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1,stringArrayList)
                stringArrayList.add(device.name)
                scanList.adapter = arrayAdapter
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

        var bluetoothDevicesList = binding.ListViewBluetoothDevicesList
        scanList = binding.ListViewBluetoothDevicesList

        setOnClickListeners()

        var intentFilter:IntentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        this.requireContext().registerReceiver(broadcastReceiver,intentFilter)
        arrayAdapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1,stringArrayList)
        scanList.adapter = arrayAdapter
    }




    fun setOnClickListeners(){
        var bluetoothRefreshButton = binding.buttonChatBluetoothRefresh

        bluetoothRefreshButton.setOnClickListener{
            if (ActivityCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
//                activity.requestPermissions()
//                 TODO: Consider calling
//                    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
//                return
            }
            myBluetoothAdapter.startDiscovery()
        }


    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}