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
        // write code here
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}