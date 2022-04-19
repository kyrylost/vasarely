package com.example.vasarely.view

import android.app.Application
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vasarely.R
import com.example.vasarely.databinding.SearchScreenBinding
import com.example.vasarely.databinding.SignInSignUpScreenBinding
import com.example.vasarely.viewmodel.AppViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.collection.LLRBNode

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

        appViewModel.userMutableLiveData.observe(viewLifecycleOwner) {

            // Here is navigation part. Example is below

            val action = SignInSignUpScreenDirections.actionSignInSignUpScreenToPreferencesSelectionScreen()
            findNavController().navigate(action)

        }

        _binding = SignInSignUpScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val montserratBoldFont : Typeface? = ResourcesCompat.getFont(requireContext(), R.font.montserrat_bold)
        val montserratRegularFont : Typeface? = ResourcesCompat.getFont(requireContext(), R.font.montserrat_regular)

        binding.signinText.typeface = montserratBoldFont
        binding.signupText.typeface = montserratBoldFont
        binding.forgetPasswordText.typeface = montserratBoldFont
        binding.emailInput.typeface = montserratRegularFont
        binding.passwordInput.typeface = montserratRegularFont
        binding.usernameInput.typeface = montserratRegularFont
        binding.usernameInput.visibility = View.GONE
        binding.usernameInputLayout.visibility = View.GONE
        binding.signupButton.visibility = View.GONE

        binding.signupButton.setOnClickListener{
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val username = binding.usernameInput.text.toString()

            if (password.isNotEmpty() && email.isNotEmpty() && username.isNotEmpty()) {
                appViewModel.register(email, password, username)
            }
            else {
                if (email.isEmpty()) binding.emailInput.error = getString(R.string.empty_email_error_message)
                if (password.isEmpty()) binding.passwordInput.error = getString(R.string.empty_password_error_message)
                if (username.isEmpty()) binding.usernameInput.error = getString(R.string.empty_username_error_message)
            }
        }

        binding.signupText.setOnClickListener {
            val textSingUp = binding.signupText;
            val textSingIn = binding.signinText;
            val Username = binding.usernameInput
            val Usernametxt = binding.usernameInputLayout
            val Password = binding.forgetPasswordText
            val btnSingUp = binding.signupButton
            val btnSingIn = binding.signinButton

            textSingIn.setTextColor(Color.GRAY);
            textSingUp.setTextColor(Color.parseColor("#0082DD"));
                Username.visibility = View.VISIBLE
                Usernametxt.visibility = View.VISIBLE
                Password.visibility = View.GONE
                btnSingIn.visibility = View.GONE
                btnSingUp.visibility = View.VISIBLE

        }

        binding.signinText.setOnClickListener {
            val textSingUp = binding.signupText;
            val textSingIn = binding.signinText;
            val Username = binding.usernameInput
            val Usernametxt = binding.usernameInputLayout
            val Password = binding.forgetPasswordText
            val btnSingUp = binding.signupButton
            val btnSingIn = binding.signinButton

            textSingIn.setTextColor(Color.parseColor("#0082DD"));
            textSingUp.setTextColor(Color.GRAY);
            Username.visibility = View.GONE
            Usernametxt.visibility = View.GONE
            Password.visibility = View.VISIBLE
            btnSingIn.visibility = View.VISIBLE
            btnSingUp.visibility = View.GONE


        }

        binding.signupButton.setOnClickListener{
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (password.isNotEmpty() && email.isNotEmpty()) {
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