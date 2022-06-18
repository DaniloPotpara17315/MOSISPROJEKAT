package com.example.petpal.screens.dog_profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.petpal.R
import com.example.petpal.databinding.FragmentDogProfileBinding
import com.example.petpal.shared_view_models.MainSharedViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class FragmentDogProfile : Fragment() {

    private val sharedViewModel: MainSharedViewModel by activityViewModels()
    private lateinit var binding: FragmentDogProfileBinding
    private lateinit var userDogSaved: Any
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedViewModel.selectedUserKey.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDogProfileBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var ref = Firebase.firestore.collection("Users").document(
            sharedViewModel.selectedUserKey!!
        ).get()

        ref.addOnSuccessListener {
            var userDog = it.data
            userDogSaved = it.data!!
            Log.d("WantedUser", "${userDog}")
            binding.textViewDogProfileName.text = userDog!!.get("Name").toString()
            binding.textViewDogProfileDesc.text =
                "${userDog!!.get("Description").toString()}\nRasa: ${
                    userDog.get("Breed").toString()
                }"
            when (userDog!!.get("Status").toString()) {
                "Druzeljubiv" -> {
                    binding.imageDogKnob.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.green_dark
                        )
                    )
                }
                "Nezainteresovan" -> {
                    binding.imageDogKnob.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.yellow_dark
                        )
                    )
                }
                "Agresivan" -> {
                    binding.imageDogKnob.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red_dark
                        )
                    )
                }
                else -> {
                    binding.imageDogKnob.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray_dark
                        )
                    )
                }
            }
        }
        val storageRef = Firebase.storage.reference
        storageRef.child("ProfileImages/${sharedViewModel.selectedUserKey.toString()}.png").downloadUrl.addOnSuccessListener {
            Log.d("ImageTag", "${it}")
            Glide.with(this).load(it).into(binding.imageDogProfile)
        }.addOnFailureListener {
            Log.d("ImageTag", "Missing${it}")
            Glide.with(this).load(R.drawable.ic_paw).into(binding.imageDogProfile)
        }
        binding.imageBackButton.setOnClickListener {
            // back button
            findNavController().popBackStack()
        }
        binding.imageDogInvite.setOnClickListener {
            //invite dog
            val database =
                FirebaseDatabase.getInstance("https://paw-pal-7f105-default-rtdb.europe-west1.firebasedatabase.app/")
            val dataRef =
                database.reference
            var body = mapOf(
                "statusCode" to "0"
            )
            dataRef.child("map")
                .child("invitations")
                .child("${sharedViewModel.selectedUserKey.toString()}")
                .child("${sharedViewModel.usr!!.uid.toString()}").setValue(body)
                .addOnSuccessListener {
                    Snackbar.make(
                        binding.root,
                        "Zahtev poslat",
                        Snackbar.LENGTH_LONG
                    ).show()
                }.addOnFailureListener{
                    Snackbar.make(
                        binding.root,
                        "${it.toString()}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }

        }
    }
}