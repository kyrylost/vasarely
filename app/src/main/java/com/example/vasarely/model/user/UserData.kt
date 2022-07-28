package com.example.vasarely.model.user

import android.graphics.Bitmap

data class UserData(var uid : String, var username : String, var techniqueReference : String,
                    var moodReference : String, var genreReferences : List<String>,
                    var followers : String, var following : String,
                    var followingList: List<String>) {

    lateinit var profilePicture: Bitmap

    fun profilePictureIsInitialized() =
        ::profilePicture.isInitialized

    fun addNewValue(newValue: String) {
        followingList += newValue
    }
}
