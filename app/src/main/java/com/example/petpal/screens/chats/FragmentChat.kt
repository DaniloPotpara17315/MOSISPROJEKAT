package com.example.petpal.screens.chats

import android.app.Activity
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context.BLUETOOTH_SERVICE
import android.content.Intent
import android.icu.lang.UProperty.NAME
import android.os.Bundle
import android.os.Handler
import android.os.Process
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petpal.R
import com.example.petpal.adapters.ChatAdapter
import com.example.petpal.databinding.FragmentChatBinding
import com.example.petpal.helpers.FirebaseHelper
import com.example.petpal.models.Profile
import com.example.petpal.shared_view_models.MainSharedViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import kotlin.collections.HashMap

class FragmentChat(val handler: Handler = Handler()) : Fragment(),ChatAdapter.ChatOperationHandler {

    private val REQUEST_ENABLE_BT = 1
    private val REQUEST_ENABLE_BT_EMIT = 2
    private val idFragmeta:UUID=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private val sharedViewModel : MainSharedViewModel by activityViewModels()
    private var _binding : FragmentChatBinding? = null
    private val binding get() = _binding!!
    lateinit var myBluetoothAdapter: BluetoothAdapter
    lateinit var bluetoothManager: BluetoothManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pd = ProgressDialog(context)
        pd.setMessage("Ucitavanje...")
        pd.setCancelable(false)
        pd.show()

        val recycler = binding.recyclerChat
        FirebaseHelper.getChats(requireContext(),this, recycler, pd)
        setOnClickListeners()
    }

    fun setOnClickListeners(){
        binding.buttonChatsShowFriends.setOnClickListener {
    // prikazi samo friends
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun openDiscovery(userId:String,buttonToHide:ImageView,buttonToShow:ImageView) {

        var mainUserId = Firebase.auth.currentUser!!.uid
        var rez = (sharedViewModel.userData.get("Friends") as MutableList<String>)
        rez.add(userId)
        var body:HashMap<String,Any> = hashMapOf<String,Any>(
            "Friends" to rez
        )
        Log.d("TAggerica","{$rez}")
        var database = Firebase.firestore.collection("Users").document(
            mainUserId
        ).update(body).addOnCompleteListener{
            buttonToHide.visibility= View.GONE
            buttonToShow.visibility = View.VISIBLE

            var databaseSecond = Firebase.firestore.collection("Users").document(userId)
            databaseSecond.get().addOnSuccessListener {
                var hisData = it.data
                var hisFriends = (hisData!!.get("Friends") as MutableList<String>)
                hisFriends.add(mainUserId)
                var bodyToSend:HashMap<String,Any> = hashMapOf<String,Any>(
                    "Friends" to hisFriends
                )
                databaseSecond.update(bodyToSend).addOnSuccessListener {
                    Snackbar.make(
                        binding.root,
                        "Korisnik dodat za prijatelja",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

    }

}
