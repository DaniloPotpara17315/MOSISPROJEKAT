package com.example.petpal

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.petpal.databinding.FragmentProfileBinding
import com.example.petpal.shared_view_models.MainSharedViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class FragmentProfile : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private val sharedViewModel : MainSharedViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        binding.textViewDogName.text= sharedViewModel.userData.get("Name").toString()
        binding.textView2DogDesc.text = sharedViewModel.userData.get("Description").toString()
        Glide.with(this).load(sharedViewModel.profileImg).into(binding.imageProfile)
        var stat= sharedViewModel.userData.get("Status").toString()
        if(stat == "Druzeljubiv") {
            paintGreen()
        }
        else if(stat == "Nezainteresovan") {
            paintYellow()
        }
        else if(stat == "Agresivan") {
            paintRed()
        }
        else {
            paintOff()
        }
        eventClicks()

    }
    private fun eventClicks(){
        binding.imageButtonStatus.setOnClickListener{
            //logic for status
            val popupMenu:PopupMenu= PopupMenu(context,binding.imageButtonStatus)
            popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item->
                if(checkChange(item.title.toString())){
                    var currUser = mutableMapOf<String, Any>(
                        "Status" to item.title.toString()
                    )
                    var user = Firebase.auth.currentUser
                    if (user != null) {
                        Log.d("DataisHere","${user.uid},${currUser},${item.title.toString()}")
                    }
                    if (user != null) {
                        Firebase.firestore.collection("Users").document(
                            user.uid
                        ).update(currUser).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
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
                                    R.id.action_drz-> paintGreen()
                                    R.id.action_nez -> paintYellow()
                                    R.id.action_agr -> paintRed()
                                    R.id.action_off-> paintOff()
                                }

                            }
                        }.addOnFailureListener{
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
        binding.imageButtonFriends.setOnClickListener{
            //logic for friends
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
                    user.delete().addOnCompleteListener{
                        Snackbar.make(
                            binding.root,
                            "Profil uspesno obrisan",
                            Snackbar.LENGTH_LONG
                        ).show()
                        Firebase.auth.signOut()
                        requireActivity().finish()
                    }
                }
            }
            binding.imageEditDeleteProfDec.visibility = View.INVISIBLE
            binding.imageEditDeleteProfConf.visibility = View.INVISIBLE
        }
        binding.imageLogout.setOnClickListener{
            Firebase.auth.signOut()
            requireActivity().finish()
            //logic for loggout
        }
    }


    private fun paintYellow() {
        binding.imageSwitchKnob.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.yellow_dark
            )
        );
        binding.imageSwitch.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.yellow
            )
        );
        binding.imageSwitchKnobP.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.yellow_dark
            )
        );
    }
    private fun paintGreen() {
        binding.imageSwitchKnob.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.green_dark
            )
        );
        binding.imageSwitch.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.green_light
            )
        );
        binding.imageSwitchKnobP.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.green_dark
            )
        );
    }
    private fun paintRed() {
        binding.imageSwitchKnob.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.red_dark
            )
        );
        binding.imageSwitch.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.red
            )
        );
        binding.imageSwitchKnobP.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.red_dark
            )
        );
    }

    private fun paintOff() {
        binding.imageSwitchKnob.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.gray_dark
            )
        );
        binding.imageSwitch.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.gray_light
            )
        );
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