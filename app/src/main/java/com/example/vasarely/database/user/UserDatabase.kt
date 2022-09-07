package com.example.vasarely.database.user

import android.graphics.Bitmap
import android.net.Uri
import android.os.Looper
import android.util.Log
import com.example.vasarely.SingleLiveEvent
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


class UserDatabase : UserAuth() {

    lateinit var preferencesReference: DatabaseReference

    private lateinit var userStorage: UserStorage
    var userStorageInitialized = SingleLiveEvent<Boolean>()

    private var amountOfWorks = 0

    var userDataFound: SingleLiveEvent<Any> = SingleLiveEvent()

    var allUserPosts = SingleLiveEvent<List<Bitmap>>()
    var profilePicture = SingleLiveEvent<Bitmap>()

    init {
        uidInit.observeForever { uid ->
            CoroutineScope(Dispatchers.IO).launch {
                userStorage = UserStorage(uid)
                allUserPosts = userStorage.allUserPosts
                profilePicture = userStorage.profilePicture
                userStorageInitialized.postValue(true)
                this.cancel()
            }
        }
    }

    fun saveProfilePicture(filePath: Uri) =
        userStorage.saveProfilePicture(filePath)

    fun saveImage(filePath: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("UserDatabase", "saveImage")
            amountOfWorks += 1
            userStorage.saveImage(filePath, amountOfWorks) {
                Log.d("UserDatabase", "callback")
                currentUserDb
                    .child("userData")
                    .child("worksAmount")
                    .setValue(amountOfWorks).addOnSuccessListener {
                        this.cancel()
                    }
            }


        }
    }

    fun saveImageDescription(description: String) {
        currentUserDb
            .child("profileData")
            .child("posts")
            .child("$amountOfWorks")
            .child("description")
            .setValue(description)
    }

    fun saveHashtags(technique: String, mood: String, genre: String) {
        currentUserDb
            .child("profileData")
            .child("posts")
            .child("$amountOfWorks")
            .child("hashtags")
            .child("mood")
            .setValue(mood)

        currentUserDb.child("profileData")
            .child("posts")
            .child("$amountOfWorks")
            .child("hashtags")
            .child("technique")
            .setValue(technique)

        currentUserDb.child("profileData")
            .child("posts")
            .child("$amountOfWorks")
            .child("hashtags")
            .child("genre")
            .setValue(genre)
    }

    fun savePreference(technique: String, mood: String, selectedGenres: List<String>) {
        preferencesReference = currentUserDb
            .child("userData")
            .child("preferences")

        val techniqueReference = preferencesReference.child("technique")
        val moodReference = preferencesReference.child("mood")
        val genresReference = preferencesReference.child("genres")

        techniqueReference.setValue(technique).addOnFailureListener { exception ->
            dataChangeExceptions.postValue(exception.toString())
        }

        moodReference.setValue(mood).addOnFailureListener { exception ->
            dataChangeExceptions.postValue(exception.toString())
        }

        genresReference.setValue(selectedGenres).addOnFailureListener { exception ->
            dataChangeExceptions.postValue(exception.toString())
        }
    }

    fun updateName(newNickname: String) {
        currentUserDb
            .child("userData")
            .child("username")
            .setValue(newNickname).addOnFailureListener { exception ->
                dataChangeExceptions.postValue(exception.toString())
            }
    }

    fun addFollowing(followingNumber: Int, followingUsersUid: String) {
        databaseReference
            .child(uid)
            .child("userData")
            .child("following")
            .setValue(followingNumber)
        databaseReference
            .child(uid)
            .child("userData")
            .child("followingList")
            .setValue(followingUsersUid)
    }

    fun getData() {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("getDataScope", (Looper.myLooper() == Looper.getMainLooper()).toString())

            currentUserDb
                .child("userData")
                .get().addOnSuccessListener { userData ->
                    userDataFound.postValue(userData.value)

                    userStorage.getProfilePicture()

                    val dataSnapshot = userData.value as HashMap<*, *> //don`t use hashMap

                    amountOfWorks = if (dataSnapshot["worksAmount"] != null)
                        dataSnapshot["worksAmount"].toString().toInt()
                    else
                        0

                    userStorage.retrieveAllUserPosts(amountOfWorks)

                }.addOnFailureListener { exception ->
                    dataChangeExceptions.postValue(exception.toString())
                    getData()
                }
        }
    }

}