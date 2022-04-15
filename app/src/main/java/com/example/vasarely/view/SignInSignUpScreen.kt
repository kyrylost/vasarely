package com.example.vasarely.view

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.vasarely.R
import com.example.vasarely.databinding.SearchScreenBinding
import com.example.vasarely.databinding.SignInSignUpScreenBinding

class SignInSignUpScreen: Fragment(R.layout.sign_in_sign_up_screen) {

    private var _binding: SignInSignUpScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}