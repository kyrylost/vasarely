package com.example.vasarely.view

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vasarely.R
import com.example.vasarely.databinding.NewPreferencesScreenBinding
import com.example.vasarely.databinding.UserPersonalPageScreenBinding
import com.example.vasarely.viewmodel.AppViewModel

class NewPreferencesScreen : Fragment(R.layout.new_preferences_screen) {

    private val appViewModel: AppViewModel by activityViewModels()
    private var _binding: NewPreferencesScreenBinding? = null
    private val binding get() = _binding!!

    private var min = 0;
    private var min1 = 0;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NewPreferencesScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val montserratBoldFont : Typeface? = ResourcesCompat.getFont(requireContext(), R.font.montserrat_bold)
        val montserratRegularFont : Typeface? = ResourcesCompat.getFont(requireContext(), R.font.montserrat_regular)

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
        binding.continueButton2.typeface = montserratRegularFont

        val workPerformances = mutableMapOf<Button, Int>(
            binding.byHandButton to 0,
            binding.compGraphButton to 0
        )
        val favouriteGenres = mutableMapOf<Button, Int>(
            binding.stillLifeButton to 0,
            binding.portraitButton to 0,
            binding.landscapeButton to 0,
            binding.marineButton to 0,
            binding.battlePaintingButton to 0,
            binding.interiorButton to 0,
            binding.caricatureButton to 0,
            binding.nudeButton to 0,
            binding.animeButton to 0,
            binding.horrorButton to 0
        )
        val pictureMoods = mutableMapOf<Button, Int>(
            binding.depressedButton to 0,
            binding.funButton to 0
        )

        binding.homeButton.setOnClickListener {
            val action = NewPreferencesScreenDirections.actionNewPreferencesScreenToMainScreen()
            findNavController().navigate(action)
        }

        binding.searchButton.setOnClickListener {
            val action = NewPreferencesScreenDirections.actionNewPreferencesScreenToSearchScreen()
            findNavController().navigate(action)
        }

        binding.returnButton.setOnClickListener {
            val action = NewPreferencesScreenDirections.actionNewPreferencesScreenToUserPersonalPageScreen()
            findNavController().navigate(action)
        }

        fun buttonSelect(_button: Button) {
            _button.setBackgroundColor(Color.parseColor("#0082DD"));
            _button.setTextColor(Color.WHITE)
        }

        fun buttonUnselect(_button: Button) {
            _button.setBackgroundColor(Color.parseColor("#FFFFFF"))
            _button.setTextColor(Color.BLACK)
        }

        fun onWorkPerformanceClicked(_button: Button) {
            var value = workPerformances[_button] ?: 0
            value += 1

            if (value > 2)
                value = 1

            if (value != 2) {
                buttonSelect(_button)
                min1 += 1
            } else {
                buttonUnselect(_button)
                min1 -= 1
            }

            workPerformances[_button] = value
        }

        fun onFavouriteGenresClicked(_button: Button) {
            var value = favouriteGenres[_button] ?: 0
            value += 1

            if (value > 2)
                value = 1

            if (value != 2) {
                buttonSelect(_button)
                min += 1
            } else {
                buttonUnselect(_button)
                min -= 1
            }

            favouriteGenres[_button] = value;
        }

        fun onPictureMoodsClicked(_button: Button) {
            var value = pictureMoods[_button] ?: 0
            value += 1;

            if (value > 2)
                value = 1

            if (value != 2) {
                buttonSelect(_button)
            } else {
                buttonUnselect(_button)
            }

            pictureMoods[_button] = value
        }

        binding.byHandButton.setOnClickListener {
            onWorkPerformanceClicked(binding.byHandButton)
        }

        binding.compGraphButton.setOnClickListener {
            onWorkPerformanceClicked(binding.compGraphButton)
        }

        binding.stillLifeButton.setOnClickListener {
            onFavouriteGenresClicked(binding.stillLifeButton)
        }

        binding.portraitButton.setOnClickListener {
            onFavouriteGenresClicked(binding.portraitButton)
        }

        binding.landscapeButton.setOnClickListener {
            onFavouriteGenresClicked(binding.landscapeButton)
        }

        binding.marineButton.setOnClickListener {
            onFavouriteGenresClicked(binding.marineButton)
        }

        binding.battlePaintingButton.setOnClickListener {
            onFavouriteGenresClicked(binding.battlePaintingButton)
        }

        binding.interiorButton.setOnClickListener {
            onFavouriteGenresClicked(binding.interiorButton)
        }

        binding.caricatureButton.setOnClickListener {
            onFavouriteGenresClicked(binding.caricatureButton)
        }

        binding.nudeButton.setOnClickListener {
            onFavouriteGenresClicked(binding.nudeButton)
        }

        binding.animeButton.setOnClickListener {
            onFavouriteGenresClicked(binding.animeButton)
        }

        binding.horrorButton.setOnClickListener {
            onFavouriteGenresClicked(binding.horrorButton)
        }

        binding.funButton.setOnClickListener {
            onPictureMoodsClicked(binding.funButton)
        }

        binding.depressedButton.setOnClickListener {
            onPictureMoodsClicked(binding.depressedButton)
        }

        binding.continueButton2.setOnClickListener {
            if (min < 1) {
                binding.firstCategoryMin.setTextColor(Color.RED)
                if (min1 < 2) {
                    binding.secondCategoryMin.setTextColor(Color.RED)
                }
            }
            else if (min < 2) {
                binding.secondCategoryMin.setTextColor(Color.RED)
            }
            else {
                appViewModel.savePreference(workPerformances[binding.byHandButton] ?: 0,
                    workPerformances[binding.compGraphButton] ?: 0,
                    pictureMoods[binding.depressedButton] ?: 0,
                    pictureMoods[binding.funButton] ?: 0,
                    favouriteGenres[binding.stillLifeButton] ?: 0,
                    favouriteGenres[binding.portraitButton] ?: 0,
                    favouriteGenres[binding.landscapeButton] ?: 0,
                    favouriteGenres[binding.marineButton] ?: 0,
                    favouriteGenres[binding.battlePaintingButton] ?: 0,
                    favouriteGenres[binding.interiorButton] ?: 0,
                    favouriteGenres[binding.caricatureButton] ?: 0,
                    favouriteGenres[binding.nudeButton] ?: 0,
                    favouriteGenres[binding.animeButton] ?: 0,
                    favouriteGenres[binding.horrorButton] ?: 0)

                val action = NewPreferencesScreenDirections.actionNewPreferencesScreenToUserPersonalPageScreen()
                findNavController().navigate(action)
            }
        }
    }

    /*private fun getValue(_map : MutableMap<Button, Int>, key : Button) : Int
    {
        return _map[key] ?: 0;
    }*/

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}