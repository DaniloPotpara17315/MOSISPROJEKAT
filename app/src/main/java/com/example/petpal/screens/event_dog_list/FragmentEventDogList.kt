package com.example.petpal.screens.event_dog_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petpal.R
import com.example.petpal.adapters.AttendeesAdapter
import com.example.petpal.databinding.FragmentEventDogListBinding
import com.example.petpal.models.Profile
import com.example.petpal.shared_view_models.MainSharedViewModel

class FragmentEventDogList : Fragment(), AttendeesAdapter.AttendeeListener {

    private val sharedView: MainSharedViewModel by activityViewModels()
    private lateinit var binding:FragmentEventDogListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventDogListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler = binding.recyclerEventDogList
        val dataToShow = sharedView.actualAttendeeInfo
        dataToShow.sortByDescending {
            var ret = it.rate.split("/")
            if (ret[1].toFloat() == 0F) {
                0F
            } else {
                ret[0].toFloat() / ret[1].toFloat()
            }
        }

        val adapter = AttendeesAdapter(requireContext(), sharedView,dataToShow,this)
        recycler.adapter=adapter
        setFragmentResultListener("rateUpdate"){
            _,_ ->
            adapter.notifyDataSetChanged()
        }
        recycler.layoutManager = LinearLayoutManager(requireContext())
        setOnClickListeners()
    }


    fun setOnClickListeners(){
        binding.imageBackButton2.setOnClickListener {
            //back button
            sharedView.actualAttendeeInfo = mutableListOf<Profile>()
            findNavController().popBackStack()
        }
    }

    override fun respond() {
        findNavController().navigate(R.id.action_goto_dogRateDialog)
    }

}