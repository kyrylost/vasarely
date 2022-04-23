package com.example.vasarely.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vasarely.R
import com.example.vasarely.databinding.SearchScreenBinding
import com.example.vasarely.viewmodel.AppViewModel

class SearchScreen: Fragment(R.layout.search_screen) {

    private val appViewModel: AppViewModel by activityViewModels()
    private var _binding: SearchScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        appViewModel.userMutableLiveData.observe(viewLifecycleOwner) {
            val action = SearchScreenDirections.actionSearchScreenToPreferencesSelectionScreen()
            findNavController().navigate(action)
        }

        _binding = SearchScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    //----------------------------Navigation between screens---------------------------------------
    /*  binding.homeButton.setOnClickListener {
            val action = SearchScreenDirections.actionSearchScreenToMainScreen()
            findNavController().navigate(action)
        }

        binding.userPageButton.setOnClickListener {
            val action = SearchScreenDirections.actionSearchScreenToUserPersonalPageScreen()
            findNavController().navigate(action)
        }
     */


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}