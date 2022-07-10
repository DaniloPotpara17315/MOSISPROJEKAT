package com.example.petpal.screens.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.petpal.R
import com.example.petpal.activity.ActivitySecond
import com.example.petpal.databinding.FragmentLoginBinding
import com.example.petpal.helpers.FirebaseHelper
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.HashMap

class FragmentLogin : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    lateinit var pd:ProgressDialog
    var setMail: Boolean = false
    var setPw: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        pd = ProgressDialog(context)
        pd.setCancelable(false)

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Toast.makeText(context, "logged in", Toast.LENGTH_SHORT).show()
//            Firebase.auth.signOut()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        check()
        val fab = binding.fab
        val pd = ProgressDialog(context)
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_back_home)
        }
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.action_back_home)
        }
        binding.editTextLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                setMail = android.util.Patterns.EMAIL_ADDRESS.matcher(p0).matches()
                check()
            }
        })
        binding.editTextLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                setPw = p0?.isNotEmpty() ?: false
                check()
            }
        })
        val button = binding.buttonLogin
        button.setOnClickListener {

            //Code for Login
            pd.setMessage("LOGOVANJE...")

            pd.show()
            var email = binding.editTextLoginEmail.text.toString()
            var password = binding.editTextLoginPassword.text.toString()

            FirebaseHelper.logInUser( requireContext(), email, password, binding.root, requireActivity(),pd)

        }
    }

    private fun check() {
        binding.buttonLogin.isEnabled = setPw && setMail
    }
}
