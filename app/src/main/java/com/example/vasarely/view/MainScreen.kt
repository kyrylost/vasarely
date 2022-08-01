package com.example.vasarely.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vasarely.R
import com.example.vasarely.databinding.MainScreenBinding
import com.example.vasarely.viewmodel.primary.AppViewModel

class MainScreen: Fragment(R.layout.main_screen) {

    private val appViewModel: AppViewModel by activityViewModels()
    private var _binding: MainScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//        appViewModel.userData.observe(viewLifecycleOwner) {
//            appViewModel.processData(it)
//        }

        appViewModel.userViewModel.dataChangeExceptions.observe(viewLifecycleOwner) { exception ->
            Toast.makeText(requireContext(), exception, Toast.LENGTH_LONG).show()
        }

        appViewModel.usersViewModel.dataChangeExceptions.observe(viewLifecycleOwner) { exception ->
            Toast.makeText(requireContext(), exception, Toast.LENGTH_LONG).show()
        }

        _binding = MainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //----------------------------Navigation between screens----------------------------------//
        //From MainScreen to SearchScreen
        binding.searchButton.setOnClickListener {
            val action = MainScreenDirections.actionMainScreenToSearchScreen()
            findNavController().navigate(action)
        }
        //From MainScreen to UserPersonalPageScreen
        binding.userPageButton.setOnClickListener {
            val action = MainScreenDirections.actionMainScreenToUserPersonalPageScreen()
            findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}