package com.example.vasarely.view

import android.app.ProgressDialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vasarely.R
import com.example.vasarely.databinding.SearchScreenBinding
import com.example.vasarely.view.service.NotificationService
import com.example.vasarely.viewmodel.primary.AppViewModel
import com.example.vasarely.viewmodel.secondary.SearchViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class SearchScreen : Fragment(R.layout.search_screen) {

    private val appViewModel: AppViewModel by activityViewModels()
    private val searchViewModel = SearchViewModel()
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

        appViewModel.userViewModel.dataChangeExceptions.observe(viewLifecycleOwner) { exception ->
            Toast.makeText(requireContext(), exception, Toast.LENGTH_LONG).show()
        }

        appViewModel.usersViewModel.dataChangeExceptions.observe(viewLifecycleOwner) { exception ->
            Toast.makeText(requireContext(), exception, Toast.LENGTH_LONG).show()
        }

        _binding = SearchScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun showData() {
            // Show data
        }
0
        val progressDialog = ProgressDialog(requireContext())

        appViewModel.userViewModel.userMutableLiveData.observe(viewLifecycleOwner) { preferencesAreSelected ->

            appViewModel.userViewModel.setUserDBStatus()
            if (progressDialog.isShowing) progressDialog.dismiss()

            if (!preferencesAreSelected) {
                val action = SearchScreenDirections.actionSearchScreenToPreferencesSelectionScreen()
                findNavController().navigate(action)
            }
            else {
                val serviceIntent = Intent(context, NotificationService::class.java)
                serviceIntent.putExtra("Uid", appViewModel.userViewModel.userDatabase.uid) //remove db ref
                context?.startForegroundService(serviceIntent)

                appViewModel.userViewModel.getData()
                appViewModel.usersViewModel.retrieveAllData()//databaseRecommendationsSearch()
            }
        }




        appViewModel.usersViewModel.localDbCopyLiveEvent.observe(viewLifecycleOwner) {
            appViewModel.recommendationsViewModel.getPostsData(
                it, appViewModel.userViewModel.userData)
        }




        appViewModel.recommendationsViewModel.recommendedPost.observe(viewLifecycleOwner) { post ->

                fun rotateImage(source: Bitmap, angle: Float) : Bitmap {
                    val matrix = Matrix()
                    matrix.postRotate(angle)
                    return Bitmap.createBitmap(
                        source, 0, 0, source.width, source.height, matrix, true)
                }


                val screenWidth = Resources.getSystem().displayMetrics.widthPixels

                val postImageView = ImageView(context)

                val params = screenWidth - dpToPx(60F)
                val postLinearLayoutParams = LinearLayout.LayoutParams(
                    params, LinearLayout.LayoutParams.WRAP_CONTENT)

                postImageView.layoutParams = postLinearLayoutParams
                postImageView.scaleType = ImageView.ScaleType.FIT_START

                postImageView.adjustViewBounds = true

                postImageView.margin(30F, 15F, 30F, 15F)

                if (post.byteCount < 50135040)
                    postImageView.setImageBitmap(post)
                else postImageView.setImageBitmap(rotateImage(post, 90f))
                binding.recs.addView(postImageView)
        }


        appViewModel.userViewModel.profilePicture.observe(viewLifecycleOwner) {
            appViewModel.userViewModel.saveProfilePictureToLocalDB(it)
        }


        if (!appViewModel.userViewModel.isLocalDataInitialized()) {
            if (appViewModel.userViewModel.isUserDBInitialized()) {
                appViewModel.userViewModel.getData()
                appViewModel.usersViewModel.retrieveAllData()//databaseRecommendationsSearch()
            }
            else {
                progressDialog.setMessage("Зачекайте, триває завантаження...")
                progressDialog.setCancelable(false)
                progressDialog.show()

                //searchViewModel.waitForEightSec()
                searchViewModel.stopWaiting.observe(viewLifecycleOwner) {
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), "Відсутній зв'язок з базою даних!", Toast.LENGTH_LONG).show()
                }

                //wait for 10 sec, if no send new request
            }
        }
        else showData()


        binding.searchInputLayout.measure(0,0)
        val searchInputLayoutHeight = binding.searchInputLayout.measuredHeight

        val logo: ImageView = binding.logo
        val logoParams: ViewGroup.LayoutParams = logo.layoutParams
        logoParams.width = searchInputLayoutHeight
        logoParams.height = searchInputLayoutHeight
        logo.layoutParams = logoParams


        //----------------------------Navigation between screens----------------------------------//
        binding.homeButton.setOnClickListener {
                val action = SearchScreenDirections.actionSearchScreenToMainScreen()
                findNavController().navigate(action)
            }

        binding.userPageButton.setOnClickListener {
            val action = SearchScreenDirections.actionSearchScreenToUserPersonalPageScreen()
            findNavController().navigate(action)
        }


        //----------------------------User searching----------------------------------------------//

        binding.search.addTextChangedListener {
            searchViewModel.getNameWithDelay(it.toString())
        }
        searchViewModel.name.observe(viewLifecycleOwner) {
            GlobalScope.launch {
                appViewModel.usersViewModel.findByUsername(it)
                this.cancel()
            }
        }

        appViewModel.usersViewModel.foundedUser.observe(viewLifecycleOwner) { foundedUsers ->
            appViewModel.usersViewModel.saveLastFoundedUsersData(foundedUsers)

            val popupMenu = PopupMenu(context, binding.search)
            popupMenu.inflate(R.menu.founded_users_menu)

            for ((userNumber, userData) in foundedUsers.withIndex()) {
                popupMenu.menu.add(0,userNumber,0,userData[1])
            }

            popupMenu.show()

            popupMenu.setOnMenuItemClickListener { selectedItem ->
                appViewModel.usersViewModel.saveFoundedUser(selectedItem.itemId)
                val action = SearchScreenDirections.actionSearchScreenToUserPageScreen()
                findNavController().navigate(action)
                true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}