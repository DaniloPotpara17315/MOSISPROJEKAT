package com.example.petpal.helpers

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import com.example.petpal.activity.ActivitySecond
import com.example.petpal.databinding.FragmentRegisterBinding
import com.example.petpal.interfaces.MapDataLoadedListener
import com.example.petpal.models.Event
import com.example.petpal.models.ProfileCoordinates
import com.example.petpal.shared_view_models.MainSharedViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

object FirebaseHelper {

    val database =
        FirebaseDatabase.getInstance("https://paw-pal-7f105-default-rtdb.europe-west1.firebasedatabase.app/")


    private fun mapToHashMap(map: Map<String,Any>) : HashMap<String,Any> {

        val hashMap = java.util.HashMap<String, Any>()
        for (pair in map) {
            hashMap[pair.key] = pair.value
        }
        return hashMap
    }
    fun getMapData(sharedViewModel: MainSharedViewModel, listener:MapDataLoadedListener) {

        val dataRef = database.getReference("map")
        dataRef.child("events").get().addOnSuccessListener {
            val temp:HashMap<Any,Any> = it.value as HashMap<Any, Any>

            val events : MutableList<Event> = mutableListOf()

            temp.forEach { entry ->
                val eventMap = entry.value as HashMap<String, Any>
                val event = Event(eventMap["eventName"] as String,
                    eventMap["eventDesc"] as String,
                    eventMap["eventDate"] as String,
                    eventMap["eventLon"] as Double,
                    eventMap["eventLat"]as Double,
                    entry.key as String
                )
                events.add(event)
            }
            sharedViewModel.events = events
            listener.drawMarkers()
        }
        dataRef.child("users").get().addOnSuccessListener {
            val temp:HashMap<Any,Any> = it.value as HashMap<Any, Any>

            val users : MutableList<ProfileCoordinates> = mutableListOf()

            temp.forEach { user ->

                val userMap = user.value as HashMap<String, Any>

                val newUser = ProfileCoordinates(user.key as String,
                    userMap["lat"] as Double,
                    userMap["lon"] as Double,
                    userMap["status"] as String
                )
                if (newUser.id != Firebase.auth.uid)
                    users.add(newUser)
            }
            sharedViewModel.users = users
            listener.drawMarkers()
        }




    }
    // Funkcija namenjena za ActivityMain,
    // Uzima trenutnog koristnika iz baze i ukoliko je uspesan, pokrece ActivitySecond
    fun getUser(context: Context, activity: Activity) {
        val user = Firebase.auth.currentUser
        var data = Firebase.firestore.collection("Users").document(
            user!!.uid
        ).get()

        data.addOnSuccessListener { document ->
            if (document != null) {
                Log.d("halp", "Success?")
                val userDataKeys = document.data!!
                val userDataHashMap = mapToHashMap(userDataKeys)

                var profileImg : Any = " "
                val storageRef = Firebase.storage.reference
                storageRef.child("ProfileImages/${user.uid}.png").downloadUrl.addOnSuccessListener {
                    profileImg = it
                    Log.d("ObjectName", "$profileImg")

                }.addOnFailureListener {
                    profileImg = " "
                    Log.d("FailedToLocat", "NotFound")
                }

                val intent = Intent(context, ActivitySecond::class.java)
                intent.putExtra("userData", userDataHashMap)

                startActivity(context,intent,null)
                activity.finish()
                Log.d("Nodocumentfound", "User data is set $userDataHashMap")

            } else {
                Log.d("Nodocumentfound", "No such document")
            }
        }
    }

    fun logInUser(context: Context, email: String, password: String, view: View, activity: Activity) {
        val auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser

                if (user != null) {
                    Snackbar.make(
                        view,
                        "Uspešno ulogovan, dobrodošao:" + user.email.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                    getUser(context,activity)
                }

            } else {
                Snackbar.make(view, "Neuspešno logovanje", Snackbar.LENGTH_LONG).show()

            }
        }
    }

    fun registerUser(context:Context,
                     email: String,
                     password: String,
                     binding:FragmentRegisterBinding,
                     imageBitmap:Bitmap,
                     pd: ProgressDialog,
                     ) {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = Firebase.auth.currentUser
                    Log.d("user", user.toString())

                    var currUser : HashMap<String, Any>
                    try {
                        currUser = hashMapOf(
                            "Name" to binding.editTextDogName.text.toString(),
                            "Email" to binding.editTextRegisterEmail.text.toString(),
                            "Breed" to binding.editTextMenuDogBreed.text.toString(),
                            "Description" to binding.editTextRegisterDescription.text.toString(),
                            "Friends" to listOf<String>(),
                            "Status" to binding.dropdownMenuDogStatus.selectedItem.toString()
                        )
                        val db = user?.let { it1 ->
                            Firebase.firestore.collection("Users").document(
                                it1.uid
                            ).set(currUser).addOnCompleteListener {
                                val baos = ByteArrayOutputStream()
                                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
                                val data = baos.toByteArray()

                                val storageRef = Firebase.storage.reference
                                val mountainImagesRef =
                                    storageRef.child("ProfileImages/${user.uid}.png")
                                var uploadTask = mountainImagesRef.putBytes(data)
                                uploadTask.addOnFailureListener {
                                    // Handle unsuccessful uploads
                                    Log.d("PictureFail", "This is a picture upload failure")
                                }.addOnSuccessListener { taskSnapshot ->
                                    // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                                    // ...
                                    pd.hide()
                                    Snackbar.make(
                                        binding.root,
                                        "Korisnik registrovan uspešno",
                                        Snackbar.LENGTH_LONG
                                    ).show()

                                    val intent = Intent(context, ActivitySecond::class.java)
                                    intent.putExtra("userData", currUser)
                                    startActivity(context,intent,null)
                                }
                            }

                        }

                    } catch (e: Exception) {
                        user?.delete()?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                pd.hide()
                                Snackbar.make(
                                    binding.root,
                                    "Neuspešna registracija V2",
                                    Snackbar.LENGTH_LONG
                                ).show()

                            }
                        }
                    }
                } else {
                    pd.hide()
                    Snackbar.make(
                        binding.root,
                        "Neuspešna registracija V1",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
    }
}