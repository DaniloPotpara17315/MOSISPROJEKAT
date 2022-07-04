package com.example.petpal.screens.homescreen

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.petpal.R
import com.example.petpal.databinding.FragmentHomescreenBinding
import com.example.petpal.helpers.FirebaseHelper
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class FragmentHomescreen : Fragment() {
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
            pd.setMessage("Molimo Sacekajte...")
            pd.show()
            FirebaseHelper.getUser(requireContext(), requireActivity())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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