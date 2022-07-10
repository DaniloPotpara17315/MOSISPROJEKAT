package com.example.petpal.screens.dog_profile

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.petpal.R
import com.example.petpal.databinding.FragmentDogProfileBinding
import com.example.petpal.helpers.FirebaseHelper
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

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDogProfileBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        activity?.findViewById<FragmentContainerView>(R.id.fragment_navbar)?.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //TODO prebaci ovo u FirebaseHelper
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

            if (sharedViewModel.selectedUserKey in sharedViewModel.userData["Friends"] as ArrayList<String>){
                binding.buttonRemoveFriend.visibility = View.VISIBLE
                binding.imageDogInvite.visibility = View.GONE
            }

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

        //zasto klik listeneri nisu u posebnu funkciju?
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
            val body = mapOf(
                "statusCode" to "0"
            )
            dataRef.child("chat")
                .child("invitations")
                .child(sharedViewModel.selectedUserKey.toString())
                .child(sharedViewModel.usr!!.uid).setValue(body)
                .addOnSuccessListener {
                    Snackbar.make(
                        binding.root,
                        "Zahtev poslat",
                        Snackbar.LENGTH_LONG
                    ).show()
                }.addOnFailureListener{
                    Snackbar.make(
                        binding.root,
                        it.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
        }
        binding.buttonRemoveFriend.setOnClickListener {
            val pd = ProgressDialog(requireContext())
            pd.setMessage("Uklanjanje prijatelja...")
            pd.setCancelable(false)
            pd.show()
            FirebaseHelper.removeFriend(sharedViewModel, binding.buttonRemoveFriend, binding.imageDogInvite,pd)
        }
    }
}