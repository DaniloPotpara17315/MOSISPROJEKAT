package com.example.petpal.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petpal.R
import com.example.petpal.models.Profile

class ChatAdapter(
    private val context: Context,
    private val dataset: MutableList<Profile>,
    private val handler: ChatOperationHandler
    ) : RecyclerView.Adapter<ChatAdapter.ViewHolder>(){

    interface ChatOperationHandler {
        fun openChat(person:Profile)
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val item: ConstraintLayout = view.findViewById(R.id.chat_item)
        val name: TextView = view.findViewById(R.id.text_chat_item_name)
        val status: ImageView = view.findViewById(R.id.image_chats_item_status)
        val profilePhoto: ImageView = view.findViewById(R.id.image_chats_item)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_item,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentProfile = dataset[position]

        //postavlja se ime
        holder.name.text = currentProfile.name

        //postavlja se boja statusa
        holder.status.setBackgroundColor(ContextCompat.getColor(
            context,
            when (currentProfile.status) {
                Profile.STATUS_ONLINE -> {
                    R.color.green_light
                }
                Profile.STATUS_DND -> {
                    R.color.yellow
                }
                Profile.STATUS_AGGRO -> {
                    R.color.red
                }
                else -> {
                    R.color.gray
                }
            }
            ))

        //postavlja se slika
        Glide.with(context).load(R.drawable.placeholder_dog).into(holder.profilePhoto)


        setOnClickListeners(holder,position)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    private fun setOnClickListeners(holder: ViewHolder, position: Int) {

        holder.item.setOnClickListener {
            handler.openChat(dataset[position])
        }

    }
}