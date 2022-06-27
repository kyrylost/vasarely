package com.example.vasarely.model

import android.graphics.Bitmap

data class UserData(var username : String,
                    var techniqueReference : String,
                    var moodReference : String,
                    var genreReferences : List<String>) {

    lateinit var profilePicture: Bitmap

    fun profilePictureIsInitialized() =
        ::profilePicture.isInitialized
}
