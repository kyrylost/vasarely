package com.example.vasarely.view

import android.app.AlertDialog
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vasarely.R
import com.example.vasarely.databinding.UserPageScreenBinding
import com.example.vasarely.viewmodel.AppViewModel

class UserPageScreen: Fragment(R.layout.user_page_screen) {

    private val appViewModel: AppViewModel by activityViewModels()
    private var _binding: UserPageScreenBinding? = null
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
        _binding = UserPageScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val screenWidth = Resources.getSystem().displayMetrics.widthPixels

        val montserratBoldFont: Typeface? =
            ResourcesCompat.getFont(requireContext(), R.font.montserrat_bold)
        val montserratRegularFont: Typeface? =
            ResourcesCompat.getFont(requireContext(), R.font.montserrat_regular)

        binding.username.typeface = montserratBoldFont
        binding.followers.typeface = montserratBoldFont
        binding.followersNumber.typeface = montserratRegularFont
        binding.following.typeface = montserratBoldFont
        binding.followingNumber.typeface = montserratRegularFont
        binding.subscribe.typeface = montserratBoldFont

        binding.username.text = appViewModel.foundedUserData.username
        binding.followersNumber.text = appViewModel.foundedUserData.getFollowers()
        binding.followingNumber.text = appViewModel.foundedUserData.getFollowing()


        fun showPosts() {

            binding.userPostsLinearLayout.removeAllViews()
            val lines = appViewModel.foundedUserLines
            val lastLinePosts = appViewModel.foundedUserLastLinePosts


            val imagesBitmaps = appViewModel.foundedUserData.allFoundedUserPostsData
            var currentPost = 0

            for (line in 1..lines.toInt()) {
                val horizontalLinearLayout = LinearLayout(context)
                horizontalLinearLayout.orientation = LinearLayout.HORIZONTAL
                for (postNumber in 1..3) {
                    val postImageView = ImageView(context)

                    val params = screenWidth / 3 - dpToPx(80F / 3)
                    val postLinearLayoutParams = LinearLayout.LayoutParams(params, params)

                    postImageView.layoutParams = postLinearLayoutParams
                    postImageView.scaleType = ImageView.ScaleType.CENTER_CROP

                    when (postNumber) {
                        1 -> postImageView.margin(30F, 10F, 0F, 0F)
                        2 -> postImageView.margin(10F, 10F, 0F, 0F)
                        3 -> {
                            if (line == lines.toInt() && lastLinePosts == 0)
                                postImageView.margin(10F, 10F, 30F, 88F)
                            else postImageView.margin(10F, 10F, 30F, 0F)
                        }
                    }


                    val newCurrentPost = currentPost
                    postImageView.setOnClickListener {
                        val dialogBuilder = AlertDialog.Builder(context)
                        val popupView = layoutInflater.inflate(R.layout.post_popup, null)

                        dialogBuilder.setView(popupView)
                        val addNoteDialog = dialogBuilder.create()
                        addNoteDialog.show()

                        val postImg = popupView.findViewById<ImageView>(R.id.Post)
                        postImg.setImageBitmap(imagesBitmaps[newCurrentPost])
                    }

                    postImageView.setImageBitmap(imagesBitmaps[currentPost])

                    currentPost += 1
                    horizontalLinearLayout.addView(postImageView)
                }
                binding.userPostsLinearLayout.addView(horizontalLinearLayout)
            }

            if (lastLinePosts != 0){
                val horizontalLinearLayout = LinearLayout(context)
                horizontalLinearLayout.orientation = LinearLayout.HORIZONTAL
                for (postNumber in 1..lastLinePosts) {
                    val postImageView = ImageView(context)

                    val params = screenWidth / 3 - dpToPx(80F / 3)
                    val postLinearLayoutParams = LinearLayout.LayoutParams(params, params)

                    postImageView.layoutParams = postLinearLayoutParams
                    postImageView.scaleType = ImageView.ScaleType.CENTER_CROP

                    when (postNumber) {
                        1 -> {
                            if (postNumber == lastLinePosts)
                                postImageView.margin(30F, 10F, 0F, 88F)
                            else postImageView.margin(30F, 10F, 0F, 0F)

                        }
                        2 -> postImageView.margin(10F, 10F, 0F, 88F)
                    }


                    val newCurrentPost = currentPost
                    postImageView.setOnClickListener {
                        val dialogBuilder = AlertDialog.Builder(context)
                        val popupView = layoutInflater.inflate(R.layout.post_popup, null)

                        dialogBuilder.setView(popupView)
                        val addNoteDialog = dialogBuilder.create()
                        addNoteDialog.show()

                        val postImg = popupView.findViewById<ImageView>(R.id.Post)
                        postImg.setImageBitmap(imagesBitmaps[newCurrentPost])
                    }

                    postImageView.setImageBitmap(imagesBitmaps[newCurrentPost])

                    currentPost += 1
                    horizontalLinearLayout.addView(postImageView)
                }
                binding.userPostsLinearLayout.addView(horizontalLinearLayout)

            }
        }


        //to SearchScreen
        binding.searchButton.setOnClickListener {
            val action = UserPageScreenDirections.actionUserPageScreenToSearchScreen()
            findNavController().navigate(action)
        }
        //to MainScreen
        binding.homeButton.setOnClickListener {
            val action = UserPageScreenDirections.actionUserPageScreenToMainScreen()
            findNavController().navigate(action)
        }
        //to UserPersonalScreen
        binding.userPageButton.setOnClickListener {
            val action = UserPageScreenDirections.actionUserPageScreenToUserPersonalPageScreen()
            findNavController().navigate(action)
        }

        if (appViewModel.foundedUserData.worksAmount != 0) {
            val progressBar = ProgressBar(requireContext())
            progressBar.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)

            progressBar.indeterminateDrawable.setColorFilter(
                ContextCompat.getColor(requireContext(),
                R.color.accent), PorterDuff.Mode.MULTIPLY)

            progressBar.margin(0F, 30F, 0F, 30F)
            binding.userPostsLinearLayout.addView(progressBar)
        }

        appViewModel.getOtherUserPosts()

        appViewModel.foundedUserPosts.observe(viewLifecycleOwner) {
            appViewModel.processFoundedUserPhotos(it as MutableList<Bitmap>)
        }

        appViewModel.foundedUserPostProcessed.observe(viewLifecycleOwner) {
            showPosts()
        }

        appViewModel.foundedUserDataChanged.observe(viewLifecycleOwner) {
            binding.followersNumber.text = appViewModel.foundedUserData.getFollowers()

        }

        binding.subscribe.setOnClickListener {
            //binding.followersNumber.text = (appViewModel.foundedUserData[3].toInt() + 1).toString()
            appViewModel.follow()
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}