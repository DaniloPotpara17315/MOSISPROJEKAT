package com.example.petpal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.example.petpal.databinding.FragmentRegisterBinding

//pocistio junk code

class FragmentRegister : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spin = binding.dropdownMenuDogBreed
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.dogBreeds,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spin.adapter = adapter
            }
        }
        val spin2 = binding.dropdownMenuDogStatus
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.dogStatus,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spin2.adapter = adapter
            }
        }
        val fab = binding.fab
        fab.setOnClickListener{
            findNavController().popBackStack()
        }
        val butCam = binding.imageCamera
        butCam.setOnClickListener{

        }
        val butReg = binding.buttonReg
        butReg.setOnClickListener{
            findNavController().navigate(R.id.action_goto_login)
        }
        val butPwHide = binding.imagePwHide
        butPwHide.setOnClickListener{
            butPwHide.
            butPwHide.setImageIcon()
        }
    }
}