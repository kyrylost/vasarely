package com.example.vasarely.model

import android.graphics.Bitmap

data class FoundedUserData(val foundedUserData : List<String>) {

    lateinit var allFoundedUserPostsData : MutableList<Bitmap>

    operator fun get(id: Int): String {
        return foundedUserData[id]
    }
}