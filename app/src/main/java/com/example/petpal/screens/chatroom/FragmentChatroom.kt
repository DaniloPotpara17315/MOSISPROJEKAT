package com.example.petpal.screens.chatroom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.petpal.R
import com.example.petpal.databinding.FragmentChatroomBinding
import com.example.petpal.shared_view_models.MainSharedViewModel

class FragmentChatroom : Fragment() {
    private val sharedViewModel : MainSharedViewModel by activityViewModels()
    private var _binding : FragmentChatroomBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()

        Glide.with(requireContext()).load(R.drawable.placeholder_dog)
            .into(binding.imageChatroomHeaderProfile)

        binding.textChatroomTitle.text = sharedViewModel.selectedProfile.value!!.name

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatroomBinding.inflate(layoutInflater)


        return binding.root
    }

    private fun setOnClickListeners() {

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            sharedViewModel.selectedProfile.value=null
            findNavController().popBackStack()
            val navbar: FragmentContainerView? = activity?.findViewById(R.id.fragment_navbar)
            navbar?.visibility = View.VISIBLE
        }

        binding.buttonChatroomBack.setOnClickListener {
            sharedViewModel.selectedProfile.value=null
            findNavController().popBackStack()
            val navbar: FragmentContainerView? = activity?.findViewById(R.id.fragment_navbar)
            navbar?.visibility = View.VISIBLE
        }
    }

}