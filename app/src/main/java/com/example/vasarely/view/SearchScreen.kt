package com.example.vasarely.view

import android.app.ProgressDialog
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
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

    private fun View.margin(left: Float? = null, top: Float? = null, right: Float? = null, bottom: Float? = null) {
        layoutParams<ViewGroup.MarginLayoutParams> {
            left?.run { leftMargin = dpToPx(this) }
            top?.run { topMargin = dpToPx(this) }
            right?.run { rightMargin = dpToPx(this) }
            bottom?.run { bottomMargin = dpToPx(this) }
        }
    }

    private inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
        if (layoutParams is T) block(layoutParams as T)
    }

    private fun dpToPx(dp: Float): Int = (dp * Resources.getSystem().displayMetrics.density).toInt()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        appViewModel.dataChangeExceptions.observe(viewLifecycleOwner) { exception ->
            Toast.makeText(requireContext(), exception, Toast.LENGTH_LONG).show()
        }

        _binding = SearchScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun showData() {
            // Show data
        }

        val progressDialog = ProgressDialog(requireContext())

        appViewModel.userMutableLiveData.observe(viewLifecycleOwner) { preferencesAreSelected ->

            appViewModel.setUserDBStatus()
            if (progressDialog.isShowing) progressDialog.dismiss()

            if (!preferencesAreSelected) {
                val action = SearchScreenDirections.actionSearchScreenToPreferencesSelectionScreen()
                findNavController().navigate(action)
            }
            else {
                appViewModel.getData()
                appViewModel.recommendationsSearch()
            }
        }

        appViewModel.recommendedPost
            .observe(viewLifecycleOwner) { post ->

                fun rotateImage(source: Bitmap, angle: Float) : Bitmap {
                    val matrix = Matrix()
                    matrix.postRotate(angle)
                    return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
                }


                val screenWidth = Resources.getSystem().displayMetrics.widthPixels

                val postImageView = ImageView(context)

                val params = screenWidth - dpToPx(60F)
                val postLinearLayoutParams = LinearLayout.LayoutParams(params, LinearLayout.LayoutParams.WRAP_CONTENT)

                postImageView.layoutParams = postLinearLayoutParams
                postImageView.scaleType = ImageView.ScaleType.FIT_START

                postImageView.adjustViewBounds = true

                postImageView.margin(30F, 15F, 30F, 15F)

                if (post.byteCount < 50135040)
                    postImageView.setImageBitmap(post)
                else postImageView.setImageBitmap(rotateImage(post, 90f))
                binding.recs.addView(postImageView)
        }


        appViewModel.userData.observe(viewLifecycleOwner) {
            appViewModel.processData(it)
            showData()
        }

        appViewModel.recommendationsToProcess.observe(viewLifecycleOwner) {
            if (appViewModel.isLocalDataInitialized()) {
                appViewModel.findPostsToRecommend(it)
            }
            else Toast.makeText(requireContext(), "Can't show your recommendations now, try later", Toast.LENGTH_SHORT).show()
        }

        if (!appViewModel.isLocalDataInitialized()) {
            if (appViewModel.isUserDBInitialized()) {
                appViewModel.getData()
                appViewModel.recommendationsSearch()
            }
            else {
                progressDialog.setMessage("Зачекайте, триває завантаження...")
                progressDialog.setCancelable(false)
                progressDialog.show()
            }
        }
        else showData()

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