package com.example.vasarely.database.user

import android.graphics.Bitmap
import android.net.Uri
import com.example.vasarely.SingleLiveEvent
import com.google.firebase.database.DatabaseReference


class UserDatabase : UserAuth() {

    lateinit var preferencesReference: DatabaseReference

    private lateinit var userStorage: UserStorage
    var userStorageInitialized = SingleLiveEvent<Boolean>()

    private var amountOfWorks = 0

    var userData: SingleLiveEvent<Any> = SingleLiveEvent()

    var allUserPosts = SingleLiveEvent<List<Bitmap>>()
    var profilePicture = SingleLiveEvent<Bitmap>()

    init {
        uidInit.observeForever { uid ->
            userStorage = UserStorage(uid)
            allUserPosts = userStorage.allUserPosts
            profilePicture = userStorage.profilePicture
            userStorageInitialized.postValue(true)
        }
    }

    fun saveProfilePicture(filePath: Uri) =
        userStorage.saveProfilePicture(filePath)

    fun saveImage(filePath: Uri) {
        amountOfWorks += 1
        userStorage.saveImage(filePath, amountOfWorks)
        currentUserDb.child("userData").child("worksAmount").setValue(amountOfWorks)
    }

    fun saveImageDescription(description: String) {
        currentUserDb.child("profileData").child("posts")
            .child("$amountOfWorks").child("description").setValue(description)
    }

    fun saveHashtags(technique : String, mood : String, genre : String) {
        currentUserDb.child("profileData").child("posts")
            .child("$amountOfWorks").child("hashtags")
            .child("mood").setValue(mood)

        currentUserDb.child("profileData").child("posts")
            .child("$amountOfWorks").child("hashtags")
            .child("technique").setValue(technique)

        currentUserDb.child("profileData").child("posts")
            .child("$amountOfWorks").child("hashtags")
            .child("genre").setValue(genre)
    }

    fun savePreference(technique : String, mood : String, selectedGenres : List<String>) {
        preferencesReference = currentUserDb.child("userData").child("preferences")

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
        currentUserDb.child("userData").child("username").setValue(newNickname).addOnFailureListener { exception ->
            dataChangeExceptions.postValue(exception.toString())
        }
    }

    fun addFollowing (followingNumber : Int, followingUsersUid: String) {
        databaseReference.child(uid).child("userData").child("following").setValue(followingNumber)
        databaseReference.child(uid).child("userData").child("followingList").setValue(followingUsersUid)
    }

    fun getData() {
        currentUserDb.child("userData").get().addOnSuccessListener {
            userData.postValue(it.value)

            userStorage.getProfilePicture()

            val dataSnapshot = it.value as HashMap<*, *> //don`t use hashMap

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