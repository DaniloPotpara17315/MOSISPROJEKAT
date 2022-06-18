package com.example.petpal.screens.event_info

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.petpal.R
import com.example.petpal.databinding.FragmentEventInfoBinding
import com.example.petpal.shared_view_models.MainSharedViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class FragmentEventInfo : Fragment() {

    private val sharedView: MainSharedViewModel by activityViewModels()
    lateinit var pd: ProgressDialog
    private var coming: Boolean = false;
    private lateinit var binding: FragmentEventInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pd = ProgressDialog(context)

        pd.setCancelable(false)
        pd.setMessage("PROMENA EMAIL-A U TOKU...")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkComing()
        binding.textViewEventTitle.text =
            "${sharedView.selectedEvent!!.name} - ${sharedView.selectedEvent!!.date}"
        binding.textViewEventDescription.text = sharedView.selectedEvent!!.desc
        val dat =
            FirebaseDatabase.getInstance("https://paw-pal-7f105-default-rtdb.europe-west1.firebasedatabase.app/")
        val datRef = dat.getReference("map")
        datRef.child("events").child("${sharedView.selectedEvent!!.id}").get().addOnSuccessListener {
            Log.d("AllEv", "${it.value}")

            var userData = HashMap<Any, Any>()
            userData = it.value as HashMap<Any, Any>
            var el = userData.get("Attendees")!! as HashMap<Any,Any>
            binding.textViewListNumber.text = el.size.toString()

        }
//        datRef.child("events").child()
        binding.imageBackButton.setOnClickListener {
            //back button
            findNavController().popBackStack()
        }

        binding.imageEventList.setOnClickListener {
            //attendees list
            findNavController().navigate(R.id.action_event_to_Rate)
        }

        binding.imageEventConfirm.setOnClickListener {
            //confirm arrival
            if (!coming) {
                val database =
                    FirebaseDatabase.getInstance("https://paw-pal-7f105-default-rtdb.europe-west1.firebasedatabase.app/")
                val dataRef =
                    database.reference// << Ovde podesavas sta gadjas iz realtime-a U ovo slucaju je test

                var newEvent = mapOf(
                    "idUser" to Firebase.auth.currentUser?.uid.toString(),
                    "grade" to "0/0"
                )
                pd.setMessage("DOLAZIM...")
                pd.show()
                dataRef.child("map").child("events")
                    .child("${sharedView.selectedEvent!!.id.toString()}").child("Attendees")
                    .child("${Firebase.auth.currentUser?.uid.toString()}").setValue(newEvent)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Snackbar.make(
                                binding.root,
                                "Prijavljeni na dogadjaj",
                                Snackbar.LENGTH_LONG
                            ).show()
                            Log.d("Task", "Uspesno uradjeno")
                            coming = true
                            binding.textViewConfirm.text = "Dolazite!"
                            binding.imageViewTick.visibility = View.VISIBLE
                            pd.hide()
                        } else {
                            Log.d("Task", "Error ${it.exception}")
                            pd.hide()
                        }
                    }
            }
        }


    }

    fun checkComing() {
        val database =
            FirebaseDatabase.getInstance("https://paw-pal-7f105-default-rtdb.europe-west1.firebasedatabase.app/")
        val dataRef =
            database.reference// << Ovde podesavas sta gadjas iz realtime-a U ovo slucaju je test
        var usrCheck = dataRef.child("map").child("events")
            .child("${sharedView.selectedEvent!!.id.toString()}").child("Attendees")
            .child("${Firebase.auth.currentUser?.uid.toString()}").get()

        usrCheck.addOnSuccessListener { it ->
            Log.d("dataRead", "${it.value}")
            if (it.value != null) {
                coming = true
                binding.textViewConfirm.text = "Dolazite!"
                binding.imageViewTick.visibility = View.VISIBLE
            } else {
                binding.textViewConfirm.text = "Potvrdite dolazak"
                binding.imageViewTick.visibility = View.GONE
            }
        }.addOnFailureListener {
            Log.d("FAIL", "FAIL")
        }

    }

}