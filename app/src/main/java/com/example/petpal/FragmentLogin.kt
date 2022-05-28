package com.example.petpal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.petpal.databinding.FragmentLoginBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlin.math.log

//pocistio junk code

/*
* JOS NESTO! Pretvori Layout-e u fragmentima da imaju Constraint Layout umesto Frame
* Dovoljno je samo da zamenis tagove, i posle moras da dodelis constraints elementima
* */

//Standardna notacija je da se fajlovi, a i klase, imenuju po formatu TipfajlaImefajla
//Najvise zato sto i sam AS generise stvari u tom formatu, kao FragmentLoginBinding, ispod
class FragmentLogin : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fab = binding.fab
        fab.setOnClickListener {
                findNavController().navigate(R.id.action_back_home)
        }
        val button = binding.buttonLogin
        button.setOnClickListener{
            findNavController().popBackStack()
        }
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.action_back_home)
        }
    }
}
