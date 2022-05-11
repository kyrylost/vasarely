package com.example.vasarely.view

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vasarely.R
import com.example.vasarely.databinding.SignInSignUpScreenBinding
import com.example.vasarely.viewmodel.AppViewModel

class SignInSignUpScreen: Fragment(R.layout.sign_in_sign_up_screen) {

    private val appViewModel: AppViewModel by activityViewModels()
    private var _binding: SignInSignUpScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        appViewModel.initAppViewModel(Application())

        appViewModel.userMutableLiveData.observe(viewLifecycleOwner) { preferencesAreSelected ->

            if (preferencesAreSelected) {
                appViewModel.getData()
                val action = SignInSignUpScreenDirections.actionSignInSignUpScreenToSearchScreen()
                findNavController().navigate(action)
            }
            else {
                val action = SignInSignUpScreenDirections.actionSignInSignUpScreenToPreferencesSelectionScreen()
                findNavController().navigate(action)
            }
        }

        _binding = SignInSignUpScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = activity?.getSharedPreferences("userLoginData", Context.MODE_PRIVATE) ?: return
        val checkbox = sharedPref.getString("remember", " ")
        val prefEmail = sharedPref.getString("email", " ")
        val prefPassword = sharedPref.getString("password", " ")

        if (checkbox.equals("true")) {

            appViewModel.login(prefEmail!!, prefPassword!!)
            val action = SignInSignUpScreenDirections.actionSignInSignUpScreenToSearchScreen()
            findNavController().navigate(action)

        }

        val montserratBoldFont : Typeface? = ResourcesCompat.getFont(requireContext(), R.font.montserrat_bold)
        val montserratRegularFont : Typeface? = ResourcesCompat.getFont(requireContext(), R.font.montserrat_regular)

        binding.signinText.typeface = montserratBoldFont
        binding.signupText.typeface = montserratBoldFont
        binding.forgetPasswordText.typeface = montserratBoldFont
        binding.emailInput.typeface = montserratRegularFont
        binding.passwordInput.typeface = montserratRegularFont
        binding.usernameInput.typeface = montserratRegularFont
        binding.usernameInput.visibility = GONE
        binding.usernameInputLayout.visibility = GONE
        binding.signupButton.visibility = GONE

        binding.signupText.setOnClickListener {
            val textSingUp = binding.signupText
            val textSingIn = binding.signinText
            val username = binding.usernameInput
            val usernametxt = binding.usernameInputLayout
            val password = binding.forgetPasswordText
            val btnSingUp = binding.signupButton
            val btnSingIn = binding.signinButton

            textSingIn.setTextColor(Color.GRAY)
            textSingUp.setTextColor(Color.parseColor("#0082DD"))
            username.visibility = VISIBLE
            usernametxt.visibility = VISIBLE
            password.visibility = GONE
            btnSingIn.visibility = GONE
            btnSingUp.visibility = VISIBLE
        }

        binding.signinText.setOnClickListener {
            val textSingUp = binding.signupText
            val textSingIn = binding.signinText
            val username = binding.usernameInput
            val usernameInput = binding.usernameInputLayout
            val password = binding.forgetPasswordText
            val btnSingUp = binding.signupButton
            val btnSingIn = binding.signinButton

            textSingIn.setTextColor(Color.parseColor("#0082DD"))
            textSingUp.setTextColor(Color.GRAY)
            username.visibility = GONE
            usernameInput.visibility = GONE
            password.visibility = VISIBLE
            btnSingIn.visibility = VISIBLE
            btnSingUp.visibility = GONE
        }


        binding.signupButton.setOnClickListener{
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val username = binding.usernameInput.text.toString()

            if ((password.isNotEmpty() && password.length >= 6) &&
                (email.isNotEmpty() && email.drop(email.length - 9) == "gmail.com") &&
                username.isNotEmpty()) {

                if (binding.rememberUser.isChecked) {
                    val editor = sharedPref.edit()
                    editor.putString("remember", "true")
                    editor.putString("email", email)
                    editor.putString("password", password)
                    editor.apply()
                    Log.d("up", "work")
                }
                else {
                    val editor = sharedPref.edit()
                    editor.putString("remember", "false")
                    editor.apply()
                    Log.d("up", "dwork")
                }

                appViewModel.register(email, password, username)
            }
            else {
                if (email.isEmpty()) binding.emailInput.error = getString(R.string.empty_email_error_message)
                if (password.isEmpty()) binding.passwordInput.error = getString(R.string.empty_password_error_message)
                if (username.isEmpty()) binding.usernameInput.error = getString(R.string.empty_username_error_message)
                if (password.isNotEmpty() && password.length < 6) binding.passwordInput.error = getString(R.string.short_password_error_message)
                if (password.isNotEmpty() && email.drop(email.length - 9) != "gmail.com") binding.emailInput.error = getString(R.string.incorrect_email_error_message)
            }
        }

        binding.signinButton.setOnClickListener{
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (password.isNotEmpty() && email.isNotEmpty()) {

                if (binding.rememberUser.isChecked) {
                    val editor = sharedPref.edit()
                    editor.putString("remember", "true")
                    editor.putString("email", email)
                    editor.putString("password", password)
                    editor.apply()
                    Log.d("in", "work")
                }
                else {
                    val editor = sharedPref.edit()
                    editor.putString("remember", "false")
                    editor.apply()
                    Log.d("in", "dwork")
                }

                appViewModel.login(email, password)
            }
            else {
                if (email.isEmpty()) binding.emailInput.error = getString(R.string.empty_email_error_message)
                if (password.isEmpty()) binding.passwordInput.error = getString(R.string.empty_password_error_message)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}