package com.example.vasarely.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vasarely.R
import com.example.vasarely.databinding.UserPersonalPageScreenBinding
import com.example.vasarely.viewmodel.AppViewModel
import com.google.android.material.textfield.TextInputEditText

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
        val popupView = layoutInflater.inflate(R.layout.menu_user_personal_page_screen, null)

        val nicknamePopup = layoutInflater.inflate(R.layout.nickname_change_popup, null)
        val changeNicknameTextView = popupView.findViewById<TextView>(R.id.settings_nickname_text)
        val btnSaveNickname = nicknamePopup.findViewById<Button>(R.id.button)
        val changeNick = nicknamePopup.findViewById<TextInputEditText>(R.id.username_input)

        val passwordPopup = layoutInflater.inflate(R.layout.change_password_popup, null)
        val changePasswordTextView = popupView.findViewById<TextView>(R.id.key_text)

        val logoutButton = popupView.findViewById<ImageButton>(R.id.logoutButton)
        val logoutText = popupView.findViewById<TextView>(R.id.logout_text)

        val likedPostButton = popupView.findViewById<ImageButton>(R.id.likedPostIcon)
        val likedPostText = popupView.findViewById<TextView>(R.id.likedPostText)

        val newPrefButton = popupView.findViewById<ImageButton>(R.id.newPrefButton)
        val newPrefText = popupView.findViewById<TextView>(R.id.new_pref_text)


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


        binding.gridImgBtn.setOnClickListener {
            binding.gridSelected.setBackgroundColor(getResources().getColor(R.color.accent));
            binding.columnSelected.setBackgroundColor(getResources().getColor(R.color.white));
        }

        binding.columnImgBtn.setOnClickListener {
            binding.gridSelected.setBackgroundColor(getResources().getColor(R.color.white));
            binding.columnSelected.setBackgroundColor(getResources().getColor(R.color.accent));
        }

        binding.menuImgBtn.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setView(popupView)

            val settingsDialog = dialogBuilder.create()
            settingsDialog.show()

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

            changePasswordTextView.setOnClickListener {
                val dialogBuilderPassword = AlertDialog.Builder(context)
                dialogBuilderPassword.setView(passwordPopup)

                val changePasswordDialog = dialogBuilderPassword.create()
                changePasswordDialog.show()
            }

            changeNicknameTextView.setOnClickListener {
                val dialogBuilderNickname = AlertDialog.Builder(context)
                dialogBuilderNickname.setView(nicknamePopup)

                val changeNicknameDialog = dialogBuilderNickname.create()
                changeNicknameDialog.show()

                btnSaveNickname.setOnClickListener {
                    val newNickname = changeNick.text.toString()
                    if (newNickname.isNotEmpty()) {
                        appViewModel.updateName(newNickname)
                        changeNicknameDialog.dismiss()
                    } else if (newNickname.isEmpty()) {
                        changeNick.error = getString(R.string.empty_username_error_message)
                    }
                }
            }

        }



    }

        override fun onDestroy() {
            super.onDestroy()
            _binding = null
        }
    }
