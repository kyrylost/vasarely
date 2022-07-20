package com.example.vasarely.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vasarely.SingleLiveEvent
import com.example.vasarely.model.UserDatabase
import com.example.vasarely.model.UsersDatabase
import com.example.vasarely.repository.UserData
import com.example.vasarely.repository.FoundedUserData

class FollowingViewModel() : ViewModel() {

    lateinit var userData: UserData
    lateinit var foundedUserData: FoundedUserData
    lateinit var userDatabase: UserDatabase
    lateinit var usersDatabase: UsersDatabase

//    var foundedUserDataChanged = SingleLiveEvent<Boolean>()
//
//    fun follow() {
//        var allowFollowing = true
//        for (i in userData.followingList) {
//            if (i == foundedUserData.uid) allowFollowing = false
//        }
//
//        if (allowFollowing) {
//            val newFollowingListString: String
//            if (userData.followingList.isEmpty()) {
//                newFollowingListString = foundedUserData.uid
//                userDatabase.addFollowing(userData.following.toInt() + 1, newFollowingListString)
//                userData.addNewValue(foundedUserData.uid)
//                Log.d("ifEmpty", userData.followingList.toString())
//            }
//            else {
//                userData.addNewValue(foundedUserData.uid)
//                newFollowingListString = userData.followingList.joinToString(",")
//                userDatabase.addFollowing(userData.following.toInt() + 1, newFollowingListString)
//                Log.d("ifNotEmpty", userData.followingList.toString())
//            }
//
//            if (foundedUserData.followersList.isEmpty()) {
//                usersDatabase.addFollower(foundedUserData.uid,
//                    foundedUserData.followers + 1,
//                    userData.uid)
//            }
//            else {
//                usersDatabase.addFollower(foundedUserData.uid,
//                    foundedUserData.followers + 1,
//                    userData.uid,
//                    foundedUserData.followersList.joinToString(","))
//            }
//
//            changeUsersLocalDataAfterFollow()
//
//        }
//    }
//
//    private fun changeUsersLocalDataAfterFollow () {
//        val currentFollowingNumber = userData.following.toInt()
//        userData.following = (currentFollowingNumber + 1).toString()
//
//        val currentNumberOfFollowers = foundedUserData.followers
//        foundedUserData.followers = (currentNumberOfFollowers + 1)
//        //change followersList in future
//        foundedUserDataChanged.postValue(true)
//    }

}