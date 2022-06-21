package com.example.petpal.screens.homescreen

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.petpal.R
import com.example.petpal.databinding.FragmentHomescreenBinding
import com.example.petpal.helpers.FirebaseHelper
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class FragmentHomescreen : Fragment() {
    private val REQUEST_ENABLE_BT = 1
    private lateinit var binding : FragmentHomescreenBinding
 /*   lateinit var myBluetoothAdapter:BluetoothAdapter
    lateinit var bluetoothManager: BluetoothManager*/
    //val pd = ProgressDialog(context)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        context?.getSystemService(BLUETOOTH_SERVICE)
/*        bluetoothManager = context?.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        myBluetoothAdapter= bluetoothManager.getAdapter()*/

        var auth = Firebase.auth

        if (auth.currentUser != null) {
            val pd = ProgressDialog(requireContext())
            pd.setMessage("Please wait...")
            pd.show()
            FirebaseHelper.getUser(requireContext(), requireActivity())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_ENABLE_BT){
            if(resultCode == RESULT_OK){

            }
            else if(resultCode == RESULT_CANCELED){

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //PLS ne brisi ovo dugme, treba mi da udjem u drugi activity xD
        //   slobodno ga premestaj kako ti volja tho
        val btnOtherActivity = binding.btnToActivity2
        btnOtherActivity.setOnClickListener {
            /*if (myBluetoothAdapter != null) {
                if (!myBluetoothAdapter.isEnabled) {

                    var enableBluetoothIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT)
                } else {

                }
            }*/

        }
        val btnLoginActivity = binding.buttonToLogin
        btnLoginActivity.setOnClickListener{
            findNavController().navigate(R.id.action_goto_login)
        }
        val btnRegisterActivity = binding.buttonToRegister
        btnRegisterActivity.setOnClickListener{
            findNavController().navigate(R.id.action_goto_register)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomescreenBinding.inflate(layoutInflater)
        return binding.root
    }


}