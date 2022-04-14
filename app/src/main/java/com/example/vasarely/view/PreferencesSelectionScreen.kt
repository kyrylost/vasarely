package com.example.vasarely.view

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.vasarely.R
import com.example.vasarely.databinding.PreferencesSelectionScreenBinding
import com.example.vasarely.databinding.SearchScreenBinding

class PreferencesSelectionScreen: Fragment(R.layout.preferences_selection_screen) {

    private var _binding: PreferencesSelectionScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PreferencesSelectionScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val montserratBoldFont : Typeface? = ResourcesCompat.getFont(requireContext(), R.font.montserrat_bold)
        binding.welcomeText.typeface = montserratBoldFont
        binding.selectPrefText.typeface = montserratBoldFont
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}