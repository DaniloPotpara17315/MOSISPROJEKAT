package com.example.petpal

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.petpal.databinding.FragmentHomescreenBinding

class FragmentHomescreen : Fragment() {

    private lateinit var binding : FragmentHomescreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //PLS ne brisi ovo dugme, treba mi da udjem u drugi activity xD
        //   slobodno ga premestaj kako ti volja tho
        val btnOtherActivity = binding.btnToActivity2
        btnOtherActivity.setOnClickListener{
            startActivity(
                Intent(context, ActivitySecond::class.java)
            )
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