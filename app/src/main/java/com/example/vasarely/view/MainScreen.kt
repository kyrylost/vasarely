package com.example.vasarely.view

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vasarely.R
import com.example.vasarely.databinding.MainScreenBinding

class MainScreen: Fragment(R.layout.main_screen) {

    private var _binding: MainScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels

        var statusBarHeight = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId);
        }

        binding.footerHome.measure(0,0)

        val footerHeight = binding.footerHome.measuredHeight

        val scrollView : ScrollView = binding.scrollView
        val scrollViewParams: ViewGroup.LayoutParams = scrollView.layoutParams

        scrollViewParams.height = screenHeight - footerHeight - statusBarHeight
        scrollViewParams.width = screenWidth
        scrollView.layoutParams = scrollViewParams

    //----------------------------Navigation between screens---------------------------------------
        //From MainScreen to SearchScreen
        binding.searchButton.setOnClickListener {
            val action = MainScreenDirections.actionMainScreenToSearchScreen()
            findNavController().navigate(action)
        }
        //From MainScreen to UserPersonalPageScreen
        binding.userPageButton.setOnClickListener {
            val action = MainScreenDirections.actionMainScreenToUserPersonalPageScreen()
            findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}