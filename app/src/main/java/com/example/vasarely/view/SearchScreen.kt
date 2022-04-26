package com.example.vasarely.view

import android.content.res.Resources
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vasarely.R
import com.example.vasarely.databinding.SearchScreenBinding
import com.example.vasarely.viewmodel.AppViewModel

class SearchScreen : Fragment(R.layout.search_screen) {

    private val appViewModel: AppViewModel by activityViewModels()
    private var _binding: SearchScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        appViewModel.userMutableLiveData.observe(viewLifecycleOwner) {
            val action = SearchScreenDirections.actionSearchScreenToPreferencesSelectionScreen()
            findNavController().navigate(action)
        }

        _binding = SearchScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val montserratBoldFont : Typeface? = ResourcesCompat.getFont(requireContext(), R.font.montserrat_bold)
        val montserratRegularFont : Typeface? = ResourcesCompat.getFont(requireContext(), R.font.montserrat_regular)

        binding.search.typeface = montserratRegularFont

        binding.searchInputLayout.measure(0,0)
        val searchInputLayoutHeight = binding.searchInputLayout.measuredHeight

        val logo: ImageView = binding.logo
        val logoParams: ViewGroup.LayoutParams = logo.layoutParams
        logoParams.width = searchInputLayoutHeight
        logoParams.height = searchInputLayoutHeight
        logo.layoutParams = logoParams

        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels

        var statusBarHeight = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId);
        }

        binding.inputCardView.measure(0,0)
        binding.footerSearch.measure(0,0)

        val inputCardViewHeight = binding.inputCardView.measuredHeight
        val footerHeight = binding.footerSearch.measuredHeight

        val scrollView : ScrollView = binding.scroll
        val scrollViewParams: ViewGroup.LayoutParams = scrollView.layoutParams

        scrollViewParams.height = screenHeight - footerHeight - inputCardViewHeight - statusBarHeight
        scrollViewParams.width = screenWidth
        scrollView.layoutParams = scrollViewParams

        //----------------------------Navigation between screens---------------------------------------
        binding.homeButton.setOnClickListener {
                val action = SearchScreenDirections.actionSearchScreenToMainScreen()
                findNavController().navigate(action)
            }

        binding.userPageButton.setOnClickListener {
            val action = SearchScreenDirections.actionSearchScreenToUserPersonalPageScreen()
            findNavController().navigate(action)
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}