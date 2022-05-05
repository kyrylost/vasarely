package com.example.vasarely.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        val btnCN = popupView.findViewById<TextView>(R.id.settings_nickname_text)
        val popupView1 = layoutInflater.inflate(R.layout.nickname_change_popup, null)
        val changeNick = popupView1.findViewById<TextInputEditText>(R.id.username_input)
        val btnSave = popupView1.findViewById<Button>(R.id.button)
        //----------------------------Navigation between screens------------------------------------
        //to SearchScreen
        binding.searchButton.setOnClickListener {
            val action =
                UserPersonalPageScreenDirections.actionUserPersonalPageScreenToSearchScreen()
            findNavController().navigate(action)
        }
        //to MainScreen
        binding.homeButton.setOnClickListener {
            val action = UserPersonalPageScreenDirections.actionUserPersonalPageScreenToMainScreen()
            findNavController().navigate(action)
        }

        binding.menuImgBtn.setOnClickListener {
            val action =
                UserPersonalPageScreenDirections.actionUserPersonalPageScreenToNewPreferencesScreen()
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

            val addNoteDialog = dialogBuilder.create()
            addNoteDialog.show()

            }
        btnCN.setOnClickListener {
                val dialogBuilder1 = AlertDialog.Builder(context)

                dialogBuilder1.setView(popupView1)
                val addNoteDialog1 = dialogBuilder1.create()
                addNoteDialog1.show()
            btnSave.setOnClickListener {
                val newnickname = changeNick.text.toString()
                if (newnickname.isNotEmpty()) {
                    appViewModel.updateName(newnickname)
                    addNoteDialog1.dismiss()
                } else if (newnickname.isEmpty()) {
                    changeNick.error = getString(R.string.empty_username_error_message)
                }
            }
        }
        }



        /*binding.logoutButton.setOnClickListener {
            appViewModel.signOut()

            val sharedPref = activity?.getSharedPreferences("userLoginData", Context.MODE_PRIVATE) ?: return@setOnClickListener
            val editor = sharedPref.edit()
            editor.putString("remember", "false")
            editor.apply()

            val action = UserPersonalPageScreenDirections.actionUserPersonalPageScreenToSignInSignUpScreen()
            findNavController().navigate(action)
        }*/



        override fun onDestroy() {
            super.onDestroy()
            _binding = null
        }
    }
