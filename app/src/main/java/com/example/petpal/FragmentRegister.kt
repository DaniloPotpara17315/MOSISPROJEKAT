package com.example.petpal

import android.os.Bundle
import android.renderscript.ScriptGroup
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.example.petpal.databinding.FragmentRegisterBinding
import kotlin.math.log


class FragmentRegister : Fragment() {

    private var showPw:Boolean = false
    private var showPwConf:Boolean = false;
    private lateinit var binding: FragmentRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Popuna status scrolla
        val spin2 = binding.dropdownMenuDogStatus
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.dogStatus,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spin2.adapter = adapter
            }
        }
        val fab = binding.fab
        //odlazak na glavni meni
        fab.setOnClickListener{
            findNavController().popBackStack()
        }
        val butCam = binding.imageCamera
        //otvaranje kamere
        butCam.setOnClickListener{

        }
        //Klik za registraciju
        val butReg = binding.buttonReg
        butReg.setOnClickListener{
            findNavController().navigate(R.id.action_goto_login)
        }
        val butPwHide = binding.imagePwHide
        val btnPwRepeatHide = binding.imagePwConfHide
        //Prikazivanje sifre
        butPwHide.setOnClickListener {
            val pwdField = binding.editTextRegisterPassword
            if (showPw) {
                butPwHide.setImageResource(R.drawable.outline_visibility_black_24)
                pwdField.inputType = 129
            } else {
                butPwHide.setImageResource(R.drawable.outline_visibility_off_black_24)
                pwdField.inputType = InputType.TYPE_CLASS_TEXT
            }
            showPw = !showPw
        }
        //Prikazivanje sifre confirm
        btnPwRepeatHide.setOnClickListener {
            val pwdField = binding.editTextRegisterPassConfirm
            if (showPwConf) {
                btnPwRepeatHide.setImageResource(R.drawable.outline_visibility_black_24)
                pwdField.inputType = 129
            } else {
                btnPwRepeatHide.setImageResource(R.drawable.outline_visibility_off_black_24)
                pwdField.inputType = InputType.TYPE_CLASS_TEXT
            }
            showPwConf = !showPwConf
        }

    }
}