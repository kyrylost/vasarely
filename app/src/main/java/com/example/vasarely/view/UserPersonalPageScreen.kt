package com.example.vasarely.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vasarely.R
import com.example.vasarely.databinding.UserPersonalPageScreenBinding
import com.example.vasarely.viewmodel.AppViewModel

class UserPersonalPageScreen: Fragment(R.layout.user_personal_page_screen) {

    private val appViewModel: AppViewModel by activityViewModels()
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

        //----------------------------Navigation between screens------------------------------------
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
            val settingsDialog = dialogBuilder.create()
            settingsDialog.show()

            val logoutButton = popupView.findViewById<ImageButton>(R.id.logoutButton)
            val logoutText = popupView.findViewById<TextView>(R.id.logout_text)

            val likedPostButton = popupView.findViewById<ImageButton>(R.id.likedPostIcon)
            val likedPostText = popupView.findViewById<TextView>(R.id.likedPostText)

            val newPrefButton = popupView.findViewById<ImageButton>(R.id.newPrefButton)
            val newPrefText = popupView.findViewById<TextView>(R.id.new_pref_text)

            logoutButton.setOnClickListener {
                appViewModel.logout()

                val sharedPref = activity?.getSharedPreferences("userLoginData", Context.MODE_PRIVATE) ?: return@setOnClickListener
                val editor = sharedPref.edit()
                editor.putString("remember", "false")
                editor.apply()

                settingsDialog.dismiss()

                val action = UserPersonalPageScreenDirections.actionUserPersonalPageScreenToSignInSignUpScreen()
                findNavController().navigate(action)
            }

            logoutText.setOnClickListener {
                appViewModel.logout()

                val sharedPref = activity?.getSharedPreferences("userLoginData", Context.MODE_PRIVATE) ?: return@setOnClickListener
                val editor = sharedPref.edit()
                editor.putString("remember", "false")
                editor.apply()

                settingsDialog.dismiss()

                val action = UserPersonalPageScreenDirections.actionUserPersonalPageScreenToSignInSignUpScreen()
                findNavController().navigate(action)
            }

            likedPostButton.setOnClickListener {
                val action = UserPersonalPageScreenDirections.actionUserPersonalPageScreenToLikedPostScreen()
                findNavController().navigate(action)

                settingsDialog.dismiss()
            }

            likedPostText.setOnClickListener {
                val action = UserPersonalPageScreenDirections.actionUserPersonalPageScreenToLikedPostScreen()
                findNavController().navigate(action)

                settingsDialog.dismiss()
            }

            newPrefButton.setOnClickListener {
                val action = UserPersonalPageScreenDirections.actionUserPersonalPageScreenToNewPreferencesScreen()
                findNavController().navigate(action)

                settingsDialog.dismiss()
            }

            newPrefText.setOnClickListener {
                val action = UserPersonalPageScreenDirections.actionUserPersonalPageScreenToNewPreferencesScreen()
                findNavController().navigate(action)

                settingsDialog.dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}