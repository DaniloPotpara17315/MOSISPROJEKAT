package com.example.petpal.screens.settings

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.app.ProgressDialog
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.petpal.R
import com.example.petpal.databinding.FragmentProfileSettingsBinding
import com.example.petpal.shared_view_models.MainSharedViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream


class FragmentProfileSettings : Fragment() {

    private lateinit var binding:FragmentProfileSettingsBinding
    private lateinit var user: FirebaseUser
    private val REQUEST_IMAGE_CAPTURE = 1;
    lateinit var pd: ProgressDialog
    private val sharedViewModel : MainSharedViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pd = ProgressDialog(context)

        pd.setCancelable(false)
        pd.setMessage("PROMENE U TOKU...")
        user = Firebase.auth.currentUser!!
        if(user == null){

        }
        else{

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileSettingsBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this).load(sharedViewModel.profileImg).into(binding.imageProfile)
        binding.imageBackButton.setOnClickListener{
            //back button funkcija
            findNavController().navigate(R.id.action_backHome)
        }
        binding.imageProfile.setOnClickListener{
            //edit camera
            pd.show()
            dispatchTakePictureIntent()
        }
        binding.imageButtonDogName.setOnClickListener{
            //change dog name funkcija
        }

        binding.imageButtonDogDesc.setOnClickListener{
            //change doggy desc funkcija
        }
        binding.imageButtonDogBreed.setOnClickListener{
            //change doggy breed funkcija
        }
        binding.imageButtonDogEmail.setOnClickListener{
            //change email funkcija
            findNavController().navigate(R.id.action_goto_changeEmail)
        }
        binding.imageButtonDogPassword.setOnClickListener{
            //change password funkcija
            findNavController().navigate(R.id.action_goto_ChangePw)
        }
        binding.imageEditName.setOnClickListener{
            //Show name form
            binding.textProfileName.visibility=INVISIBLE
            binding.imageEditName.visibility=INVISIBLE
            binding.imageEditDogNameDec.visibility = VISIBLE
            binding.imageEditDogNameConf.visibility = VISIBLE
            binding.imageDogNameBackground.visibility = VISIBLE
            var el = sharedViewModel.userData.get("Name").toString()
            binding.editTextDogName.setText(el)
            binding.editTextDogName.visibility = VISIBLE
        }

        binding.imageEditDogNameDec.setOnClickListener{
            //cancel code
            binding.textProfileName.visibility=VISIBLE
            binding.imageEditName.visibility=VISIBLE
            binding.imageEditDogNameDec.visibility = INVISIBLE
            binding.imageEditDogNameConf.visibility = INVISIBLE
            binding.imageDogNameBackground.visibility = INVISIBLE
            binding.editTextDogName.visibility = INVISIBLE
        }
        binding.imageEditDogNameConf.setOnClickListener {
            //confirm code change name

            pd.show()
            var currUser = mutableMapOf<String, Any>(
                "Name" to binding.editTextDogName.text.toString(),
            )

            Firebase.firestore.collection("Users").document(
                user.uid
            ).update(currUser).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    pd.hide()
                    Snackbar.make(
                        binding.root,
                        "Promenjeno ime uspešno",
                        Snackbar.LENGTH_LONG
                    ).show()
                    sharedViewModel.userData.set("Name", binding.editTextDogName.text.toString())
                    binding.textProfileName.visibility = VISIBLE
                    binding.imageEditName.visibility = VISIBLE
                    binding.imageEditDogNameDec.visibility = INVISIBLE
                    binding.imageEditDogNameConf.visibility = INVISIBLE
                    binding.imageDogNameBackground.visibility = INVISIBLE
                    binding.editTextDogName.visibility = INVISIBLE
                }
                else{
                    pd.hide()
                    Snackbar.make(
                        binding.root,
                        "Neuspešna promena",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

        }

        binding.imageEditDogDesc.setOnClickListener{
            binding.textProfileDesc.visibility=INVISIBLE
            binding.imageEditDogDesc.visibility=INVISIBLE
            binding.imageEditDogDescDec.visibility = VISIBLE
            binding.imageEditDogDescConf.visibility = VISIBLE
            binding.imageDogDescBackground.visibility = VISIBLE
            var el = sharedViewModel.userData.get("Description").toString()
            binding.editTextDogDesc.setText(el)
            binding.editTextDogDesc.visibility = VISIBLE
        }
        binding.imageEditDogDescDec.setOnClickListener{
            //cancel code
            binding.textProfileDesc.visibility=VISIBLE
            binding.imageEditDogDesc.visibility=VISIBLE
            binding.imageEditDogDescDec.visibility = INVISIBLE
            binding.imageEditDogDescConf.visibility = INVISIBLE
            binding.imageDogDescBackground.visibility = INVISIBLE
            binding.editTextDogDesc.visibility = INVISIBLE
        }
        binding.imageEditDogDescConf.setOnClickListener{
            //confirm code change opis
            pd.show()
            var currUser = mutableMapOf<String, Any>(
                "Description" to binding.editTextDogDesc.text.toString(),
            )

            Firebase.firestore.collection("Users").document(
                user.uid
            ).update(currUser).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    pd.hide()
                    Snackbar.make(
                        binding.root,
                        "Promenjen opis uspešno",
                        Snackbar.LENGTH_LONG
                    ).show()
                    sharedViewModel.userData.set(
                        "Description",
                        binding.editTextDogDesc.text.toString()
                    )
                    binding.textProfileDesc.visibility = VISIBLE
                    binding.imageEditDogDesc.visibility = VISIBLE
                    binding.imageEditDogDescDec.visibility = INVISIBLE
                    binding.imageEditDogDescConf.visibility = INVISIBLE
                    binding.imageDogDescBackground.visibility = INVISIBLE
                    binding.editTextDogDesc.visibility = INVISIBLE
                } else {
                    pd.hide()
                    Snackbar.make(
                        binding.root,
                        "Neuspešna promena",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

        binding.imageEditChangeRace.setOnClickListener{

            binding.textProfileBreed.visibility=INVISIBLE
            binding.imageEditChangeRace.visibility=INVISIBLE
            binding.imageEditDogBreedDec.visibility = VISIBLE
            binding.imageEditDogBreedConf.visibility = VISIBLE
            binding.imageDogBreedBackground.visibility = VISIBLE
            var el = sharedViewModel.userData.get("Breed").toString()
            binding.editTextDogBreed.setText(el)
            binding.editTextDogBreed.visibility = VISIBLE
        }

        binding.imageEditDogBreedDec.setOnClickListener{
            //cancel code
            binding.textProfileBreed.visibility=VISIBLE
            binding.imageEditChangeRace.visibility=VISIBLE
            binding.imageEditDogBreedDec.visibility = INVISIBLE
            binding.imageEditDogBreedConf.visibility = INVISIBLE
            binding.imageDogBreedBackground.visibility = INVISIBLE
            binding.editTextDogBreed.visibility = INVISIBLE
        }
        binding.imageEditDogBreedConf.setOnClickListener{

            pd.show()
            var currUser = mutableMapOf<String, Any>(
                "Breed" to binding.editTextDogBreed.text.toString(),
            )

            Firebase.firestore.collection("Users").document(
                user.uid
            ).update(currUser).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    pd.hide()
                    Snackbar.make(
                        binding.root,
                        "Promenjena rasa uspešno",
                        Snackbar.LENGTH_LONG
                    ).show()
                    sharedViewModel.userData.set(
                        "Breed",
                        binding.editTextDogBreed.text.toString()
                    )
                    binding.textProfileBreed.visibility=VISIBLE
                    binding.imageEditChangeRace.visibility=VISIBLE
                    binding.imageEditDogBreedDec.visibility = INVISIBLE
                    binding.imageEditDogBreedConf.visibility = INVISIBLE
                    binding.imageDogBreedBackground.visibility = INVISIBLE
                    binding.editTextDogBreed.visibility = INVISIBLE
                } else {
                    pd.hide()
                    Snackbar.make(
                        binding.root,
                        "Neuspešna promena",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
            binding.textProfileBreed.visibility=VISIBLE
            binding.imageEditChangeRace.visibility=VISIBLE
            binding.imageEditDogBreedDec.visibility = INVISIBLE
            binding.imageEditDogBreedConf.visibility = INVISIBLE
            binding.imageDogBreedBackground.visibility = INVISIBLE
            binding.editTextDogBreed.visibility = INVISIBLE
        }
    }


    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            var imageBitmap = data?.extras?.get("data") as Bitmap

            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
            val data = baos.toByteArray()

            val storageRef = Firebase.storage.reference
            val mountainImagesRef = storageRef.child("ProfileImages/${user.uid}.png")
            var uploadTask = mountainImagesRef.putBytes(data)
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
                pd.hide()
                Log.d("PictureFail","This is a picture upload failure")
            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...
                Snackbar.make(
                    binding.root,
                    "Slika uspesno promenjena",
                    Snackbar.LENGTH_LONG
                ).show()
                binding.imageProfile.setImageBitmap(imageBitmap)
                storageRef.child("ProfileImages/${user.uid}.png").downloadUrl.addOnSuccessListener{
                    sharedViewModel.profileImg = it
                    pd.hide()
                    Log.d("ObjectName","${sharedViewModel.profileImg}")
                }
            }
            //imageView.setImageBitmap(imageBitmap)
        }
    }
}