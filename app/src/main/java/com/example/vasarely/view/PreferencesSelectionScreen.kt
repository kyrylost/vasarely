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
        val montserratRegularFont : Typeface? = ResourcesCompat.getFont(requireContext(), R.font.montserrat_regular)

        binding.welcomeText.typeface = montserratBoldFont
        binding.selectPrefText.typeface = montserratBoldFont


        binding.firstCategory.typeface = montserratBoldFont
        binding.firstCategoryMin.typeface = montserratRegularFont

        binding.byHandButton.typeface = montserratRegularFont
        binding.compGraphButton.typeface = montserratRegularFont


        binding.secondCategory.typeface = montserratBoldFont
        binding.secondCategoryMin.typeface = montserratRegularFont

        binding.stillLifeButton.typeface = montserratRegularFont
        binding.portraitButton.typeface = montserratRegularFont
        binding.landscapeButton.typeface = montserratRegularFont
        binding.marineButton.typeface = montserratRegularFont
        binding.battlePaintingButton.typeface = montserratRegularFont
        binding.interiorButton.typeface = montserratRegularFont
        binding.caricatureButton.typeface = montserratRegularFont
        binding.nudeButton.typeface = montserratRegularFont
        binding.animeButton.typeface = montserratRegularFont
        binding.horrorButton.typeface = montserratRegularFont


        binding.thirdCategory.typeface = montserratBoldFont
        binding.thirdCategoryMin.typeface = montserratRegularFont

        binding.depressedButton.typeface = montserratRegularFont
        binding.funButton.typeface = montserratRegularFont
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}