package com.example.petpal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petpal.adapters.ChatAdapter
import com.example.petpal.databinding.FragmentChatBinding

class FragmentChat : Fragment() {

    private var _binding : FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataset = mutableListOf<String>("Issa Dog", "Issit Adog?")


        val recycler = binding.recyclerChat
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = ChatAdapter(dataset)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(layoutInflater)
        return binding.root
    }

}