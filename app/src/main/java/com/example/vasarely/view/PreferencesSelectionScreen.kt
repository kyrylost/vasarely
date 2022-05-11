package com.example.vasarely.view

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vasarely.R
import com.example.vasarely.databinding.PreferencesSelectionScreenBinding
import com.example.vasarely.viewmodel.AppViewModel


class PreferencesSelectionScreen: Fragment(R.layout.preferences_selection_screen) {

    private val appViewModel: AppViewModel by activityViewModels()
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

        val montserratBoldFont: Typeface? =
            ResourcesCompat.getFont(requireContext(), R.font.montserrat_bold)
        val montserratRegularFont: Typeface? =
            ResourcesCompat.getFont(requireContext(), R.font.montserrat_regular)

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
        var byHandClicked = 0
        var computerGraphClicked = 0
        var depressedButtonClicked = 0
        var funButtonClicked = 0
        var clickS = 0
        var clickP = 0
        var clickL = 0
        var clickM = 0
        var clickB = 0
        var clickI = 0
        var clickC = 0
        var clickN = 0
        var clickA = 0
        var clickH = 0
        var min = 0
        var min1 = 0

        binding.byHandButton.setOnClickListener {
            val byHand = binding.byHandButton

            byHandClicked += 1
            if (byHandClicked > 2) byHandClicked = 1
            if (byHandClicked !=2) {
                byHand.setBackgroundColor(Color.parseColor("#0082DD"))
                byHand.setTextColor(Color.WHITE)
                min1 += 1
            }else{
                byHand.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                byHand.setTextColor(Color.BLACK)
                min1-=1
            }
        }
        binding.compGraphButton.setOnClickListener {
            val compGraph = binding.compGraphButton

            computerGraphClicked += 1
            if (computerGraphClicked > 2) computerGraphClicked = 1
            if (computerGraphClicked!=2) {
                compGraph.setBackgroundColor(Color.parseColor("#0082DD"))
                compGraph.setTextColor(Color.WHITE)
                min1 += 1

            }else{
                compGraph.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                compGraph.setTextColor(Color.BLACK)
                min -= 1
            }
        }

        binding.depressedButton.setOnClickListener {
            val depress = binding.depressedButton
            depressedButtonClicked += 1
            if (depressedButtonClicked > 2) depressedButtonClicked = 1
            if (depressedButtonClicked != 2) {
                depress.setBackgroundColor(Color.parseColor("#0082DD"))
                depress.setTextColor(Color.WHITE)}
            else{
                depress.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                depress.setTextColor(Color.BLACK)
            }
        }
        binding.funButton.setOnClickListener {
            val funBut = binding.funButton
            funButtonClicked += 1
            if (funButtonClicked > 2) depressedButtonClicked = 1
            if (funButtonClicked != 2) {
                funBut.setBackgroundColor(Color.parseColor("#0082DD"))
                funBut.setTextColor(Color.WHITE)}
            else{
                funBut.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                funBut.setTextColor(Color.BLACK)
            }
        }

        binding.stillLifeButton.setOnClickListener {
            val funBut = binding.stillLifeButton
            clickS += 1
            if (clickS > 2) clickS = 1
            if (clickS != 2) {
                funBut.setBackgroundColor(Color.parseColor("#0082DD"))
                funBut.setTextColor(Color.WHITE)
            min +=1
            }else{
                funBut.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                funBut.setTextColor(Color.BLACK)
                min-=1
            }
        }
        binding.portraitButton.setOnClickListener {
            val funBut = binding.portraitButton
            clickP += 1
            if (clickP > 2) clickP = 1
            if (clickP != 2) {
                funBut.setBackgroundColor(Color.parseColor("#0082DD"))
                funBut.setTextColor(Color.WHITE)
                min +=1
            }else{
                funBut.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                funBut.setTextColor(Color.BLACK)
                min -=1
            }
        }
        binding.landscapeButton.setOnClickListener {
            val funBut = binding.landscapeButton
            clickL += 1
            if (clickL > 2) clickL = 1
            if (clickL != 2) {
                funBut.setBackgroundColor(Color.parseColor("#0082DD"))
                funBut.setTextColor(Color.WHITE)
                min +=1
            }else{
                funBut.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                funBut.setTextColor(Color.BLACK)
                min -=1
            }
        }
        binding.marineButton.setOnClickListener {
            val funBut = binding.marineButton
            clickM += 1
            if (clickM > 2) clickM = 1
            if (clickM != 2) {
                funBut.setBackgroundColor(Color.parseColor("#0082DD"))
                funBut.setTextColor(Color.WHITE)
                min +=1
            }else{
                funBut.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                funBut.setTextColor(Color.BLACK)
                min -=1
            }
        }
        binding.battlePaintingButton.setOnClickListener {
            val funBut = binding.battlePaintingButton
            clickB += 1
            if (clickB > 2) clickB = 1
            if (clickB != 2) {
                funBut.setBackgroundColor(Color.parseColor("#0082DD"))
                funBut.setTextColor(Color.WHITE)
                min +=1
            }else{
                funBut.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                funBut.setTextColor(Color.BLACK)
                min -=1
            }
        }
        binding.interiorButton.setOnClickListener {
            val funBut = binding.interiorButton
            clickI += 1
            if (clickI > 2) clickI = 1
            if (clickI != 2) {
                funBut.setBackgroundColor(Color.parseColor("#0082DD"))
                funBut.setTextColor(Color.WHITE)
                min +=1
            }else{
                funBut.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                funBut.setTextColor(Color.BLACK)
                min -=1
            }
        }
        binding.caricatureButton.setOnClickListener {
            val funBut = binding.caricatureButton
            clickC += 1
            if (clickC > 2) clickC = 1
            if (clickC != 2) {
                funBut.setBackgroundColor(Color.parseColor("#0082DD"))
                funBut.setTextColor(Color.WHITE)
                min +=1
            }else{
                funBut.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                funBut.setTextColor(Color.BLACK)
                min -=1
            }
        }
        binding.nudeButton.setOnClickListener {
            val funBut = binding.nudeButton
            clickN += 1
            if (clickN > 2) clickN = 1
            if (clickN != 2) {
                funBut.setBackgroundColor(Color.parseColor("#0082DD"))
                funBut.setTextColor(Color.WHITE)
                min +=1
            }else{
                funBut.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                funBut.setTextColor(Color.BLACK)
                min -=1
            }
        }
        binding.animeButton.setOnClickListener {
            val funBut = binding.animeButton
            clickA += 1
            if (clickA > 2) clickA = 1
            if (clickA != 2) {
                funBut.setBackgroundColor(Color.parseColor("#0082DD"))
                funBut.setTextColor(Color.WHITE)
                min +=1
            }else{
                funBut.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                funBut.setTextColor(Color.BLACK)
                min -=1
            }
        }
        binding.horrorButton.setOnClickListener {
            val funBut = binding.horrorButton
            clickH += 1
            if (clickH != 2) {
                funBut.setBackgroundColor(Color.parseColor("#0082DD"))
                funBut.setTextColor(Color.WHITE)
                min +=1
            } else{
                funBut.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                funBut.setTextColor(Color.BLACK)
                min -=1
            }
        }
        binding.continueButton.setOnClickListener {
            if (min1 < 1){
                binding.firstCategoryMin.setTextColor(Color.RED)
                if (min < 2){
                    binding.secondCategoryMin.setTextColor(Color.RED)
                }
            }
            else if (min < 2){
                binding.secondCategoryMin.setTextColor(Color.RED)
            }
            else{
                appViewModel.savePreference(byHandClicked, computerGraphClicked,
                    depressedButtonClicked, funButtonClicked, clickS, clickP, clickL, clickM,
                    clickB, clickI, clickC, clickN, clickA, clickH)

                appViewModel.getData()

                val action = PreferencesSelectionScreenDirections.actionPreferencesSelectionScreenToSearchScreen()
                findNavController().navigate(action)}
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}