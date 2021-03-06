package com.example.petpal.screens.register

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.example.petpal.R
import com.example.petpal.activity.ActivitySecond
import com.example.petpal.databinding.FragmentRegisterBinding
import com.example.petpal.helpers.FirebaseHelper
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream


class FragmentRegister : Fragment() {

    private var showPw: Boolean = false
    private var showPwConf: Boolean = false;
    private val REQUEST_IMAGE_CAPTURE = 1;
    private lateinit var imageBitmap: Bitmap
    private var formCheck: BooleanArray = BooleanArray(7)
    lateinit var pd : ProgressDialog
    private lateinit var binding: FragmentRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pd = ProgressDialog(context)
        pd.setCancelable(false)
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
        fab.setOnClickListener {
            findNavController().popBackStack()
        }
        val butCam = binding.imageCamera

        //otvaranje kamere
        butCam.setOnClickListener {

            dispatchTakePictureIntent()

        }
        //Klik za registraciju
        val butReg = binding.buttonReg
        butReg.setOnClickListener {
            pd.setMessage("REGISTRACIJA U TOKU...")
            pd.show()
            val email = binding.editTextRegisterEmail.text.toString()
            val password = binding.editTextRegisterPassword.text.toString()

            FirebaseHelper.registerUser(
                requireContext(),
                email,
                password,
                binding,
                imageBitmap,
                pd,
                findNavController()
            )


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

        //region textWatchers

        binding.editTextDogName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                formCheck[1] = p0?.isNotEmpty() ?: false
                enableButton()
            }
        })
        binding.editTextRegisterEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                formCheck[2] = android.util.Patterns.EMAIL_ADDRESS.matcher(p0).matches()
                enableButton()
            }

        })

        binding.editTextRegisterDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                formCheck[3] = p0?.isNotEmpty() ?: false
                enableButton()
            }
        })

        binding.editTextMenuDogBreed.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                formCheck[4] = p0?.isNotEmpty() ?: false
                enableButton()
            }
        })

        spin2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                formCheck[5] = p2 != 0
                enableButton()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        binding.editTextRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

                if (p0 != null) {
                    formCheck[6] =
                        p0.isNotEmpty() and (binding.editTextRegisterPassConfirm.text.toString() == p0.toString())
                }
                enableButton()
            }

        })

        binding.editTextRegisterPassConfirm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null) {
                    formCheck[6] =
                        p0.isNotEmpty() and (binding.editTextRegisterPassword.text.toString() == p0.toString())
                }
                enableButton()

            }

        })
        //endregion

    }

    private fun enableButton() {
        binding.buttonReg.isEnabled = formCheck.all { it }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap
            formCheck[0] = true
            binding.imageCamera.setImageDrawable(null)
            binding.imageCameraBackground.setImageBitmap(imageBitmap)
            //imageView.setImageBitmap(imageBitmap)
        }
    }
}