package com.example.vasarely.model

import android.graphics.Bitmap

data class UserPostsData (var allUserPostsData : MutableList<Bitmap>) {
    val postsAmount = allUserPostsData.count()
}