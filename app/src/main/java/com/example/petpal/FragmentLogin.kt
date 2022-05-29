package com.example.petpal

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
import com.example.petpal.databinding.FragmentLoginBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlin.math.log

class FragmentLogin : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    var setMail:Boolean = false
    var setPw:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        fab.setOnClickListener {
                findNavController().navigate(R.id.action_back_home)
        }
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.action_back_home)
        }
        binding.editTextLoginEmail.addTextChangedListener(object  :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                setMail = android.util.Patterns.EMAIL_ADDRESS.matcher(p0).matches()
                check()
            }
        })
        binding.editTextLoginPassword.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                setPw = p0?.isNotEmpty()?:false
                check()
            }
        })
        val button = binding.buttonLogin
        button.setOnClickListener{
            //Code for Login
            findNavController().popBackStack()
        }
    }
    private fun check(){
        binding.buttonLogin.isEnabled = setPw && setMail
    }
}
