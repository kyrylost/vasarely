package com.example.vasarely.view

import android.content.res.Resources
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vasarely.R
import com.example.vasarely.databinding.LikedPostScreenBinding

class LikedPostScreen : Fragment(R.layout.liked_post_screen) {
    private var _binding: LikedPostScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LikedPostScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val montserratBoldFont: Typeface? =
            ResourcesCompat.getFont(requireContext(), R.font.montserrat_bold)
        val montserratRegularFont: Typeface? =
            ResourcesCompat.getFont(requireContext(), R.font.montserrat_regular)

        binding.textView2.typeface = montserratBoldFont

        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels

        var statusBarHeight = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId);
        }

        binding.likedPostCardView.measure(0,0)
        binding.footerUser.measure(0,0)

        val likedPostCardViewHeight = binding.likedPostCardView.measuredHeight
        val footerHeight = binding.footerUser.measuredHeight

        val scrollView : ScrollView = binding.scroll
        val scrollViewParams: ViewGroup.LayoutParams = scrollView.layoutParams

        scrollViewParams.height = screenHeight - footerHeight - likedPostCardViewHeight - statusBarHeight
        scrollViewParams.width = screenWidth
        scrollView.layoutParams = scrollViewParams

        //----------------------------Navigation between screens------------------------------------
        //to SearchScreen
        binding.searchButton.setOnClickListener {
            val action = LikedPostScreenDirections.actionLikedPostScreenToSearchScreen()
            findNavController().navigate(action)
        }
        //to MainScreen
        binding.homeButton.setOnClickListener {
            val action = LikedPostScreenDirections.actionLikedPostScreenToMainScreen()
            findNavController().navigate(action)
        }
        //to UserPersonalPage
        binding.returnButton.setOnClickListener {
            val action = LikedPostScreenDirections.actionLikedPostScreenToUserPersonalPageScreen()
            findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}