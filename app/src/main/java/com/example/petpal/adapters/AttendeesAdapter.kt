package com.example.petpal.adapters

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
import com.example.petpal.models.Profile
import com.example.petpal.shared_view_models.MainSharedViewModel


class AttendeesAdapter (val context: Context,val sharedViewModel: MainSharedViewModel, val dataSet:MutableList<Profile>, val listener: AttendeeListener): RecyclerView.Adapter<AttendeesAdapter.ViewHolder>() {


    interface AttendeeListener {
        fun respond()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val image = view.findViewById<ImageView>(R.id.image_event_dog_item_Profile)
        val name = view.findViewById<TextView>(R.id.text_event_dog_item_Name)
        val rate = view.findViewById<RatingBar>(R.id.ratingBar_event_dot_item_rate)
        val item = view.findViewById<ConstraintLayout>(R.id.constraint_event_dog_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_dog_item,parent,false)

        return AttendeesAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(dataSet[position].imageUri).into(holder.image)
        holder.name.text = dataSet[position].name
        var splittedString = dataSet[position].rate.split("/")
        if(splittedString[1].toFloat() == 0F){
            holder.rate.rating = 2.5F
        }else{
        holder.rate.rating= splittedString[0].toFloat()/splittedString[1].toFloat()
        }
        holder.item.setOnClickListener{
            sharedViewModel.selectedAttendee = dataSet[position]
            listener.respond()
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}