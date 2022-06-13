package com.example.petpal

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.petpal.databinding.FragmentHomescreenBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*


class FragmentHomescreen : Fragment() {

    private lateinit var binding : FragmentHomescreenBinding
    //val pd = ProgressDialog(context)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var auth = Firebase.auth

        if(auth.currentUser != null){
            startActivity(
                Intent(context, ActivitySecond::class.java)
            )
        }
        //pd.setCancelable(false)
        //pd.setMessage("Loading")
//        pd.show()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //PLS ne brisi ovo dugme, treba mi da udjem u drugi activity xD
        //   slobodno ga premestaj kako ti volja tho
        val btnOtherActivity = binding.btnToActivity2
        btnOtherActivity.setOnClickListener{
            val today = Calendar.getInstance()

//            val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->  })
//
//            var datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
//                today.get(Calendar.DAY_OF_MONTH)

//            startActivity(
//                Intent(context, ActivitySecond::class.java)
//            )
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