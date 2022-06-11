package com.example.vasarely.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vasarely.R
import com.example.vasarely.databinding.UserPersonalPageScreenBinding
import com.example.vasarely.viewmodel.AppViewModel
import com.google.android.material.textfield.TextInputEditText
import java.io.IOException


class UserPersonalPageScreen: Fragment(R.layout.user_personal_page_screen) {

    private val appViewModel: AppViewModel by activityViewModels()
    private var _binding: UserPersonalPageScreenBinding? = null
    private val binding get() = _binding!!
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var addWorkPopupImage: ImageView? = null
    private var profilePictureImage: ImageView? = null
    private var addingNewPhoto = true

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

    //private fun pxToDp(px: Float): Int = (px / Resources.getSystem().displayMetrics.density).toInt()
    private fun dpToPx(dp: Float): Int = (dp * Resources.getSystem().displayMetrics.density).toInt()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        appViewModel.userData.observe(viewLifecycleOwner) {
            appViewModel.processData(it)
        }

        appViewModel.dataChangeExceptions.observe(viewLifecycleOwner) { exception ->
            Toast.makeText(requireContext(), exception, Toast.LENGTH_LONG).show()
        }

        _binding = UserPersonalPageScreenBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.username.text = appViewModel.localData.username
        if (appViewModel.isProfilePictureInitialized())
            binding.avatarPlacer.setImageBitmap(appViewModel.localData.profilePicture)

        val screenWidth = Resources.getSystem().displayMetrics.widthPixels

        val montserratBoldFont: Typeface? =
            ResourcesCompat.getFont(requireContext(), R.font.montserrat_bold)
        val montserratRegularFont: Typeface? =
            ResourcesCompat.getFont(requireContext(), R.font.montserrat_regular)

        binding.username.typeface = montserratBoldFont
        binding.subs.typeface = montserratBoldFont
        binding.subsNumber.typeface = montserratRegularFont
        binding.follow.typeface = montserratBoldFont
        binding.followNumber.typeface = montserratRegularFont
        binding.addWork.typeface = montserratBoldFont


        fun showPosts() {
            binding.postsLinearLayout.removeAllViews()
            val lines = appViewModel.lines
            val lastLinePosts = appViewModel.lastLinePosts

            val imagesBitmaps = appViewModel.profileData.allUserPostsData
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

                    Log.d("bytes",imagesBitmaps[currentPost].byteCount.toString())

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
                binding.postsLinearLayout.addView(horizontalLinearLayout)
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

                    Log.d("bytesV",imagesBitmaps[currentPost].byteCount.toString())

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
                binding.postsLinearLayout.addView(horizontalLinearLayout)

            }
        }

        if (appViewModel.isProfileDataInitialized()) showPosts()

        appViewModel.allUserPosts.observe(viewLifecycleOwner) {
            Log.d("observed", "asd")
            appViewModel.saveImagesToLocalDB(it as MutableList)

            val progressBar = ProgressBar(requireContext())
            progressBar.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)

            progressBar.indeterminateDrawable.setColorFilter(ContextCompat.getColor(requireContext(),
                R.color.accent), PorterDuff.Mode.MULTIPLY)

            progressBar.margin(0F, 30F, 0F, 30F)

            binding.postsLinearLayout.addView(progressBar)
        }

        appViewModel.postsProcessed.observe(viewLifecycleOwner) {
            showPosts()
        }

        //----------------------------Navigation between screens------------------------------------
        //to SearchScreen
        binding.searchButton.setOnClickListener {
            val action =
                UserPersonalPageScreenDirections.actionUserPersonalPageScreenToSearchScreen()
            findNavController().navigate(action)
        }
        //to MainScreen
        binding.homeButton.setOnClickListener {
            val action = UserPersonalPageScreenDirections.actionUserPersonalPageScreenToMainScreen()
            findNavController().navigate(action)
        }

        binding.menuImgBtn.setOnClickListener {
            val action =
                UserPersonalPageScreenDirections.actionUserPersonalPageScreenToNewPreferencesScreen()
            findNavController().navigate(action)
        }


        binding.avatarPlacer.setOnClickListener {

            val popupSetProfilePicture = layoutInflater.inflate(R.layout.add_profile_picture_popup, null)

            profilePictureImage = popupSetProfilePicture.findViewById(R.id.addProfilePictureImage)
            val profilePictureButton = popupSetProfilePicture.findViewById<Button>(R.id.addProfilePictureButton)

            addingNewPhoto = false
            launchGallery()

            val dialogBuilderAddProfilePic = AlertDialog.Builder(context)
            dialogBuilderAddProfilePic.setView(popupSetProfilePicture)

            val addProfilePicDialog = dialogBuilderAddProfilePic.create()
            addProfilePicDialog.show()

            profilePictureButton.setOnClickListener {
                addProfilePicDialog.dismiss()

                appViewModel.saveProfilePicture()

                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, filePath!!))
                else
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, filePath)
                appViewModel.saveProfilePictureToLocalDB(bitmap)

                binding.avatarPlacer.setImageBitmap(appViewModel.localData.profilePicture)
            }
        }


        binding.gridImgBtn.setOnClickListener {
            binding.gridSelected.setBackgroundColor(resources.getColor(R.color.accent))
            binding.columnSelected.setBackgroundColor(resources.getColor(R.color.white))
        }

        binding.columnImgBtn.setOnClickListener {
            binding.gridSelected.setBackgroundColor(resources.getColor(R.color.white))
            binding.columnSelected.setBackgroundColor(resources.getColor(R.color.accent))
        }

        binding.menuImgBtn.setOnClickListener {

            val popupView = layoutInflater.inflate(R.layout.menu_user_personal_page_screen, null)

            val likedPostButton = popupView.findViewById<ImageButton>(R.id.likedPostIcon)
            val likedPostText = popupView.findViewById<TextView>(R.id.likedPostText)
            likedPostText.typeface = montserratBoldFont

            val newPrefButton = popupView.findViewById<ImageButton>(R.id.newPrefButton)
            val newPrefText = popupView.findViewById<TextView>(R.id.new_pref_text)
            newPrefText.typeface = montserratBoldFont

            val changeNicknameTextView =
                popupView.findViewById<TextView>(R.id.settings_nickname_text)
            changeNicknameTextView.typeface = montserratBoldFont

            val changePasswordTextView = popupView.findViewById<TextView>(R.id.key_text)
            changePasswordTextView.typeface = montserratBoldFont

            val logoutButton = popupView.findViewById<ImageButton>(R.id.logoutButton)
            val logoutText = popupView.findViewById<TextView>(R.id.logout_text)
            logoutText.typeface = montserratBoldFont

            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setView(popupView)

            val settingsDialog = dialogBuilder.create()
            settingsDialog.show()

            val closeSettingsButton = popupView.findViewById<ImageButton>(R.id.closeButtonSettingsPopup)
            closeSettingsButton.setOnClickListener{
                settingsDialog.dismiss()
            }

            logoutButton.setOnClickListener {
                appViewModel.logout()

                val sharedPref =
                    activity?.getSharedPreferences("userLoginData", Context.MODE_PRIVATE)
                        ?: return@setOnClickListener
                val editor = sharedPref.edit()
                editor.putString("remember", "false")
                editor.apply()

                settingsDialog.dismiss()

                val action =
                    UserPersonalPageScreenDirections.actionUserPersonalPageScreenToSignInSignUpScreen()
                findNavController().navigate(action)
            }

            logoutText.setOnClickListener {
                appViewModel.logout()

                val sharedPref =
                    activity?.getSharedPreferences("userLoginData", Context.MODE_PRIVATE)
                        ?: return@setOnClickListener
                val editor = sharedPref.edit()
                editor.putString("remember", "false")
                editor.apply()

                settingsDialog.dismiss()

                val action =
                    UserPersonalPageScreenDirections.actionUserPersonalPageScreenToSignInSignUpScreen()
                findNavController().navigate(action)
            }

            likedPostButton.setOnClickListener {
                val action =
                    UserPersonalPageScreenDirections.actionUserPersonalPageScreenToLikedPostScreen()
                findNavController().navigate(action)

                settingsDialog.dismiss()
            }

            likedPostText.setOnClickListener {
                val action =
                    UserPersonalPageScreenDirections.actionUserPersonalPageScreenToLikedPostScreen()
                findNavController().navigate(action)

                settingsDialog.dismiss()
            }

            newPrefButton.setOnClickListener {
                val action =
                    UserPersonalPageScreenDirections.actionUserPersonalPageScreenToNewPreferencesScreen()
                findNavController().navigate(action)

                settingsDialog.dismiss()
            }

            newPrefText.setOnClickListener {
                val action =
                    UserPersonalPageScreenDirections.actionUserPersonalPageScreenToNewPreferencesScreen()
                findNavController().navigate(action)

                settingsDialog.dismiss()
            }

            changePasswordTextView.setOnClickListener {
                val passwordPopup = layoutInflater.inflate(R.layout.change_password_popup, null)
                val dialogBuilderPassword = AlertDialog.Builder(context)
                dialogBuilderPassword.setView(passwordPopup)

                val changePasswordText =
                    passwordPopup.findViewById<TextView>(R.id.changePasswordPopupTextView)
                val passwordInputPopup =
                    passwordPopup.findViewById<TextInputEditText>(R.id.password_input)
                val savePasswordButton = passwordPopup.findViewById<Button>(R.id.Next1)
                changePasswordText.typeface = montserratBoldFont
                passwordInputPopup.typeface = montserratRegularFont
                savePasswordButton.typeface = montserratBoldFont

                val changePasswordDialog = dialogBuilderPassword.create()
                changePasswordDialog.show()

                val closePasswordButton = passwordPopup.findViewById<ImageButton>(R.id.closeButtonPasswordPopup)
                closePasswordButton.setOnClickListener{
                    changePasswordDialog.dismiss()
                }
            }

            changeNicknameTextView.setOnClickListener {

                val nicknamePopup = layoutInflater.inflate(R.layout.nickname_change_popup, null)

                val changeNicknameText =
                    nicknamePopup.findViewById<TextView>(R.id.changeNicknamePopupTextView)
                val btnSaveNickname = nicknamePopup.findViewById<Button>(R.id.Next1)
                val changeNick = nicknamePopup.findViewById<TextInputEditText>(R.id.username_input)
                changeNicknameText.typeface = montserratBoldFont
                btnSaveNickname.typeface = montserratBoldFont
                changeNick.typeface = montserratRegularFont

                val dialogBuilderNickname = AlertDialog.Builder(context)
                dialogBuilderNickname.setView(nicknamePopup)

                val changeNicknameDialog = dialogBuilderNickname.create()
                changeNicknameDialog.show()

                val closeNicknameButton = nicknamePopup.findViewById<ImageButton>(R.id.closeButtonNicknamePopup)
                closeNicknameButton.setOnClickListener{
                    changeNicknameDialog.dismiss()
                }

                btnSaveNickname.setOnClickListener {
                    val newNickname = changeNick.text.toString()
                    if (newNickname.isNotEmpty()) {
                        appViewModel.updateName(newNickname)
                        changeNicknameDialog.dismiss()
                    } else if (newNickname.isEmpty()) {
                        changeNick.error = getString(R.string.empty_username_error_message)
                    }
                }
            }

        }

        //------------------------------------Adding Work-----------------------------------------//
        binding.addWork.setOnClickListener {

            val popupAddWork = layoutInflater.inflate(R.layout.add_new_photo_popup, null)

            addWorkPopupImage = popupAddWork.findViewById(R.id.AddWorkImage)

            var min1 = 0
            var min2 = 0
            var min3 = 0

            addingNewPhoto = true
            launchGallery()

            val dialogBuilderAddWork = AlertDialog.Builder(context)
            dialogBuilderAddWork.setView(popupAddWork)

            val addWorkDialog = dialogBuilderAddWork.create()
            addWorkDialog.show()

            val addNewPhotoNextButton = popupAddWork.findViewById<Button>(R.id.addNewPhotoNextButton)

            addNewPhotoNextButton.setOnClickListener {

                val description = popupAddWork.findViewById<TextInputEditText>(R.id.username_input)
                    .text.toString()

                val popupFirstCategory = layoutInflater.inflate(R.layout.first_category_popup, null)
                val nextFirstCategory = popupFirstCategory.findViewById<Button>(R.id.Next1)
                val byHand = popupFirstCategory.findViewById<Button>(R.id.by_hand_button)
                val compGraph = popupFirstCategory.findViewById<Button>(R.id.comp_graph_button)
                val textMinFirstCategory = popupFirstCategory.findViewById<TextView>(R.id.first_category_min)

                var byHandClicked = 0
                var computerGraphClicked = 0

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

                var depressedButtonClicked = 0
                var funButtonClicked = 0

                addWorkDialog.dismiss()

                val dialogBuilderFirstCategory = AlertDialog.Builder(context)
                dialogBuilderFirstCategory.setView(popupFirstCategory)

                val firstCategoryDialog = dialogBuilderFirstCategory.create()
                firstCategoryDialog.setCancelable(false)
                firstCategoryDialog.show()

                byHand.setOnClickListener {

                    byHandClicked += 1
                    if (byHandClicked > 2) byHandClicked = 1
                    if (byHandClicked != 2) {
                        byHand.setBackgroundColor(Color.parseColor("#0082DD"))
                        byHand.setTextColor(Color.WHITE)
                        min1 += 1
                    } else {
                        byHand.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                        byHand.setTextColor(Color.BLACK)
                        min1 -= 1
                    }
                }

                compGraph.setOnClickListener {

                    computerGraphClicked += 1
                    if (computerGraphClicked > 2) computerGraphClicked = 1
                    if (computerGraphClicked != 2) {
                        compGraph.setBackgroundColor(Color.parseColor("#0082DD"))
                        compGraph.setTextColor(Color.WHITE)
                        min1 += 1

                    } else {
                        compGraph.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                        compGraph.setTextColor(Color.BLACK)
                        min1 -= 1
                    }
                }

                nextFirstCategory.setOnClickListener {
                    if (min1 != 1) {
                        textMinFirstCategory.setTextColor(Color.RED)
                    } else {

                        val popupSecondCategory = layoutInflater.inflate(R.layout.second_category_popup, null)
                        val nextSecondCategory = popupSecondCategory.findViewById<Button>(R.id.Next2)
                        val textMinSecondCategory = popupSecondCategory.findViewById<TextView>(R.id.second_category_min)

                        val portraitButton = popupSecondCategory.findViewById<Button>(R.id.portrait_button)
                        val landscapeButton = popupSecondCategory.findViewById<Button>(R.id.landscape_button)
                        val marineButton = popupSecondCategory.findViewById<Button>(R.id.marine_button)
                        val battlePaintingButton = popupSecondCategory.findViewById<Button>(R.id.battle_painting_button)
                        val interiorButton = popupSecondCategory.findViewById<Button>(R.id.interior_button)
                        val caricatureButton = popupSecondCategory.findViewById<Button>(R.id.caricature_button)
                        val nudeButton = popupSecondCategory.findViewById<Button>(R.id.nude_button)
                        val animeButton = popupSecondCategory.findViewById<Button>(R.id.anime_button)
                        val horrorButton = popupSecondCategory.findViewById<Button>(R.id.horror_button)
                        val stillLifeButton = popupSecondCategory.findViewById<Button>(R.id.still_life_button)

                        firstCategoryDialog.dismiss()

                        val dialogBuilderSecondCategory = AlertDialog.Builder(context)
                        dialogBuilderSecondCategory.setView(popupSecondCategory)

                        val secondCategoryDialog = dialogBuilderSecondCategory.create()
                        secondCategoryDialog.setCancelable(false)
                        secondCategoryDialog.show()

                        stillLifeButton.setOnClickListener {
                            clickS += 1
                            if (clickS > 2) clickS = 1
                            if (clickS != 2) {
                                stillLifeButton.setBackgroundColor(Color.parseColor("#0082DD"))
                                stillLifeButton.setTextColor(Color.WHITE)
                                min2 += 1
                            } else {
                                stillLifeButton.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                                stillLifeButton.setTextColor(Color.BLACK)
                                min2 -= 1
                            }
                        }

                        portraitButton.setOnClickListener {
                            clickP += 1
                            if (clickP > 2) clickP = 1
                            if (clickP != 2) {
                                portraitButton.setBackgroundColor(Color.parseColor("#0082DD"))
                                portraitButton.setTextColor(Color.WHITE)
                                min2 += 1
                            } else {
                                portraitButton.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                                portraitButton.setTextColor(Color.BLACK)
                                min2 -= 1
                            }
                        }

                        landscapeButton.setOnClickListener {
                            clickL += 1
                            if (clickL > 2) clickL = 1
                            if (clickL != 2) {
                                landscapeButton.setBackgroundColor(Color.parseColor("#0082DD"))
                                landscapeButton.setTextColor(Color.WHITE)
                                min2 += 1
                            } else {
                                landscapeButton.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                                landscapeButton.setTextColor(Color.BLACK)
                                min2 -= 1
                            }
                        }

                        marineButton.setOnClickListener {
                            clickM += 1
                            if (clickM > 2) clickM = 1
                            if (clickM != 2) {
                                marineButton.setBackgroundColor(Color.parseColor("#0082DD"))
                                marineButton.setTextColor(Color.WHITE)
                                min2 += 1
                            } else {
                                marineButton.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                                marineButton.setTextColor(Color.BLACK)
                                min2 -= 1
                            }
                        }

                        battlePaintingButton.setOnClickListener {
                            clickB += 1
                            if (clickB > 2) clickB = 1
                            if (clickB != 2) {
                                battlePaintingButton.setBackgroundColor(Color.parseColor("#0082DD"))
                                battlePaintingButton.setTextColor(Color.WHITE)
                                min2 += 1
                            } else {
                                battlePaintingButton.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                                battlePaintingButton.setTextColor(Color.BLACK)
                                min2 -= 1
                            }
                        }

                        interiorButton.setOnClickListener {
                            clickI += 1
                            if (clickI > 2) clickI = 1
                            if (clickI != 2) {
                                interiorButton.setBackgroundColor(Color.parseColor("#0082DD"))
                                interiorButton.setTextColor(Color.WHITE)
                                min2 += 1
                            } else {
                                interiorButton.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                                interiorButton.setTextColor(Color.BLACK)
                                min2 -= 1
                            }
                        }

                        caricatureButton.setOnClickListener {
                            clickC += 1
                            if (clickC > 2) clickC = 1
                            if (clickC != 2) {
                                caricatureButton.setBackgroundColor(Color.parseColor("#0082DD"))
                                caricatureButton.setTextColor(Color.WHITE)
                                min2 += 1
                            } else {
                                caricatureButton.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                                caricatureButton.setTextColor(Color.BLACK)
                                min2 -= 1
                            }
                        }

                        nudeButton.setOnClickListener {
                            clickN += 1
                            if (clickN > 2) clickN = 1
                            if (clickN != 2) {
                                nudeButton.setBackgroundColor(Color.parseColor("#0082DD"))
                                nudeButton.setTextColor(Color.WHITE)
                                min2 += 1
                            } else {
                                nudeButton.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                                nudeButton.setTextColor(Color.BLACK)
                                min2 -= 1
                            }
                        }

                        animeButton.setOnClickListener {
                            clickA += 1
                            if (clickA > 2) clickA = 1
                            if (clickA != 2) {
                                animeButton.setBackgroundColor(Color.parseColor("#0082DD"))
                                animeButton.setTextColor(Color.WHITE)
                                min2 += 1
                            } else {
                                animeButton.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                                animeButton.setTextColor(Color.BLACK)
                                min2 -= 1
                            }
                        }

                        horrorButton.setOnClickListener {
                            clickH += 1
                            if (clickH != 2) {
                                horrorButton.setBackgroundColor(Color.parseColor("#0082DD"))
                                horrorButton.setTextColor(Color.WHITE)
                                min2 += 1
                            } else {
                                horrorButton.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                                horrorButton.setTextColor(Color.BLACK)
                                min2 -= 1
                            }
                        }

                        nextSecondCategory.setOnClickListener{
                            if (min2 != 1) {
                                textMinSecondCategory.setTextColor(Color.RED)
                            } else {

                                val popupThirdCategory = layoutInflater.inflate(R.layout.third_category_popup, null)
                                val depressedButton = popupThirdCategory.findViewById<Button>(R.id.depressed_button)
                                val funButton = popupThirdCategory.findViewById<Button>(R.id.fun_button)
                                val publishButton = popupThirdCategory.findViewById<Button>(R.id.Next3)

                                secondCategoryDialog.dismiss()

                                val dialogBuilderThirdCategory = AlertDialog.Builder(context)
                                dialogBuilderThirdCategory.setView(popupThirdCategory)

                                val thirdCategoryDialog = dialogBuilderThirdCategory.create()
                                thirdCategoryDialog.setCancelable(false)
                                thirdCategoryDialog.show()

                                depressedButton.setOnClickListener {

                                    depressedButtonClicked += 1
                                    if (depressedButtonClicked > 2) depressedButtonClicked = 1
                                    if (depressedButtonClicked != 2) {
                                        depressedButton.setBackgroundColor(Color.parseColor("#0082DD"))
                                        depressedButton.setTextColor(Color.WHITE)
                                        min3 += 1
                                    }
                                    else {
                                        depressedButton.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                                        depressedButton.setTextColor(Color.BLACK)
                                        min3 -= 1
                                    }
                                }

                                funButton.setOnClickListener {
                                    funButtonClicked += 1
                                    if (funButtonClicked > 2) funButtonClicked = 1
                                    if (funButtonClicked != 2) {
                                        funButton.setBackgroundColor(Color.parseColor("#0082DD"))
                                        funButton.setTextColor(Color.WHITE)
                                        min3 += 1
                                    }
                                    else {
                                        funButton.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                                        funButton.setTextColor(Color.BLACK)
                                        min3 -= 1
                                    }
                                }
                                publishButton.setOnClickListener {
                                    if (min3 == 1) {
                                        appViewModel.saveImageAndData(
                                            byHandClicked, funButtonClicked,
                                            clickS, clickP, clickL, clickM, clickB,
                                            clickI, clickC, clickN, clickA, clickH,
                                            description
                                        )

                                        //add on screen
                                        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                                            ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, filePath!!))
                                        else
                                            MediaStore.Images.Media.getBitmap(requireContext().contentResolver, filePath)
                                        appViewModel.saveNewImageToLocalDB(bitmap)
                                        showPosts()

                                        thirdCategoryDialog.dismiss()
                                    }
                                    else {
                                        Toast.makeText(context,"Оберіть один!", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            filePath = data.data

            if (addingNewPhoto) {
                try {
                    appViewModel.saveImageFilePath(filePath!!)
                    addWorkPopupImage?.setImageURI(filePath)
                }
                catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            else {
                try {
                    appViewModel.saveImageFilePath(filePath!!)
                    profilePictureImage?.setImageURI(filePath)
                }
                catch (e: IOException) {
                    e.printStackTrace()
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
