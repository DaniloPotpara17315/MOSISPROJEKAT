package com.example.petpal

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.petpal.databinding.FragmentHomescreenBinding
import com.example.petpal.shared_view_models.MainSharedViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap


class FragmentHomescreen : Fragment() {

    private lateinit var binding : FragmentHomescreenBinding
    //val pd = ProgressDialog(context)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var auth = Firebase.auth

        if(auth.currentUser != null){

            startActivity(
                Intent(context, ActivitySecond::class.java)
            )
        }
        //pd.setCancelable(false)
        //pd.setMessage("Loading")
//        pd.show()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //PLS ne brisi ovo dugme, treba mi da udjem u drugi activity xD
        //   slobodno ga premestaj kako ti volja tho
        val btnOtherActivity = binding.btnToActivity2
        btnOtherActivity.setOnClickListener {

            val database =
                FirebaseDatabase.getInstance("https://paw-pal-7f105-default-rtdb.europe-west1.firebasedatabase.app/")
            val dataRef = database.getReference("test")// << Ovde podesavas sta gadjas iz realtime-a U ovo slucaju je test

//            Read CODE (async read once) ovo samo procita jednom sta se nalazi u firebase realtime
//            dataRef.child("test").get().addOnSuccessListener {
//                Toast.makeText(context,"${it.value}",Toast.LENGTH_SHORT).show()
//            }

//       Ovde se koriscenje addValueEventListener na data ref zakaci i svaki put kad se desi promena
//       ova dzamutka od postlistenera se izvrsi ispod. U sekciji za onDataChange je ako uspesno detektuje,
//       Tu ti ide logika za promenu informacija


//            val postListener = object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//            //Odje logika, dataSnapshot.Value vraca HashMapu iz cele baze za sada,
//                    val post = dataSnapshot.value
//
//                    Log.d("loadPost:onCancelled", "${post}")
//                    // ...
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {
//                    // Getting Post failed, log a message
//                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
//                }
//            }
//            dataRef.addValueEventListener(postListener)
//            dataRef.removeEventListener(postListener) <-- Ovo se koristi za unsubscribe
//            startActivity(
//                Intent(context, ActivitySecond::class.java)
//            )
        }
        val btnLoginActivity = binding.buttonToLogin
        btnLoginActivity.setOnClickListener{
            findNavController().navigate(R.id.action_goto_login)
        }
        val btnRegisterActivity = binding.buttonToRegister
        btnRegisterActivity.setOnClickListener{
            findNavController().navigate(R.id.action_goto_register)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomescreenBinding.inflate(layoutInflater)
        return binding.root
    }

}