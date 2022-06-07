package com.example.petpal

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.petpal.databinding.FragmentProfileChangeEmailBinding


class FragmentProfileChangeEmail : Fragment() {

    private var showPw:Boolean = false
    private lateinit var binding:FragmentProfileChangeEmailBinding
    var setMail:Boolean = false
    var setPw:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileChangeEmailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        check()
        binding.imageBackButton3.setOnClickListener {
            //back button
            findNavController().navigate(R.id.action_back_settings)
        }
        binding.editTextRegisterEmail.addTextChangedListener(object  : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                setMail = android.util.Patterns.EMAIL_ADDRESS.matcher(p0).matches()
                check()
            }
        })

        binding.editTextRegisterPassword.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                setPw = p0?.isNotEmpty()?:false
                check()
            }
        })

        binding.imagePwHide.setOnClickListener {
            val pwdField = binding.editTextRegisterPassword
            if (showPw) {
                binding.imagePwHide.setImageResource(R.drawable.outline_visibility_black_24)
                pwdField.inputType = 129
            } else {
                binding.imagePwHide.setImageResource(R.drawable.outline_visibility_off_black_24)
                pwdField.inputType = InputType.TYPE_CLASS_TEXT
            }
            showPw = !showPw
        }

        binding.buttonChangeEmail.setOnClickListener {
            //logic for email change
        }
    }

    private fun check(){
        binding.buttonChangeEmail.isEnabled = setPw && setMail
    }

}