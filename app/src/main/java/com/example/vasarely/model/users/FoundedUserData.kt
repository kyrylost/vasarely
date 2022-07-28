package com.example.vasarely.model.users

import android.graphics.Bitmap

data class FoundedUserData(var uid : String, var username : String,
                           var worksAmount : Int, var followers : Int,
                           var following : Int, var followersList: List<String>) {

    lateinit var allFoundedUserPostsData : MutableList<Bitmap>

    fun getFollowers () : String {
        return followers.toString()
    }

    fun getFollowing () : String {
        return following.toString()
    }
}