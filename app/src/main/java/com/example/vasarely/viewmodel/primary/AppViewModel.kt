package com.example.vasarely.viewmodel.primary

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vasarely.SingleLiveEvent

class AppViewModel : ViewModel() {

    var userViewModel = UserViewModel()
    var usersViewModel = UsersViewModel()
    var recommendationsViewModel = RecommendationsViewModel()

    var foundedUserDataChanged = SingleLiveEvent<Boolean>()

    fun follow() {
        var allowFollowing = true
        for (i in userViewModel.userData.followingList) {
            if (i == usersViewModel.foundedUserData.uid) allowFollowing = false
        }

        if (allowFollowing) {
            val newFollowingListString: String
            if (userViewModel.userData.followingList.isEmpty()) {
                newFollowingListString = usersViewModel.foundedUserData.uid
                userViewModel.userDatabase.addFollowing(
                    userViewModel.userData.following.toInt() + 1,
                    newFollowingListString
                )
                userViewModel.userData.addNewValue(usersViewModel.foundedUserData.uid)
                Log.d("ifEmpty", userViewModel.userData.followingList.toString())
            } else {
                userViewModel.userData.addNewValue(usersViewModel.foundedUserData.uid)
                newFollowingListString = userViewModel.userData.followingList.joinToString(",")
                userViewModel.userDatabase.addFollowing(
                    userViewModel.userData.following.toInt() + 1,
                    newFollowingListString
                )
                Log.d("ifNotEmpty", userViewModel.userData.followingList.toString())
            }

            if (usersViewModel.foundedUserData.followersList.isEmpty()) {
                usersViewModel.usersDatabase.addFollower(
                    usersViewModel.foundedUserData.uid,
                    usersViewModel.foundedUserData.followers + 1,
                    userViewModel.userData.uid
                )
            } else {
                usersViewModel.usersDatabase.addFollower(
                    usersViewModel.foundedUserData.uid,
                    usersViewModel.foundedUserData.followers + 1,
                    userViewModel.userData.uid,
                    usersViewModel.foundedUserData.followersList.joinToString(",")
                )
            }

            changeUsersLocalDataAfterFollow()

        }
    }

    private fun changeUsersLocalDataAfterFollow() {
        val currentFollowingNumber = userViewModel.userData.following.toInt()
        userViewModel.userData.following = (currentFollowingNumber + 1).toString()

        val currentNumberOfFollowers = usersViewModel.foundedUserData.followers
        usersViewModel.foundedUserData.followers = (currentNumberOfFollowers + 1)
        //change followersList in future
        foundedUserDataChanged.postValue(true)
    }
}