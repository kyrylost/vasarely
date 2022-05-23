package com.example.vasarely.model

import android.graphics.Bitmap

data class ProfileData (var allUserPostsData : List<Bitmap>) {
    val postsAmount = allUserPostsData.count()
}