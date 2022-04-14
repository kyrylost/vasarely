package com.example.vasarely.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.vasarely.R
import com.example.vasarely.databinding.SignInSignUpScreenBinding
import com.example.vasarely.databinding.UserPageScreenBinding

class UserPageScreen: Fragment(R.layout.user_page_screen) {

    private var _binding: UserPageScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UserPageScreenBinding.inflate(inflater, container, false)
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