package com.example.petpal.adapters

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petpal.R
import com.example.petpal.shared_view_models.MainSharedViewModel

class BluetoothDeviceAdapter(val context: Context, val sharedViewModel: MainSharedViewModel, val dataSet:MutableList<BluetoothDevice>,val listener:BluetoothListener) :RecyclerView.Adapter<BluetoothDeviceAdapter.ViewHolder>() {

    //: RecyclerView.Adapter<AttendeesAdapter.ViewHolder>()

    interface BluetoothListener{
        fun openConnection(device:BluetoothDevice)
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val deviceName = view.findViewById<TextView>(R.id.text_bluetooth_device_name)
        val buttonBluetoothConnect = view.findViewById<ImageView>(R.id.button_bluetooth_connect)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bluetooth_item,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.deviceName.text = dataSet[position].name

        setOnClickListeners(holder,position)
    }

    fun setOnClickListeners(holder:ViewHolder,position: Int){
        holder.buttonBluetoothConnect.setOnClickListener {
            //begin connecting process
            listener.openConnection(dataSet[position])
        }
    }



    override fun getItemCount(): Int {
        return dataSet.size
    }
}