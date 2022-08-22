package com.example.vasarely.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vasarely.R
import com.example.vasarely.databinding.UserPersonalPageScreenBinding
import com.example.vasarely.view.recycler.PostAdapter
import com.example.vasarely.viewmodel.primary.AppViewModel
import com.example.vasarely.viewmodel.secondary.AddingWork
import com.google.android.material.textfield.TextInputEditText
import java.io.IOException


class UserPersonalPageScreen: Fragment(R.layout.user_personal_page_screen) {

    private val appViewModel: AppViewModel by activityViewModels()
    private var _binding: UserPersonalPageScreenBinding? = null
    private val binding get() = _binding!!

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

        appViewModel.userViewModel.dataChangeExceptions.observe(viewLifecycleOwner) { exception ->
            Toast.makeText(requireContext(), exception, Toast.LENGTH_LONG).show()
        }

        appViewModel.usersViewModel.dataChangeExceptions.observe(viewLifecycleOwner) { exception ->
            Toast.makeText(requireContext(), exception, Toast.LENGTH_LONG).show()
        }

        _binding = UserPersonalPageScreenBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.username.text = appViewModel.userViewModel.userData.username
        binding.followersNumber.text = appViewModel.userViewModel.userData.followers
        binding.followingNumber.text = appViewModel.userViewModel.userData.following

        if (appViewModel.userViewModel.isProfilePictureInitialized()) // Change to observer
            binding.avatarPlacer.setImageBitmap(appViewModel.userViewModel.userData.profilePicture)

        fun showPosts() {
            Log.d("showPosts" ,"Triggered")
            binding.layoutForProgressBar.removeAllViews()
            binding.postsRecyclerView.apply {
                val postAdapter =
                    PostAdapter(
                        appViewModel
                            .userViewModel
                            .userPostsData
                            .allUserPostsData
                    )

                layoutManager = GridLayoutManager(context, 3)
                adapter = postAdapter

                postAdapter.onItemClick = { bitmap ->
                    val dialogBuilder = AlertDialog.Builder(context)
                    val popupView = layoutInflater.inflate(R.layout.post_popup, null)

                    dialogBuilder.setView(popupView)
                    val addNoteDialog = dialogBuilder.create()
                    addNoteDialog.show()

                    val postImg = popupView.findViewById<ImageView>(R.id.Post)
                    postImg.setImageBitmap(bitmap)
                }
            }
        }

        if (appViewModel.userViewModel.isUserPostsDataInitializedAndProcessed()) {
            showPosts()
        }

        appViewModel.userViewModel.userPostsFound.observe(viewLifecycleOwner) {
            val progressBar = ProgressBar(requireContext())
            progressBar.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            progressBar.indeterminateDrawable
                .setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.accent),
                    PorterDuff.Mode.MULTIPLY
                )

            progressBar.margin(0F, 30F, 0F, 30F)

            binding.layoutForProgressBar.addView(progressBar)
        }

        appViewModel.userViewModel.postsProcessed.observe(viewLifecycleOwner) {
            showPosts()
        }

        //----------------------------Navigation between screens----------------------------------//
        //to SearchScreen
        binding.searchButton.setOnClickListener {
            val action = UserPersonalPageScreenDirections.actionUserPersonalPageScreenToSearchScreen()
            findNavController().navigate(action)
        }
        //to MainScreen
        binding.homeButton.setOnClickListener {
            val action = UserPersonalPageScreenDirections.actionUserPersonalPageScreenToMainScreen()
            findNavController().navigate(action)
        }

        binding.menuImgBtn.setOnClickListener {
            val action = UserPersonalPageScreenDirections.actionUserPersonalPageScreenToNewPreferencesScreen()
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

                appViewModel.userViewModel.saveProfilePicture()

                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                            requireContext().contentResolver, filePath!!
                        )
                    )
                else
                    MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver, filePath
                    )

                appViewModel.userViewModel.saveAddedProfilePictureToLocalDB(bitmap)
                binding.avatarPlacer.setImageBitmap(appViewModel.userViewModel.userData.profilePicture)
            }
        }


        binding.gridImgBtn.setOnClickListener {
            binding.gridSelected.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.accent))
            binding.columnSelected.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        }

        binding.columnImgBtn.setOnClickListener {
            binding.gridSelected.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
            binding.columnSelected.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.accent))
        }

        binding.menuImgBtn.setOnClickListener {

            val popupView =
                layoutInflater.inflate(R.layout.menu_user_personal_page_screen, null)

            val closeSettingsButton =
                popupView.findViewById<ImageButton>(R.id.closeButtonSettingsPopup)

            val likedPostButton =
                popupView.findViewById<ImageButton>(R.id.likedPostIcon)
            val likedPostText =
                popupView.findViewById<TextView>(R.id.likedPostText)

            val newPrefButton =
                popupView.findViewById<ImageButton>(R.id.newPrefButton)
            val newPrefText =
                popupView.findViewById<TextView>(R.id.new_pref_text)

            val changeNicknameButton =
                popupView.findViewById<ImageView>(R.id.changeNicknameButton)
            val changeNicknameTextView =
                popupView.findViewById<TextView>(R.id.settings_nickname_text)

            val changePasswordButton =
                popupView.findViewById<ImageView>(R.id.changePasswordButton)
            val changePasswordTextView =
                popupView.findViewById<TextView>(R.id.key_text)

            val buyPremiumButton =
                popupView.findViewById<ImageView>(R.id.buyPremiumButton)
            val buyPremium =
                popupView.findViewById<TextView>(R.id.subscription_text)

            val logoutButton =
                popupView.findViewById<ImageButton>(R.id.logoutButton)
            val logoutText =
                popupView.findViewById<TextView>(R.id.logout_text)


            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setView(popupView)

            val settingsDialog = dialogBuilder.create()
            settingsDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            settingsDialog.show()


            fun likedPostOnClick() {
                val action = UserPersonalPageScreenDirections
                        .actionUserPersonalPageScreenToLikedPostScreen()
                findNavController().navigate(action)

                settingsDialog.dismiss()
            }

            fun newPreferenceOnClick() {
                val action = UserPersonalPageScreenDirections
                    .actionUserPersonalPageScreenToNewPreferencesScreen()
                findNavController().navigate(action)

                settingsDialog.dismiss()
            }

            fun changeNicknameOnClick() {
                val nicknamePopup =
                    layoutInflater.inflate(R.layout.nickname_change_popup, null)

                val btnSaveNickname =
                    nicknamePopup.findViewById<Button>(R.id.setNewNicknameButton)
                val closeNicknameButton =
                    nicknamePopup.findViewById<ImageButton>(R.id.closeButtonNicknamePopup)
                val changeNick =
                    nicknamePopup.findViewById<TextInputEditText>(R.id.username_input)

                val dialogBuilderNickname = AlertDialog.Builder(context)
                dialogBuilderNickname.setView(nicknamePopup)
                val changeNicknameDialog = dialogBuilderNickname.create()
                changeNicknameDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                changeNicknameDialog.show()


                closeNicknameButton.setOnClickListener{
                    changeNicknameDialog.dismiss()
                }

                btnSaveNickname.setOnClickListener {
                    val newNickname = changeNick.text.toString()
                    if (newNickname.isNotEmpty()) {
                        appViewModel.userViewModel.updateName(newNickname)
                        changeNicknameDialog.dismiss()
                    } else if (newNickname.isEmpty()) {
                        changeNick.error = getString(R.string.empty_username_error_message)
                    }
                }
            }

            fun changePasswordOnClick() {
                val passwordPopup =
                    layoutInflater.inflate(R.layout.change_password_popup, null)

//                val passwordInputPopup =
//                    passwordPopup.findViewById<TextInputEditText>(R.id.password_input)
                val closePasswordButton =
                    passwordPopup.findViewById<ImageButton>(R.id.closeButtonPasswordPopup)
//                val savePasswordButton =
//                    passwordPopup.findViewById<Button>(R.id.setNewPasswordButton)

                val dialogBuilderPassword = AlertDialog.Builder(context)
                dialogBuilderPassword.setView(passwordPopup)
                val changePasswordDialog = dialogBuilderPassword.create()
                changePasswordDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                changePasswordDialog.show()


                closePasswordButton.setOnClickListener{
                    changePasswordDialog.dismiss()
                }
            }

            fun buyPremiumOnClick() {
                val action = UserPersonalPageScreenDirections
                    .actionUserPersonalPageScreenToBuyPremiumScreen()
                findNavController().navigate(action)

                settingsDialog.dismiss()
            }

            fun logoutOnClick() {
                appViewModel.userViewModel.logout()

                val sharedPref =
                    activity?.getSharedPreferences("userLoginData", Context.MODE_PRIVATE)
                        ?: return
                val editor = sharedPref.edit()
                editor.putString("remember", "false")
                editor.apply()

                settingsDialog.dismiss()

                val action = UserPersonalPageScreenDirections
                    .actionUserPersonalPageScreenToSignInSignUpScreen()
                findNavController().navigate(action)
            }

            // -------------- Close button onClick -------------- //
            closeSettingsButton.setOnClickListener{
                settingsDialog.dismiss()
            }

            // -------------- Liked post onClick -------------- //
            likedPostButton.setOnClickListener {
                likedPostOnClick()
            }
            likedPostText.setOnClickListener {
                likedPostOnClick()
            }

            // -------------- New preference onClick -------------- //
            newPrefButton.setOnClickListener {
                newPreferenceOnClick()
            }
            newPrefText.setOnClickListener {
                newPreferenceOnClick()
            }

            // -------------- Change nickname onClick -------------- //
            changeNicknameButton.setOnClickListener {
                changeNicknameOnClick()
            }
            changeNicknameTextView.setOnClickListener {
                changeNicknameOnClick()
            }

            // -------------- Change password onClick -------------- //
            changePasswordButton.setOnClickListener {
                changePasswordOnClick()
            }
            changePasswordTextView.setOnClickListener {
                changePasswordOnClick()
            }

            // -------------- Buy premium onClick -------------- //
            buyPremiumButton.setOnClickListener {
                buyPremiumOnClick()
            }
            buyPremium.setOnClickListener {
                buyPremiumOnClick()
            }

            // -------------- Logout onClick -------------- //
            logoutButton.setOnClickListener {
                logoutOnClick()
            }

            logoutText.setOnClickListener {
                logoutOnClick()
            }

        }

        //------------------------------------Adding Work-----------------------------------------//
        binding.addWork.setOnClickListener {

            val addingWork = AddingWork()

            val popupAddWork = layoutInflater.inflate(R.layout.add_new_photo_popup, null)
            var description = ""

            addWorkPopupImage = popupAddWork.findViewById(R.id.AddWorkImage)

            addingNewPhoto = true
            launchGallery()

            val dialogBuilderAddWork = AlertDialog.Builder(context)
            dialogBuilderAddWork.setView(popupAddWork)

            val addWorkDialog = dialogBuilderAddWork.create()
            addWorkDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            addWorkDialog.show()

            val addNewPhotoNextButton = popupAddWork.findViewById<Button>(R.id.addNewPhotoNextButton)

            addNewPhotoNextButton.setOnClickListener {
                description =
                    popupAddWork.findViewById<TextInputEditText>(
                        R.id.username_input).text.toString()

                val popupFirstCategory =
                    layoutInflater.inflate(R.layout.first_category_popup, null)
                val nextFirstCategory =
                    popupFirstCategory.findViewById<Button>(R.id.Next1)
                val byHand =
                    popupFirstCategory.findViewById<Button>(R.id.by_hand_button)
                val compGraph =
                    popupFirstCategory.findViewById<Button>(R.id.comp_graph_button)
                val textMinFirstCategory =
                    popupFirstCategory.findViewById<TextView>(R.id.first_category_min)

                addWorkDialog.dismiss()

                val dialogBuilderFirstCategory = AlertDialog.Builder(context)
                dialogBuilderFirstCategory.setView(popupFirstCategory)
                val firstCategoryDialog = dialogBuilderFirstCategory.create()
                firstCategoryDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                firstCategoryDialog.setCancelable(false)
                firstCategoryDialog.show()


                byHand.setOnClickListener {
                    addingWork.byHandClicked()
                }
                addingWork.byHandColorChanged.observe(viewLifecycleOwner) {
                    byHand.setBackgroundColor(addingWork.byHandBGColor)
                    byHand.setTextColor(addingWork.byHandTextColor)
                }

                compGraph.setOnClickListener {
                    addingWork.computerGraphClicked()
                }
                addingWork.computerGraphColorChanged.observe(viewLifecycleOwner) {
                    compGraph.setBackgroundColor(addingWork.computerGraphBGColor)
                    compGraph.setTextColor(addingWork.computerGraphTextColor)
                }


                nextFirstCategory.setOnClickListener {
                    addingWork.isTechniqueSelected()
                }

                addingWork.techniqueNotSelected.observe(viewLifecycleOwner) {
                    textMinFirstCategory.setTextColor(Color.RED)
                }

                addingWork.techniqueSelected.observe(viewLifecycleOwner) {
                    firstCategoryDialog.dismiss()
                }
            }

            addingWork.techniqueSelected.observe(viewLifecycleOwner) {

                val popupSecondCategory =
                    layoutInflater.inflate(R.layout.second_category_popup, null)
                val nextSecondCategory = popupSecondCategory.findViewById<Button>(R.id.Next2)
                val textMinSecondCategory =
                    popupSecondCategory.findViewById<TextView>(R.id.second_category_min)

                val portraitButton =
                    popupSecondCategory.findViewById<Button>(R.id.portrait_button)
                val landscapeButton =
                    popupSecondCategory.findViewById<Button>(R.id.landscape_button)
                val marineButton =
                    popupSecondCategory.findViewById<Button>(R.id.marine_button)
                val battlePaintingButton =
                    popupSecondCategory.findViewById<Button>(R.id.battle_painting_button)
                val interiorButton =
                    popupSecondCategory.findViewById<Button>(R.id.interior_button)
                val caricatureButton =
                    popupSecondCategory.findViewById<Button>(R.id.caricature_button)
                val nudeButton =
                    popupSecondCategory.findViewById<Button>(R.id.nude_button)
                val animeButton =
                    popupSecondCategory.findViewById<Button>(R.id.anime_button)
                val horrorButton =
                    popupSecondCategory.findViewById<Button>(R.id.horror_button)
                val stillLifeButton =
                    popupSecondCategory.findViewById<Button>(R.id.still_life_button)

                val dialogBuilderSecondCategory = AlertDialog.Builder(context)
                dialogBuilderSecondCategory.setView(popupSecondCategory)
                val secondCategoryDialog = dialogBuilderSecondCategory.create()
                secondCategoryDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                secondCategoryDialog.setCancelable(false)
                secondCategoryDialog.show()

                stillLifeButton.setOnClickListener {
                    addingWork.stillLifeButtonClicked()
                }
                addingWork.stillLifeButtonColorChanged.observe(viewLifecycleOwner) {
                    stillLifeButton.setBackgroundColor(addingWork.stillLifeButtonBGColor)
                    stillLifeButton.setTextColor(addingWork.stillLifeButtonTextColor)
                }

                portraitButton.setOnClickListener {
                    addingWork.portraitButtonClicked()
                }
                addingWork.portraitButtonColorChanged.observe(viewLifecycleOwner) {
                    portraitButton.setBackgroundColor(addingWork.portraitButtonBGColor)
                    portraitButton.setTextColor(addingWork.portraitButtonTextColor)
                }

                landscapeButton.setOnClickListener {
                    addingWork.landscapeButtonClicked()
                }
                addingWork.landscapeButtonColorChanged.observe(viewLifecycleOwner) {
                    landscapeButton.setBackgroundColor(addingWork.landscapeButtonBGColor)
                    landscapeButton.setTextColor(addingWork.landscapeButtonTextColor)
                }

                marineButton.setOnClickListener {
                    addingWork.marineButtonClicked()
                }
                addingWork.marineButtonColorChanged.observe(viewLifecycleOwner) {
                    marineButton.setBackgroundColor(addingWork.marineButtonBGColor)
                    marineButton.setTextColor(addingWork.marineButtonTextColor)
                }

                battlePaintingButton.setOnClickListener {
                    addingWork.battlePaintingButtonClicked()
                }
                addingWork.battlePaintingButtonColorChanged.observe(viewLifecycleOwner) {
                    battlePaintingButton.setBackgroundColor(addingWork.battlePaintingButtonBGColor)
                    battlePaintingButton.setTextColor(addingWork.battlePaintingButtonTextColor)
                }

                interiorButton.setOnClickListener {
                    addingWork.interiorButtonClicked()
                }
                addingWork.interiorButtonColorChanged.observe(viewLifecycleOwner) {
                    interiorButton.setBackgroundColor(addingWork.interiorButtonBGColor)
                    interiorButton.setTextColor(addingWork.interiorButtonTextColor)
                }

                caricatureButton.setOnClickListener {
                    addingWork.caricatureButtonClicked()
                }
                addingWork.caricatureButtonColorChanged.observe(viewLifecycleOwner) {
                    caricatureButton.setBackgroundColor(addingWork.caricatureButtonBGColor)
                    caricatureButton.setTextColor(addingWork.caricatureButtonTextColor)
                }

                nudeButton.setOnClickListener {
                    addingWork.nudeButtonClicked()
                }
                addingWork.nudeButtonColorChanged.observe(viewLifecycleOwner) {
                    nudeButton.setBackgroundColor(addingWork.nudeButtonBGColor)
                    nudeButton.setTextColor(addingWork.nudeButtonTextColor)
                }

                animeButton.setOnClickListener {
                    addingWork.animeButtonClicked()
                }
                addingWork.animeButtonColorChanged.observe(viewLifecycleOwner) {
                    animeButton.setBackgroundColor(addingWork.animeButtonBGColor)
                    animeButton.setTextColor(addingWork.animeButtonTextColor)
                }

                horrorButton.setOnClickListener {
                    addingWork.horrorButtonClicked()
                }
                addingWork.horrorButtonColorChanged.observe(viewLifecycleOwner) {
                    horrorButton.setBackgroundColor(addingWork.horrorButtonBGColor)
                    horrorButton.setTextColor(addingWork.horrorButtonTextColor)
                }

                nextSecondCategory.setOnClickListener {
                    addingWork.isGenreSelected()
                    addingWork.genreNotSelected.observe(viewLifecycleOwner) {
                        textMinSecondCategory.setTextColor(Color.RED)
                    }
                    addingWork.genreSelected.observe(viewLifecycleOwner) {
                        secondCategoryDialog.dismiss()
                    }
                }
            }

            addingWork.genreSelected.observe(viewLifecycleOwner) {
                val popupThirdCategory =
                    layoutInflater.inflate(R.layout.third_category_popup, null)
                val depressedButton =
                    popupThirdCategory.findViewById<Button>(R.id.depressed_button)
                val funButton =
                    popupThirdCategory.findViewById<Button>(R.id.fun_button)
                val publishButton =
                    popupThirdCategory.findViewById<Button>(R.id.Next3)


                val dialogBuilderThirdCategory = AlertDialog.Builder(context)
                dialogBuilderThirdCategory.setView(popupThirdCategory)
                val thirdCategoryDialog = dialogBuilderThirdCategory.create()
                thirdCategoryDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                thirdCategoryDialog.setCancelable(false)
                thirdCategoryDialog.show()

                funButton.setOnClickListener {
                    addingWork.funButtonClicked()
                }
                addingWork.funButtonColorChanged.observe(viewLifecycleOwner) {
                    funButton.setBackgroundColor(addingWork.funButtonBGColor)
                    funButton.setTextColor(addingWork.funButtonTextColor)
                }

                depressedButton.setOnClickListener {
                    addingWork.depressedButtonClicked()
                }
                addingWork.depressedButtonColorChanged.observe(viewLifecycleOwner) {
                    depressedButton.setBackgroundColor(addingWork.depressedButtonBGColor)
                    depressedButton.setTextColor(addingWork.depressedButtonTextColor)
                }


                publishButton.setOnClickListener {
                    addingWork.isMoodSelected()

                    addingWork.moodNotSelected.observe(viewLifecycleOwner) {
                        Toast.makeText(context, "Оберіть один!", Toast.LENGTH_LONG).show()
                    }
                    addingWork.moodSelected.observe(viewLifecycleOwner) {
                        thirdCategoryDialog.dismiss()

                        appViewModel.userViewModel.saveImageAndData(
                            addingWork.byHandClicked,
                            addingWork.funButtonClicked,
                            addingWork.stillLifeButtonClicked,
                            addingWork.portraitButtonClicked,
                            addingWork.landscapeButtonClicked,
                            addingWork.marineButtonClicked,
                            addingWork.battlePaintingButtonClicked,
                            addingWork.interiorButtonClicked,
                            addingWork.caricatureButtonClicked,
                            addingWork.nudeButtonClicked,
                            addingWork.animeButtonClicked,
                            addingWork.horrorButtonClicked,
                            description
                        )

                        //add on screen
                        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                            ImageDecoder.decodeBitmap(
                                ImageDecoder.createSource(
                                    requireContext().contentResolver, filePath!!
                                )
                            )
                        else
                            MediaStore.Images.Media.getBitmap(
                                requireContext().contentResolver, filePath
                            )

                        appViewModel.userViewModel.saveNewImageToLocalDB(bitmap)
                        showPosts()
                    }
                }
            }
        }
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
    }

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {

            val data: Intent? = result.data

            if ((data == null) || (data.data == null))
                return@registerForActivityResult

            filePath = data.data

            if (addingNewPhoto) {
                try {
                    appViewModel.userViewModel.saveImageFilePath(filePath!!)
                    addWorkPopupImage?.setImageURI(filePath)
                }
                catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                try {
                    appViewModel.userViewModel.saveImageFilePath(filePath!!)
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
