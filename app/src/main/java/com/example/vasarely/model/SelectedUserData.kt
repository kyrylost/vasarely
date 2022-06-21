package com.example.vasarely.model

import android.graphics.Bitmap

data class SelectedUserData(val foundedUserData : List<String>) {
    operator fun get(id: Int): String {
        return foundedUserData[id]
    }

    lateinit var allFoundedUserPostsData : MutableList<Bitmap>
}