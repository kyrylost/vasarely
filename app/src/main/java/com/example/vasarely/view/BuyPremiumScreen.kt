package com.example.vasarely.view

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vasarely.R
import com.example.vasarely.databinding.BuyPremiumScreenBinding
import com.example.vasarely.viewmodel.secondary.BuyPremiumViewModel
import com.google.android.material.textfield.TextInputEditText

class BuyPremiumScreen: Fragment(R.layout.buy_premium_screen) {

    private val buyPremiumViewModel: BuyPremiumViewModel by activityViewModels()
    private var _binding: BuyPremiumScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = BuyPremiumScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //----------------------------Navigation between screens----------------------------------//

        binding.searchButton.setOnClickListener {
            val action = BuyPremiumScreenDirections
                .actionBuyPremiumScreenToSearchScreen()
            findNavController().navigate(action)
        }
        binding.homeButton.setOnClickListener {
            val action = BuyPremiumScreenDirections
                .actionBuyPremiumScreenToMainScreen()
            findNavController().navigate(action)
        }
        binding.userPageButton.setOnClickListener {
            val action = BuyPremiumScreenDirections
                .actionBuyPremiumScreenToUserPersonalPageScreen()
            findNavController().navigate(action)
        }
        binding.buyPremiumReturnButton.setOnClickListener {
            val action = BuyPremiumScreenDirections
                .actionBuyPremiumScreenToUserPersonalPageScreen()
            findNavController().navigate(action)
        }


        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Отримуємо дані про поточний курс")
        progressDialog.setCancelable(false)
        progressDialog.show()

        buyPremiumViewModel.getCourse()

        buyPremiumViewModel.costsAreCalculatedMutableLiveData.observe(viewLifecycleOwner) {
            progressDialog.dismiss()
        }


        binding.uah.setOnClickListener {
            buyPremiumViewModel.uahClicked()
        }
        binding.usd.setOnClickListener {
            buyPremiumViewModel.usdClicked()
        }
        binding.eur.setOnClickListener {
            buyPremiumViewModel.eurClicked()
        }
        binding.btc.setOnClickListener {
            buyPremiumViewModel.btcClicked()
        }

        buyPremiumViewModel.uahTextColor.observe(viewLifecycleOwner) { textColor ->
            binding.uah.setTextColor(textColor)
        }
        buyPremiumViewModel.usdTextColor.observe(viewLifecycleOwner) { textColor ->
            binding.usd.setTextColor(textColor)
        }
        buyPremiumViewModel.eurTextColor.observe(viewLifecycleOwner) { textColor ->
            binding.eur.setTextColor(textColor)
        }
        buyPremiumViewModel.btcTextColor.observe(viewLifecycleOwner) { textColor ->
            binding.btc.setTextColor(textColor)
        }

        buyPremiumViewModel.displayedCost.observe(viewLifecycleOwner) { premiumCost ->
            binding.price.text = premiumCost.toString()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}