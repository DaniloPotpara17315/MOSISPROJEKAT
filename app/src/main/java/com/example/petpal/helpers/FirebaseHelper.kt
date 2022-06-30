package com.example.petpal.helpers

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.Visibility
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.drawable.toIcon
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petpal.R
import com.example.petpal.activity.ActivitySecond
import com.example.petpal.adapters.ChatAdapter
import com.example.petpal.databinding.FragmentRegisterBinding
import com.example.petpal.interfaces.MapComms
import com.example.petpal.models.ChatEntry
import com.example.petpal.models.Event
import com.example.petpal.models.Profile
import com.example.petpal.models.ProfileCoordinates
import com.example.petpal.shared_view_models.MainSharedViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.net.URL

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
    fun getMapData(sharedViewModel: MainSharedViewModel, listener:MapComms) {

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
            listener.drawEventMarkers(sharedViewModel.events)
        }
        dataRef.child("users").get().addOnSuccessListener {
            val temp:HashMap<Any,Any> = it.value as HashMap<Any, Any>

            val users : MutableList<ProfileCoordinates> = mutableListOf()

            temp.forEach { user ->

                Firebase.storage.reference.child("ProfileImages/${user.key}.png").downloadUrl.addOnCompleteListener { img ->
                    val userMap = user.value as HashMap<String, Any>


                    val newUser = ProfileCoordinates(user.key as String,
                        userMap["lat"] as Double,
                        userMap["lon"] as Double,
                        userMap["status"] as String,
                        if (img.isSuccessful) img.result.toString() else " "
                    )
                    if (newUser.id != Firebase.auth.uid)
                        users.add(newUser)

                    sharedViewModel.users = users
                    listener.drawEventMarkers(sharedViewModel.events)
                }


            }
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
                //Log.d("halp", "Success?")
                val userDataKeys = document.data!!
                val userDataHashMap = mapToHashMap(userDataKeys)

                var profileImg = " "
                val storageRef = Firebase.storage.reference
                storageRef.child("ProfileImages/${user.uid}.png").downloadUrl.addOnSuccessListener {
                    profileImg = it.toString()
                    Log.d("ObjectName", "$profileImg")

                    val intent = Intent(context, ActivitySecond::class.java)
                    intent.putExtra("userData", userDataHashMap)
                    intent.putExtra("userImg", profileImg)
                    intent.putExtra("passerbyId", activity.intent.getStringExtra("passerbyId"))
                    startActivity(context,intent,null)
                    activity.finish()

                }.addOnFailureListener {
                    profileImg = " "
                    Log.d("FailedToLocat", it.toString())
                    Log.d("FailedToLocat", "NotFound")
                    val intent = Intent(context, ActivitySecond::class.java)
                    intent.putExtra("userData", userDataHashMap)
                    intent.putExtra("userImg", profileImg)
                    startActivity(context,intent,null)
                    activity.finish()
                }


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
                     navController: NavController
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

                                    Firebase.auth.signOut()
                                    navController.navigate(R.id.action_goto_login)
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

    fun getChats(
        context: Context,
        handler: ChatAdapter.ChatOperationHandler,
        recycler:RecyclerView,
        friendList: ArrayList<String>,
        pd: ProgressDialog
    ) {

        val users = Firebase.firestore.collection("Users").get()
            .addOnSuccessListener{
                val document = it.documents
                val dataset = mutableListOf<ChatEntry>()
                val dataRef = database.getReference("chat")
                    .child("invitations")
                    .child(Firebase.auth.currentUser!!.uid)
                    .get()
                    .addOnSuccessListener { ds ->
                        if (ds.value!=null) {
                            val temp:HashMap<Any,Any> = ds.value as HashMap<Any, Any>

                            val adapter = ChatAdapter(context,dataset,handler,friendList)
                            document.forEach { user ->
                                //val entry = temp.get(user.id)
                                if (user.id in temp.keys) {
                                    val storageRef = Firebase.storage.reference
                                    storageRef.child("ProfileImages/${user.id}.png").downloadUrl.addOnCompleteListener() {
                                        val x = temp[user.id] as HashMap<Any, Any>

                                        Log.d("statusCode", "${x["statusCode"] == "1"}")
                                        var prf =  Profile(
                                                user["Name"] as String,
                                        0,
                                        )
                                        prf.imageUri = it.result.toString()
                                        dataset.add(
                                            ChatEntry(
                                                prf,
                                                x["statusCode"].toString(),
                                                user.id
                                                //temp["statusCode"] as Boolean
                                            )
                                        )
                                        adapter.notifyItemInserted(dataset.size)
                                    }
                                }
                                Log.d("dataset","$dataset")
                            }
                            recycler.adapter = adapter
                            recycler.layoutManager = LinearLayoutManager(context)

                        }
                        pd.dismiss()
                    }


            }
    }

    fun notifyInviteAccepted(id : String, pd: ProgressDialog) {

        database.getReference("chat")
            .child("invitations")
            .child(Firebase.auth.currentUser!!.uid)
            .child(id)
            .setValue(
                mapOf("statusCode" to 1)
            )
            .addOnCompleteListener{

                database.getReference("chat")
                    .child("invitations")
                    .child(id)
                    .child(Firebase.auth.currentUser!!.uid)
                    .setValue(
                        mapOf("statusCode" to 1)
                    )
                    .addOnCompleteListener {
                        pd.dismiss()
                    }
            }

    }
    fun notifyFriendAdded(id:String,pd: ProgressDialog){
        database.getReference("chat")
            .child("invitations")
            .child(Firebase.auth.currentUser!!.uid)
            .child(id)
            .setValue(
                mapOf("statusCode" to 1)
            )
            .addOnCompleteListener{
                pd.dismiss()
            }

    }
    fun notifyInviteDeclined(id: String) {
        database.getReference("chat")
            .child("invitations")
            .child(Firebase.auth.currentUser!!.uid)
            .child(id)
            .removeValue()
    }

    fun removeFriend(sharedViewModel:MainSharedViewModel, toHide: ImageView, toShow: ImageView,pd: ProgressDialog) {
        val userId = sharedViewModel.selectedUserKey
        var mainUserId = Firebase.auth.currentUser!!.uid
        var rez = (sharedViewModel.userData.get("Friends") as MutableList<String>)
        rez.remove(userId!!)
        var body:HashMap<String,Any> = hashMapOf<String,Any>(
            "Friends" to rez
        )
        Log.d("TAggerica","{$rez}")
        var database = Firebase.firestore.collection("Users").document(
            mainUserId
        ).update(body).addOnCompleteListener{

            var databaseSecond = Firebase.firestore.collection("Users").document(userId)
            databaseSecond.get().addOnSuccessListener {
                var hisData = it.data
                var hisFriends = (hisData!!.get("Friends") as MutableList<String>)
                hisFriends.remove(mainUserId)
                var bodyToSend:HashMap<String,Any> = hashMapOf<String,Any>(
                    "Friends" to hisFriends
                )
                databaseSecond.update(bodyToSend).addOnSuccessListener {
                    toShow.visibility = View.VISIBLE
                    toHide.visibility = View.GONE
                    pd.dismiss()
                }
            }
        }



    }

}