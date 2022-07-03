package com.example.petpal.screens.profile

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.HandlerCompat.postDelayed
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.petpal.R
import com.example.petpal.activity.ActivityMain
import com.example.petpal.databinding.FragmentProfileBinding
import com.example.petpal.helpers.FirebaseHelper
import com.example.petpal.services.BackgroundCommunicationService
import com.example.petpal.shared_view_models.MainSharedViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.lang.Thread.sleep

class FragmentProfile : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private val sharedViewModel : MainSharedViewModel by activityViewModels()
    lateinit var pd : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pd = ProgressDialog(context)
        pd.setMessage("PROMENE U TOKU...")
        pd.setCancelable(false)
        var usr = Firebase.auth.currentUser
        if(usr == null){

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewDogName.text= sharedViewModel.userData["Name"].toString()
        binding.textView2DogDesc.text = sharedViewModel.userData["Description"].toString()
        Log.d("profileImg", "${sharedViewModel.profileImg}")
        Glide.with(this).load(sharedViewModel.profileImg).into(binding.imageProfile)
        var stat= sharedViewModel.userData["Status"].toString()

        when(stat){
            "Druzeljubiv"-> paintGreen()
            "Nezainteresovan" ->paintYellow()
            "Agresivan"->paintRed()
            else -> {
                paintOff()
            }
        }
        eventClicks()

        sharedViewModel.notifsEnabled.observe(viewLifecycleOwner) {
            if (it) {
                binding.imageProfileNotificationToggle
                    .setImageResource(R.drawable.ic_baseline_toggle_on)
                binding.imageProfileNotificationToggle
                    .setColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.blue_medium),
                        android.graphics.PorterDuff.Mode.SRC_IN)

            } else {
                binding.imageProfileNotificationToggle
                    .setImageResource(R.drawable.ic_baseline_toggle_off)
                binding.imageProfileNotificationToggle
                    .setColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.blue_pale),
                        android.graphics.PorterDuff.Mode.SRC_IN)
                Intent(requireActivity(), BackgroundCommunicationService::class.java).also { intent ->
                        requireActivity().stopService(intent)
                    }
            }
        }
    }
    private fun eventClicks(){
        binding.imageButtonStatus.setOnClickListener{
            //logic for status
            val popupMenu:PopupMenu= PopupMenu(context,binding.imageButtonStatus)
            popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item->
                if(checkChange(item.title.toString())){
                    pd.show()
                    var currUser = mutableMapOf<String, Any>(
                        "Status" to item.title.toString()
                    )
                    var user = Firebase.auth.currentUser
                    if (user != null) {
                        Log.d("DataisHere","${user.uid},${currUser},${item.title.toString()}")
                    }
                    if (user != null) {

                        FirebaseHelper.database.getReference("map").child("users").child(user.uid)
                            .child("status").setValue(item.title.toString())

                        Firebase.firestore.collection("Users").document(
                            user.uid
                        ).update(currUser).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                pd.hide()
                                Snackbar.make(
                                    binding.root,
                                    "Promenjen status uspesno",
                                    Snackbar.LENGTH_LONG
                                ).show()
                                sharedViewModel.userData.set(
                                    "Status",
                                    item.title.toString()
                                )
                                when(item.itemId){
                                    R.id.action_drz -> paintGreen()
                                    R.id.action_nez -> paintYellow()
                                    R.id.action_agr -> paintRed()
                                    R.id.action_off -> paintOff()
                                }
                            }
                        }.addOnFailureListener{
                            pd.hide()
                            Log.d("FailureToChangeStat",it.toString())
                        }
                    }
                }
                true
            })
            popupMenu.show()
        }
        binding.imageButtonProfile.setOnClickListener{
            //logic for profile
            findNavController().navigate(R.id.actoin_goto_settings)
        }
        binding.imageButtonDelete.setOnClickListener{
            //logic for delete
            binding.imageEditDeleteProfDec.visibility = View.VISIBLE
            binding.imageEditDeleteProfConf.visibility = View.VISIBLE
        }
        binding.imageEditDeleteProfDec.setOnClickListener {
            binding.imageEditDeleteProfDec.visibility = View.INVISIBLE
            binding.imageEditDeleteProfConf.visibility = View.INVISIBLE
        }
        binding.imageEditDeleteProfConf.setOnClickListener {
            //add code for deletion of profile and all it's data
            var user = Firebase.auth.currentUser
            val storageRef = Firebase.storage.reference
            val mountainImagesRef = storageRef.child("ProfileImages/${user!!.uid}.png")
            mountainImagesRef.delete().addOnCompleteListener{
                Firebase.firestore.collection("Users").document(
                    user.uid
                ).delete().addOnCompleteListener{

                    val realtimeRef = FirebaseDatabase.getInstance("https://paw-pal-7f105-default-rtdb.europe-west1.firebasedatabase.app/")
                    realtimeRef.getReference("map").child("users").child(Firebase.auth.uid!!).removeValue()
                        .addOnCompleteListener {
                            user.delete().addOnCompleteListener{

                                Firebase.auth.signOut()

                                startActivity(
                                    Intent(context, ActivityMain::class.java)
                                )

                                requireActivity().finish()

                            }
                        }


                }
            }
            binding.imageEditDeleteProfDec.visibility = View.INVISIBLE
            binding.imageEditDeleteProfConf.visibility = View.INVISIBLE
        }
        binding.imageLogout.setOnClickListener{
            Firebase.auth.signOut()

            startActivity(
                Intent(context, ActivityMain::class.java)
            )

            requireActivity().finish()
            //logic for loggout
        }

        binding.buttonProfileNotifications.setOnClickListener {

            sharedViewModel.notifsEnabled.value = !sharedViewModel.notifsEnabled.value!!

            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            if (sharedPref!=null) {
                with (sharedPref.edit()) {
                    putBoolean("notificationsEnabled", sharedViewModel.notifsEnabled.value!!)
                    apply()
                }
            }
        }
    }


    private fun paintYellow() {

        binding.imageSwitchKnobP.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.yellow_dark
            )
        );
    }
    private fun paintGreen() {

        binding.imageSwitchKnobP.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.green_dark
            )
        );
    }
    private fun paintRed() {

        binding.imageSwitchKnobP.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.red_dark
            )
        );
    }

    private fun paintOff() {
        binding.imageSwitchKnobP.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.gray_dark
            )
        );
    }
    private fun checkChange(currValue:String):Boolean{
        return sharedViewModel.userData.get("Status").toString() != currValue.toString()
    }
}