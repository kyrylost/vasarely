package com.example.vasarely.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vasarely.R
import com.example.vasarely.databinding.UserPersonalPageScreenBinding
import com.example.vasarely.viewmodel.AppViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException

class UserPersonalPageScreen: Fragment(R.layout.user_personal_page_screen) {

    private val appViewModel: AppViewModel by activityViewModels()
    private var _binding: UserPersonalPageScreenBinding? = null
    private val binding get() = _binding!!
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    var addWork: ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        appViewModel.userData.observe(viewLifecycleOwner) {
            appViewModel.processData(it)
        }

        _binding = UserPersonalPageScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val montserratBoldFont: Typeface? =
            ResourcesCompat.getFont(requireContext(), R.font.montserrat_bold)
        val montserratRegularFont: Typeface? =
            ResourcesCompat.getFont(requireContext(), R.font.montserrat_regular)
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        val popupAddwork = layoutInflater.inflate(R.layout.add_new_photo_popup, null)

        addWork = popupAddwork.findViewById<ImageView>(R.id.AddWorkImage)

        binding.username.typeface = montserratBoldFont
        binding.subs.typeface = montserratBoldFont
        binding.follow.typeface = montserratBoldFont
        binding.addWork.typeface = montserratBoldFont

        if (appViewModel.isLocalDataInitialized())
            binding.username.text = appViewModel.localData.username


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
        binding.addWork.setOnClickListener {

            var min1 = 0
            var min = 0
            launchGallery()
            val dialogBuilderAddWork = AlertDialog.Builder(context)
            dialogBuilderAddWork.setView(popupAddwork)

            val changeNicknameDialog = dialogBuilderAddWork.create()
            changeNicknameDialog.show()

            val btn = popupAddwork.findViewById<Button>(R.id.button)
            btn.setOnClickListener {
                val popupfirstcategory = layoutInflater.inflate(R.layout.first_category_popup, null)
                val Next1 = popupfirstcategory.findViewById<Button>(R.id.Next1)

                val popupsecondcategory = layoutInflater.inflate(R.layout.second_category_popup, null)

                val Next2 = popupsecondcategory.findViewById<Button>(R.id.Next2)
                val textmin2 = popupsecondcategory.findViewById<TextView>(R.id.second_category_min)

                val popupthirdcategory = layoutInflater.inflate(R.layout.third_category_popup, null)

                val byHand = popupfirstcategory.findViewById<Button>(R.id.by_hand_button)
                val compGraph = popupfirstcategory.findViewById<Button>(R.id.comp_graph_button)
                val textmin = popupfirstcategory.findViewById<TextView>(R.id.first_category_min)

                var byHandClicked = 0
                var computerGraphClicked = 0

                changeNicknameDialog.dismiss()
                val dialogBuilderFirstCategory = AlertDialog.Builder(context)
                dialogBuilderFirstCategory.setView(popupfirstcategory)

                val FirstCategoryDialog = dialogBuilderFirstCategory.create()
                FirstCategoryDialog.show()

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
                Next1.setOnClickListener {
                    if (min1 < 1) {
                        textmin.setTextColor(Color.RED)

                    } else {
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

                        val portraitButton = popupsecondcategory.findViewById<Button>(R.id.portrait_button)
                        val landscapeButton = popupsecondcategory.findViewById<Button>(R.id.landscape_button)
                        val marineButton = popupsecondcategory.findViewById<Button>(R.id.marine_button)
                        val battlePaintingButton = popupsecondcategory.findViewById<Button>(R.id.battle_painting_button)
                        val interiorButton = popupsecondcategory.findViewById<Button>(R.id.interior_button)
                        val caricatureButton = popupsecondcategory.findViewById<Button>(R.id.caricature_button)
                        val nudeButton = popupsecondcategory.findViewById<Button>(R.id.nude_button)
                        val animeButton = popupsecondcategory.findViewById<Button>(R.id.anime_button)
                        val horrorButton = popupsecondcategory.findViewById<Button>(R.id.horror_button)
                        val stillLifeButton = popupsecondcategory.findViewById<Button>(R.id.still_life_button)

                        FirstCategoryDialog.dismiss()
                        val dialogBuilderSecondCategory = AlertDialog.Builder(context)
                        dialogBuilderSecondCategory.setView(popupsecondcategory)

                        val SecondCategoryDialog = dialogBuilderSecondCategory.create()
                        SecondCategoryDialog.show()

                        stillLifeButton.setOnClickListener {
                            val funBut = stillLifeButton
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
                        portraitButton.setOnClickListener {
                            val funBut = portraitButton
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
                        landscapeButton.setOnClickListener {
                            val funBut = landscapeButton
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
                        marineButton.setOnClickListener {
                            val funBut = marineButton
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
                        battlePaintingButton.setOnClickListener {
                            val funBut = battlePaintingButton
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
                        interiorButton.setOnClickListener {
                            val funBut = interiorButton
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
                        caricatureButton.setOnClickListener {
                            val funBut = caricatureButton
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
                        nudeButton.setOnClickListener {
                            val funBut = nudeButton
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
                        animeButton.setOnClickListener {
                            val funBut = animeButton
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
                        horrorButton.setOnClickListener {
                            val funBut = horrorButton
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
                        Next2.setOnClickListener{
                            if (min < 2) {
                                textmin2.setTextColor(Color.RED)
                            } else{
                                var depressedButtonClicked = 0
                                var funButtonClicked = 0
                                val depressedButton = popupthirdcategory.findViewById<Button>(R.id.depressed_button)
                                val funButton = popupthirdcategory.findViewById<Button>(R.id.fun_button)
                                SecondCategoryDialog.dismiss()
                                val dialogBuilderThirdCategory = AlertDialog.Builder(context)
                                dialogBuilderThirdCategory.setView(popupthirdcategory)

                                val ThirdCategoryDialog = dialogBuilderThirdCategory.create()
                                ThirdCategoryDialog.show()
                                depressedButton.setOnClickListener {
                                    val depress = depressedButton
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
                                funButton.setOnClickListener {
                                    val funBut = funButton
                                    funButtonClicked += 1
                                    if (funButtonClicked > 2) funButtonClicked = 1
                                    if (funButtonClicked != 2) {
                                        funBut.setBackgroundColor(Color.parseColor("#0082DD"))
                                        funBut.setTextColor(Color.WHITE)}
                                    else{
                                        funBut.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                                        funBut.setTextColor(Color.BLACK)
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
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                addWork?.setImageURI(filePath)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

        override fun onDestroy() {
            super.onDestroy()
            _binding = null
        }
    }
