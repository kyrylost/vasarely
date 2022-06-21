package com.example.vasarely.model

import android.graphics.Bitmap

data class LastFoundedUsersData(var usersList: List<List<String>>) {
    lateinit var worksBitmapList: List<List<Bitmap>>
}