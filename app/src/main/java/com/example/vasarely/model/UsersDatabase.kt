package com.example.vasarely.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.vasarely.SingleLiveEvent
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

@OptIn(DelicateCoroutinesApi::class)
class UsersDatabase : DatabaseRoot() {

    var localDbCopyInitialized: SingleLiveEvent<DataSnapshot> = SingleLiveEvent() //change name
    var foundedUserStorage = FoundedUserStorage()

    var foundedUser = SingleLiveEvent<MutableList<List<String>>>()
    var foundedUserPosts = foundedUserStorage.foundedUserPosts

//    var recommendation = usersStorage.recommendation

    init {
//        suspend fun recommendationsSearch() {
//            localDbCopyLiveEvent.postValue(localDbCopy.await().allData)
//        }
        GlobalScope.launch {
            localDbCopyInitialized.postValue(localDbCopy.await().allData) //observe in search. then go to RecVM(giving userdata)
            Log.d("localDbCopy init", "test")
        }
    }

//    fun getImage(userUid: String, postNumber: Int) = usersStorage.getImage(userUid, postNumber)

    fun getOtherUserPosts(uid : String, worksAmount : Int) = foundedUserStorage.getOtherUserPosts(uid, worksAmount)

    suspend fun findByUsername(name: String) {
        val foundedUsersData = mutableListOf<List<String>>()

        localDbCopy.await().apply {
            for (userData in this.allData.children) {
                val username = userData.child("userData").child("username").value.toString()

                val nameLength = name.count()

                if (username.lowercase() == name.lowercase()) {
                    val foundedUserData = mutableListOf<String>()
                    foundedUserData.add(userData.key.toString())
                    foundedUserData.add(username)
                    foundedUserData.add(userData.child("userData").child("worksAmount").value.toString())
                    foundedUserData.add(userData.child("userData").child("followers").value.toString())
                    foundedUserData.add(userData.child("userData").child("following").value.toString())
                    foundedUserData.add(userData.child("userData").child("followersList").value.toString())
                    foundedUsersData.addAll(0, listOf(foundedUserData))
                    //foundedUser.postValue(foundedUsersData)
                    Log.d("yes", userData.key.toString())
                }

                if (nameLength < username.count()) {
                    val usernameFirstN = (username.subSequence(0, nameLength)).toString()

                    if(usernameFirstN.lowercase() == name.lowercase()) {
                        val foundedUserData = mutableListOf<String>()
                        foundedUserData.add(userData.key.toString())
                        foundedUserData.add(username)
                        foundedUserData.add(userData.child("userData").child("worksAmount").value.toString())
                        foundedUserData.add(userData.child("userData").child("followers").value.toString())
                        foundedUserData.add(userData.child("userData").child("following").value.toString())
                        foundedUserData.add(userData.child("userData").child("followersList").value.toString())
                        foundedUsersData.add(foundedUserData)
                    }
                }

            }
            foundedUser.postValue(foundedUsersData)
        }
//        if (::localDbCopy.isInitialized) {
//
//        }

//        else findByUsername(name)
    }

    fun addFollower (userUid : String, followersNumber : Int, currentUserUid: String, followersUid : String = "") {
        databaseReference.child(userUid).child("userData").child("followers").setValue(followersNumber)
        if (followersUid == "") {
            databaseReference.child(userUid).child("userData").child("followersList").setValue(currentUserUid)
        }
        else {
            val newFollowersList = "$followersUid,$currentUserUid"
            databaseReference.child(userUid).child("userData").child("followersList").setValue(newFollowersList)
        }

    }

}