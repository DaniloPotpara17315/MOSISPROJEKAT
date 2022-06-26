package com.example.petpal.adapters

import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petpal.R
import com.example.petpal.helpers.FirebaseHelper
import com.example.petpal.models.ChatEntry
import com.example.petpal.models.Profile
import com.google.firebase.ktx.Firebase

class ChatAdapter(
    private val context: Context,
    private val dataset: MutableList<ChatEntry>,
    private val handler: ChatOperationHandler
    ) : RecyclerView.Adapter<ChatAdapter.ViewHolder>(){

    interface ChatOperationHandler {
//        fun openChat(person:Profile)
        fun openDiscovery(id:String,buttonToHide:ImageView,buttonToShow:ImageView)
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val item: ConstraintLayout = view.findViewById(R.id.chat_item)
        val name: TextView = view.findViewById(R.id.text_chat_item_name)
        val status: ImageView = view.findViewById(R.id.image_chats_item_status)
        val profilePhoto: ImageView = view.findViewById(R.id.image_chats_item)
        val buttonAccept:ImageView = view.findViewById(R.id.button_chat_accept)
        val buttonDecline:ImageView = view.findViewById(R.id.button_chat_decline)
        val buttonSet:ConstraintLayout = view.findViewById(R.id.constraint_chat_invite_buttonset)
        val buttonBluetooth:ImageView = view.findViewById(R.id.button_chat_bluetooth)
        val buttonFriend:ImageView = view.findViewById(R.id.button_chat_friend)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_item,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentProfile = dataset[position].profile

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
        Glide.with(context).load(dataset[position].profile.imageUri).into(holder.profilePhoto)

        if(dataset[position].statusCode == "1") {
            holder.buttonSet.visibility = View.GONE
            setOnClickListenersAccepted(holder,position)
            holder.buttonBluetooth.visibility = View.VISIBLE
        }
        else if(dataset[position].statusCode == "2"){
            holder.buttonSet.visibility = View.GONE
//            setOnClickListenersAccepted(holder,position)
            holder.buttonBluetooth.visibility = View.GONE
            holder.buttonFriend.visibility = View.VISIBLE

        }
        else {
            setOnClickListenersPending(holder,position)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    private fun setOnClickListenersAccepted(holder: ViewHolder, position: Int) {

        holder.item.setOnClickListener {
//            handler.openChat(dataset[position].profile)
        }
        holder.buttonBluetooth.setOnClickListener {
            val pd = ProgressDialog(context)
            pd.setCancelable(false)
            pd.setMessage("Ucitavanje")
            pd.show()
            dataset[position].statusCode = "2"
            handler.openDiscovery(dataset[position].id,holder.buttonBluetooth,holder.buttonFriend)
            notifyItemChanged(position)
            FirebaseHelper.notifyFriendAdded(dataset[position].id, pd)
        }
    }

    private fun setOnClickListenersPending(holder:ViewHolder, position: Int){

        holder.buttonAccept.setOnClickListener {

            val pd = ProgressDialog(context)
            pd.setCancelable(false)
            pd.setMessage("Ucitavanje")
            pd.show()
            dataset[position].statusCode = "1"
            holder.buttonSet.visibility = View.GONE
            setOnClickListenersAccepted(holder,position)
            notifyItemChanged(position)
            FirebaseHelper.notifyInviteAccepted(dataset[position].id, pd)
        }

        holder.buttonDecline.setOnClickListener {
            FirebaseHelper.notifyInviteDeclined(dataset[position].id)
            dataset.remove(dataset[position])
            notifyItemRemoved(position)
        }

    }
}