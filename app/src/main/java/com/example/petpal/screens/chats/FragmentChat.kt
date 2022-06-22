package com.example.petpal.screens.chats

import android.app.Activity
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context.BLUETOOTH_SERVICE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class FragmentChat : Fragment(),ChatAdapter.ChatOperationHandler {

    private val REQUEST_ENABLE_BT = 1
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

        /*dataset.add(Profile("Online", Profile.STATUS_ONLINE))
        dataset.add(Profile("Do not disturb", Profile.STATUS_DND))
        dataset.add(Profile("Stay away", Profile.STATUS_AGGRO))
        dataset.add(Profile("Invisible", Profile.STATUS_INVIS))
        dataset.add(Profile("Offline"))
*/
        /*
        recycler.layoutManager = LinearLayoutManager(requireContext())
        //recycler.adapter = ChatAdapter(requireContext(), dataset,this)
*/
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun openChat(person:Profile) {
        sharedViewModel.selectedProfile.value = person
        findNavController().navigate(R.id.action_chat_to_chatroom)
        val navbar: FragmentContainerView? = activity?.findViewById(R.id.fragment_navbar)
        navbar?.visibility = View.GONE

    }

    override fun openDiscovery() {
        bluetoothManager = context?.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        myBluetoothAdapter= bluetoothManager.getAdapter()
        if (myBluetoothAdapter != null) {
                if (!myBluetoothAdapter.isEnabled) {

                    var enableBluetoothIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT)
                } else {

                }
            }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_ENABLE_BT){
            if(resultCode == Activity.RESULT_OK){
                findNavController().navigate(R.id.action_go_to_bluetooth)
            }
            else if(resultCode == Activity.RESULT_CANCELED){

            }
        }
    }

}