package com.example.vasarely.view

import android.app.AlertDialog
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
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

        appViewModel.userData.observe(viewLifecycleOwner) {
            appViewModel.processData(it)
        }

        _binding = UserPersonalPageScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val montserratBoldFont : Typeface? = ResourcesCompat.getFont(requireContext(), R.font.montserrat_bold)
        val montserratRegularFont : Typeface? = ResourcesCompat.getFont(requireContext(), R.font.montserrat_regular)

        binding.username.typeface = montserratBoldFont
        binding.subs.typeface = montserratBoldFont
        binding.follow.typeface = montserratBoldFont
        binding.addWork.typeface = montserratBoldFont

        if (appViewModel.isLocalDataInitialized())
            binding.username.text = appViewModel.localData.username


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
            binding.gridSelected.setBackgroundColor(resources.getColor(R.color.accent))
            binding.columnSelected.setBackgroundColor(resources.getColor(R.color.white))
        }

        binding.columnImgBtn.setOnClickListener {
            binding.gridSelected.setBackgroundColor(resources.getColor(R.color.white))
            binding.columnSelected.setBackgroundColor(resources.getColor(R.color.accent))
        }

        binding.menuImgBtn.setOnClickListener {

            val popupView = layoutInflater.inflate(R.layout.menu_user_personal_page_screen, null)

            val likedPostButton = popupView.findViewById<ImageButton>(R.id.likedPostIcon)
            val likedPostText = popupView.findViewById<TextView>(R.id.likedPostText)
            likedPostText.typeface = montserratBoldFont

            val newPrefButton = popupView.findViewById<ImageButton>(R.id.newPrefButton)
            val newPrefText = popupView.findViewById<TextView>(R.id.new_pref_text)
            newPrefText.typeface = montserratBoldFont

            val changeNicknameTextView = popupView.findViewById<TextView>(R.id.settings_nickname_text)
            changeNicknameTextView.typeface = montserratBoldFont

            val changePasswordTextView = popupView.findViewById<TextView>(R.id.key_text)
            changePasswordTextView.typeface = montserratBoldFont

            val logoutButton = popupView.findViewById<ImageButton>(R.id.logoutButton)
            val logoutText = popupView.findViewById<TextView>(R.id.logout_text)
            logoutText.typeface = montserratBoldFont

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
                val passwordPopup = layoutInflater.inflate(R.layout.change_password_popup, null)
                val dialogBuilderPassword = AlertDialog.Builder(context)
                dialogBuilderPassword.setView(passwordPopup)

                val changePasswordText = passwordPopup.findViewById<TextView>(R.id.changePasswordPopupTextView)
                val passwordInputPopup = passwordPopup.findViewById<TextInputEditText>(R.id.password_input)
                val savePasswordButton = passwordPopup.findViewById<Button>(R.id.savePasswordPopup)
                changePasswordText.typeface = montserratBoldFont
                passwordInputPopup.typeface = montserratRegularFont
                savePasswordButton.typeface = montserratBoldFont

                val changePasswordDialog = dialogBuilderPassword.create()
                changePasswordDialog.show()
            }

            changeNicknameTextView.setOnClickListener {

                val nicknamePopup = layoutInflater.inflate(R.layout.nickname_change_popup, null)

                val changeNicknameText = nicknamePopup.findViewById<TextView>(R.id.changeNicknamePopupTextView)
                val btnSaveNickname = nicknamePopup.findViewById<Button>(R.id.savePasswordPopup)
                val changeNick = nicknamePopup.findViewById<TextInputEditText>(R.id.username_input)
                changeNicknameText.typeface = montserratBoldFont
                btnSaveNickname.typeface = montserratBoldFont
                changeNick.typeface = montserratRegularFont

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
