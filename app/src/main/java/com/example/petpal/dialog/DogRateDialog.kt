package com.example.petpal.dialog

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.petpal.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.petpal.databinding.FragmentDogRateDialogBinding
import com.example.petpal.shared_view_models.MainSharedViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.ktx.storage

class DogRateDialog : BottomSheetDialogFragment() {

    private val sharedView: MainSharedViewModel by activityViewModels()
    private lateinit var binding:FragmentDogRateDialogBinding
    lateinit var pd : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pd = ProgressDialog(context)
        pd.setCancelable(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDogRateDialogBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textDogName.text = sharedView.selectedAttendee.name
        Glide.with(requireContext()).load(sharedView.selectedAttendee.imageUri).into(binding.imageProfile)
        setOnClickListeners()
    }

    fun setOnClickListeners(){
        binding.buttonCancel.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonCreate.setOnClickListener {
            pd.setMessage("OCENJIVANJE U TOKU...")
            pd.show()
            var helper = sharedView.selectedAttendee.rate.split("/").toMutableList()
            helper[0] = (binding.dogRating.rating+helper[0].toFloat()).toString()
            helper[1] = (helper[1].toFloat()+1).toString()
            val valueToSend = "${helper[0]}/${helper[1]}"
            val updatePart = hashMapOf<String, Any>(
                "grade" to valueToSend
            )
            val dat =
                FirebaseDatabase.getInstance("https://paw-pal-7f105-default-rtdb.europe-west1.firebasedatabase.app/")
            val datRef = dat.getReference("map")
            datRef.child("events")
                .child("${sharedView.selectedEvent!!.id}")
                .child("Attendees")
                .child("${sharedView.selectedAttendee.userId}")
                .updateChildren(updatePart).addOnSuccessListener {
                    pd.dismiss()
                    Snackbar.make(
                        binding.root,
                        "Uspesno ocenjeno",
                        Snackbar.LENGTH_LONG
                    ).show()

                    sharedView.selectedAttendee.rate = valueToSend
                    setFragmentResult("rateUpdate", bundleOf("rateUpdate" to 0))
                    findNavController().popBackStack()
                }.addOnFailureListener{
                    pd.dismiss()
                }
        }
    }
}