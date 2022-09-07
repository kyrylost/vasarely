package com.example.vasarely.database.users

import android.util.Log
import com.example.vasarely.SingleLiveEvent
import com.example.vasarely.database.root.DatabaseRoot
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class UsersDatabase : DatabaseRoot() {

    var localDbCopyLiveEvent: SingleLiveEvent<DataSnapshot> = SingleLiveEvent() //change name
    private var foundedUserStorage = FoundedUserStorage()

    var foundedUser = SingleLiveEvent<MutableList<List<String>>>()
    var foundedUserPosts = foundedUserStorage.foundedUserPosts


    init {
        localDbCopyInitialized.observeForever {
            CoroutineScope(Dispatchers.IO).launch {
                localDbCopyLiveEvent.postValue(localDbCopy.await().allData) //observe in search. then go to RecVM(giving userdata)
                Log.d("localDbCopy init", "test")
            }
        }
    }


    fun getOtherUserPosts(uid: String, worksAmount: Int) =
        foundedUserStorage.getOtherUserPosts(uid, worksAmount)

    suspend fun findByUsername(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val foundedUsersData = mutableListOf<List<String>>()

            localDbCopy.await().apply {
                for (userData in this.allData.children) {
                    val username = userData.child("userData").child("username").value.toString()

                    val nameLength = name.count()

                    if (username.lowercase() == name.lowercase()) {
                        val foundedUserData = mutableListOf<String>()
                        foundedUserData.add(userData.key.toString())
                        foundedUserData.add(username)
                        foundedUserData.add(
                            userData.child("userData").child("worksAmount").value.toString()
                        )
                        foundedUserData.add(
                            userData.child("userData").child("followers").value.toString()
                        )
                        foundedUserData.add(
                            userData.child("userData").child("following").value.toString()
                        )
                        foundedUserData.add(
                            userData.child("userData").child("followersList").value.toString()
                        )
                        foundedUsersData.addAll(0, listOf(foundedUserData))
                        //foundedUser.postValue(foundedUsersData)
                        Log.d("yes", userData.key.toString())
                    }

                    if (nameLength < username.count()) {
                        val usernameFirstN = (username.subSequence(0, nameLength)).toString()

                        if (usernameFirstN.lowercase() == name.lowercase()) {
                            val foundedUserData = mutableListOf<String>()
                            foundedUserData.add(userData.key.toString())
                            foundedUserData.add(username)
                            foundedUserData.add(
                                userData.child("userData").child("worksAmount").value.toString()
                            )
                            foundedUserData.add(
                                userData.child("userData").child("followers").value.toString()
                            )
                            foundedUserData.add(
                                userData.child("userData").child("following").value.toString()
                            )
                            foundedUserData.add(
                                userData.child("userData").child("followersList").value.toString()
                            )
                            foundedUsersData.add(foundedUserData)
                        }
                    }

                }
                foundedUser.postValue(foundedUsersData)
            }
            this.cancel()
        }
    }

    fun addFollower(
        userUid: String,
        followersNumber: Int,
        currentUserUid: String,
        followersUid: String = ""
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            databaseReference.child(userUid)
                .child("userData")
                .child("followers")
                .setValue(followersNumber)

            if (followersUid == "") {
                databaseReference.child(userUid)
                    .child("userData")
                    .child("followersList")
                    .setValue(currentUserUid)
            } else {
                val newFollowersList = "$followersUid,$currentUserUid"
                databaseReference.child(userUid)
                    .child("userData")
                    .child("followersList")
                    .setValue(newFollowersList)
            }
            this.cancel()
        }
    }

}