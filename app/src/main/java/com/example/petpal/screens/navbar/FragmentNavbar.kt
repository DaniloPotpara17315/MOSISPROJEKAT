package com.example.petpal.screens.navbar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.petpal.R
import com.example.petpal.databinding.FragmentNavbarBinding
import com.example.petpal.shared_view_models.MainSharedViewModel

class FragmentNavbar : Fragment() {

    private var _binding : FragmentNavbarBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel : MainSharedViewModel by activityViewModels()
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
                    resetColors()

                    binding.navbarIconProfile.setColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.white),
                        android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.navbarTitleProfil.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.white)
                    )
                }
                R.id.fragmentMap -> {
                    navController.navigate(R.id.action_fragmentMap_to_fragmentProfile)
                    resetColors()
                    binding.navbarIconProfile.setColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.white),
                        android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.navbarTitleProfil.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.white)
                    )
                }
                else -> {}
            }
        }
        binding.navbarIconChat.setOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.fragmentMap -> {
                    navController.navigate(R.id.action_fragmentMap_to_fragmentChat)
                    resetColors()
                    binding.navbarIconChat.setColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.white),
                        android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.navbarTitleSetnje.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.white)
                    )

                }
                R.id.fragmentProfile -> {
                    navController.navigate(R.id.action_fragmentProfile_to_fragmentChat)
                    resetColors()
                    binding.navbarIconChat.setColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.white),
                        android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.navbarTitleSetnje.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.white)
                    )
                }
                else -> {}
            }
        }
        binding.navbarIconMap.setOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.fragmentChat -> {
                    navController.navigate(R.id.action_fragmentChat_to_fragmentMap)
                    resetColors()
                    binding.navbarIconMap.setColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.white),
                        android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.navbarTitleMap.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.white)
                    )
                }
                R.id.fragmentProfile -> {
                    navController.navigate(R.id.action_fragmentProfile_to_fragmentMap)
                    resetColors()
                    binding.navbarIconMap.setColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.white),
                        android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.navbarTitleMap.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.white)
                    )
                }
                else -> {}
            }
        }

    }

    private fun resetColors() {
        binding.navbarIconMap.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.blue_medium),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        binding.navbarIconChat.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.blue_medium),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        binding.navbarIconProfile.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.blue_medium),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        binding.navbarTitleMap.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_pale))
        binding.navbarTitleSetnje.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_pale))
        binding.navbarTitleProfil.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_pale))
    }

}
