package com.example.petpal

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.petpal.databinding.FragmentProfileChangeEmailBinding
import com.example.petpal.shared_view_models.MainSharedViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class FragmentProfileChangeEmail : Fragment() {

    private var showPw:Boolean = false
    private lateinit var user: FirebaseUser
    private lateinit var binding:FragmentProfileChangeEmailBinding
    private val sharedViewModel : MainSharedViewModel by activityViewModels()
    val pd = ProgressDialog(context)
    var setMail:Boolean = false
    var setPw:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pd.setCancelable(false)
        pd.setMessage("PROMENA EMAIL-A U TOKU...")
        user = Firebase.auth.currentUser!!
        if(user == null){

        }
        else{

        }
//        Toast.makeText(context,"${user.email.toString()}",Toast.LENGTH_LONG).show()

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
        binding.textViewChangeMailText.text = "Trenutni email: ${user.email.toString()}"
        binding.imageBackButton3.setOnClickListener {
            //back button
            findNavController().navigate(R.id.action_back_settings)
        }
        binding.editTextRegisterEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                setMail = android.util.Patterns.EMAIL_ADDRESS.matcher(p0).matches()
                check()
            }
        })

        binding.editTextRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                setPw = p0?.isNotEmpty() ?: false
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
            pd.show()
            val credential = EmailAuthProvider
                .getCredential(
                    user.email.toString(),
                    binding.editTextRegisterPassword.text.toString()
                )

            user.reauthenticate(credential)
                .addOnCompleteListener {
                    user!!.updateEmail(binding.editTextRegisterEmail.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Snackbar.make(binding.root, "Email promenjen", Snackbar.LENGTH_LONG)
                                    .show()
                                var currUser = mutableMapOf<String, Any>(
                                    "Email" to binding.editTextRegisterEmail.text.toString(),
                                )

                                Firebase.firestore.collection("Users").document(
                                    user.uid
                                ).update(currUser).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        sharedViewModel.userData.set(
                                            "Email",
                                            binding.editTextRegisterEmail.text.toString()
                                        )
                                        pd.hide()
                                        findNavController().popBackStack()

                                    }
                                    else{
                                        pd.hide()
                                    }

                                }
                            }
                        }
                }
        }
    }

    private fun check(){
        binding.buttonChangeEmail.isEnabled = setPw && setMail
    }

}