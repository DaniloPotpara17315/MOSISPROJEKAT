package com.example.petpal.screens.chats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petpal.R
import com.example.petpal.adapters.ChatAdapter
import com.example.petpal.databinding.FragmentChatBinding
import com.example.petpal.models.Profile
import com.example.petpal.shared_view_models.MainSharedViewModel

class FragmentChat : Fragment(),ChatAdapter.ChatOperationHandler {

    private val sharedViewModel : MainSharedViewModel by activityViewModels()
    private var _binding : FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataset = mutableListOf<Profile>()

        dataset.add(Profile("Online", Profile.STATUS_ONLINE))
        dataset.add(Profile("Do not disturb", Profile.STATUS_DND))
        dataset.add(Profile("Stay away", Profile.STATUS_AGGRO))
        dataset.add(Profile("Invisible", Profile.STATUS_INVIS))
        dataset.add(Profile("Offline"))

        val recycler = binding.recyclerChat
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = ChatAdapter(requireContext(), dataset,this)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun openChat(person:Profile) {
        sharedViewModel.selectedProfile.value = person
        findNavController().navigate(R.id.action_chat_to_chatroom)
        val navbar: FragmentContainerView? = activity?.findViewById(R.id.fragment_navbar)
        navbar?.visibility = View.GONE

    }

}