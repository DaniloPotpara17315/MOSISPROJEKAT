package com.example.petpal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.petpal.databinding.FragmentNavbarBinding

class FragmentNavbar : Fragment() {

    private var _binding : FragmentNavbarBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNavbarBinding.inflate(layoutInflater)

        return binding.root

    }

    private fun setOnClickListeners() {

        val navController = requireActivity().findNavController(R.id.fragment_container)

        binding.navbarIconProfile.setOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.fragmentChat -> {
                    navController.navigate(R.id.action_fragmentChat_to_fragmentProfile)
                }
                R.id.fragmentMap -> {
                    navController.navigate(R.id.action_fragmentMap_to_fragmentProfile)
                }
                else -> {}
            }
        }
        binding.navbarIconChat.setOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.fragmentMap -> {
                    navController.navigate(R.id.action_fragmentMap_to_fragmentChat)
                }
                R.id.fragmentProfile -> {
                    navController.navigate(R.id.action_fragmentProfile_to_fragmentChat)
                }
                else -> {}
            }
        }
        binding.navbarIconMap.setOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.fragmentChat -> {
                    navController.navigate(R.id.action_fragmentChat_to_fragmentMap)
                }
                R.id.fragmentProfile -> {
                    navController.navigate(R.id.action_fragmentProfile_to_fragmentMap)
                }
                else -> {}
            }
        }

    }
}