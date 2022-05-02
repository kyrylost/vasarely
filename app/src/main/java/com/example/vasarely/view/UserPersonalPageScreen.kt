package com.example.vasarely.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vasarely.R
import com.example.vasarely.databinding.UserPersonalPageScreenBinding

class UserPersonalPageScreen: Fragment(R.layout.user_personal_page_screen) {

    private var _binding: UserPersonalPageScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UserPersonalPageScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //----------------------------Navigation between screens---------------------------------------
        //to SearchScreen
        binding.searchButton.setOnClickListener {
            val action = UserPersonalPageScreenDirections.actionUserPersonalPageScreenToSearchScreen()
            findNavController().navigate(action)
        }
        //to MainScreen
        binding.homeButton.setOnClickListener {
            val action = UserPersonalPageScreenDirections.actionUserPersonalPageScreenToMainScreen()
            findNavController().navigate(action)
        }

        binding.menuImgBtn.setOnClickListener {
            val action = UserPersonalPageScreenDirections.actionUserPersonalPageScreenToNewPreferencesScreen()
            findNavController().navigate(action)
        }


        binding.gridImgBtn.setOnClickListener{
            binding.gridSelected.setBackgroundColor(getResources().getColor(R.color.accent));
            binding.columnSelected.setBackgroundColor(getResources().getColor(R.color.white));
        }

        binding.columnImgBtn.setOnClickListener{
            binding.gridSelected.setBackgroundColor(getResources().getColor(R.color.white));
            binding.columnSelected.setBackgroundColor(getResources().getColor(R.color.accent));
        }

        binding.menuImgBtn.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(context)
            val popupView = layoutInflater.inflate(R.layout.menu_user_personal_page_screen, null)

            dialogBuilder.setView(popupView)
            val addNoteDialog = dialogBuilder.create()
            addNoteDialog.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}