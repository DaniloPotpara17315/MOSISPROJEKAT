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
import com.example.petpal.databinding.FragmentProfileChangePasswordBinding

class FragmentProfileChangePassword : Fragment() {

    private var showPwCurrent:Boolean = false
    private var showPwNew:Boolean = false
    private var showPwConfirm:Boolean = false
    private var formCheck:BooleanArray = BooleanArray(2)
    private lateinit var binding:FragmentProfileChangePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileChangePasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enableButton()

        binding.imageBackButton3.setOnClickListener {
            findNavController().navigate(R.id.action_home)
        }

        binding.imagePwHide.setOnClickListener {
            val pwdField = binding.editTextRegisterPassword
            if (showPwCurrent) {
                binding.imagePwHide.setImageResource(R.drawable.outline_visibility_black_24)
                pwdField.inputType = 129
            } else {
                binding.imagePwHide.setImageResource(R.drawable.outline_visibility_off_black_24)
                pwdField.inputType = InputType.TYPE_CLASS_TEXT
            }
            showPwCurrent = !showPwCurrent
        }


        binding.imagePwHide2.setOnClickListener {
            val pwdField = binding.editTextRegisterPassword2
            if (showPwNew) {
                binding.imagePwHide2.setImageResource(R.drawable.outline_visibility_black_24)
                pwdField.inputType = 129
            } else {
                binding.imagePwHide2.setImageResource(R.drawable.outline_visibility_off_black_24)
                pwdField.inputType = InputType.TYPE_CLASS_TEXT
            }
            showPwNew = !showPwNew
        }


        binding.imagePwConfHide.setOnClickListener {
            val pwdField = binding.editTextRegisterPassConfirm2
            if (showPwConfirm) {
                binding.imagePwConfHide.setImageResource(R.drawable.outline_visibility_black_24)
                pwdField.inputType = 129
            } else {
                binding.imagePwConfHide.setImageResource(R.drawable.outline_visibility_off_black_24)
                pwdField.inputType = InputType.TYPE_CLASS_TEXT
            }
            showPwConfirm = !showPwConfirm
        }

        binding.buttonChangeEmail.setOnClickListener {
            //button pw changer
        }
        binding.editTextRegisterPassword.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                formCheck[0] = p0?.isNotEmpty()?:false
                enableButton()
            }
        })


        binding.editTextRegisterPassword2.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

                if (p0 != null) {
                    formCheck[1] = p0.isNotEmpty() and (binding.editTextRegisterPassConfirm2.text.toString() == p0.toString())
                }
                enableButton()
            }

        })

        binding.editTextRegisterPassConfirm2.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null) {
                    formCheck[1] = p0.isNotEmpty() and (binding.editTextRegisterPassword2.text.toString() == p0.toString())
                }
                enableButton()
            }
        })
    }
    private fun enableButton(){
        binding.buttonChangeEmail.isEnabled = formCheck.all { it }
    }

}