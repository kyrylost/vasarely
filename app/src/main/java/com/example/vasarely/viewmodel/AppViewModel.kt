package com.example.vasarely.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vasarely.SingleLiveEvent
import com.example.vasarely.model.Database
import com.example.vasarely.model.ProfileData
import com.example.vasarely.model.UserData
import java.util.*
import kotlin.collections.HashMap

class AppViewModel: ViewModel() {
    private var database = Database()
    lateinit var userMutableLiveData: SingleLiveEvent<Boolean>
    lateinit var userData: SingleLiveEvent<Any>
    lateinit var localData: UserData
    lateinit var profileData: ProfileData
    lateinit var allUserPosts: SingleLiveEvent<List<Bitmap>>
    lateinit var recommendationsToProcess: SingleLiveEvent<List<Map<Int, Any?>>>
    lateinit var recommendedPost: SingleLiveEvent<Bitmap>
    lateinit var dataChangeExceptions: SingleLiveEvent<String>

    lateinit var userDB: String


    fun isLocalDataInitialized() = ::localData.isInitialized

    fun isProfileDataInitialized() = ::profileData.isInitialized

    fun isUserDBInitialized() = ::userDB.isInitialized

    fun setUserDBStatus() {
        userDB = "initialized"
    }

    private fun asBoolean(int: Int): Boolean {
        return (int == 1)
    }


    fun initAppViewModel(application: Application) {
        database.initDatabase(application)

        userMutableLiveData = database.userMutableLiveData
        userData = database.userData
        allUserPosts = database.allUserPosts
        recommendationsToProcess = database.recommendationsToProcess
        dataChangeExceptions = database.dataChangeExceptions
        recommendedPost = database.recommendation

    }

    fun register(email: String, password: String, username: String) =
        database.register(email, password, username)

    fun login(email: String, password: String) = database.login(email, password)

    fun logout() = database.logout()

    fun savePreference(
        byHandSelected: Int, computerGraphicsSelected: Int,
        depressedButtonSelected: Int, funButtonSelected: Int,
        stillLifeButtonSelected: Int, portraitButtonSelected: Int,
        landscapeButtonSelected: Int, marineButtonSelected: Int,
        battlePaintingButtonSelected: Int, interiorButtonSelected: Int,
        caricatureButtonSelected: Int, nudeButtonSelected: Int,
        animeButtonSelected: Int, horrorButtonSelected: Int
    ) {

        val selectedGenres = mutableListOf<String>()

        val technique : String = if (asBoolean(byHandSelected) && asBoolean(computerGraphicsSelected)) "ignore"
        else if (asBoolean(byHandSelected)) "byHand"
        else "computerGraphics"

        val mood : String = if (asBoolean(depressedButtonSelected) && asBoolean(funButtonSelected)) "ignore"
        else if (asBoolean(funButtonSelected)) "fun"
        else if (asBoolean(depressedButtonSelected)) "depressed"
        else "ignore"

        if (asBoolean(stillLifeButtonSelected)) selectedGenres.add("stillLife")
        if (asBoolean(portraitButtonSelected)) selectedGenres.add("portrait")
        if (asBoolean(landscapeButtonSelected)) selectedGenres.add("landscape")
        if (asBoolean(marineButtonSelected)) selectedGenres.add("marine")
        if (asBoolean(battlePaintingButtonSelected)) selectedGenres.add("battlePainting")
        if (asBoolean(interiorButtonSelected)) selectedGenres.add("interior")
        if (asBoolean(caricatureButtonSelected)) selectedGenres.add("caricature")
        if (asBoolean(nudeButtonSelected)) selectedGenres.add("nude")
        if (asBoolean(animeButtonSelected)) selectedGenres.add("anime")
        if (asBoolean(horrorButtonSelected)) selectedGenres.add("horror")

        database.savePreference(
            technique, mood, selectedGenres
        )

    }

    fun updateName(newNickname: String) {
        database.updateName(newNickname)
    }

    fun getData() {
        database.getData()
    }

    fun processData(data: Any) {
        val username: String
        val technique: String
        val mood: String
        val genres = mutableListOf<String>()

        val dataHashMap = data as HashMap<*, *>

        val preferenceHashMap = dataHashMap["preferences"] as HashMap<*, *>

        username = dataHashMap["username"].toString()
        technique = preferenceHashMap["technique"].toString()
        mood = preferenceHashMap["mood"].toString()
        val genresArrayList = preferenceHashMap["genres"] as ArrayList<*>
        for (value in genresArrayList) {
            genres.add(value as String)
        }

        localData = UserData(username, technique, mood, genres)
    }

    fun saveImage(filePath: Uri) {
        database.saveImage(filePath)
    }

    fun saveImageDescription(description: String) {
        database.saveImageDescription(description)
    }

    fun saveImageData(
        byHandSelected: Int ,
        depressedButtonSelected: Int, funButtonSelected: Int,
        stillLifeButtonSelected: Int, portraitButtonSelected: Int,
        landscapeButtonSelected: Int, marineButtonSelected: Int,
        battlePaintingButtonSelected: Int, interiorButtonSelected: Int,
        caricatureButtonSelected: Int, nudeButtonSelected: Int,
        animeButtonSelected: Int, horrorButtonSelected: Int) {

        database.saveHashtags(
            asBoolean(byHandSelected),
            asBoolean(depressedButtonSelected), asBoolean(funButtonSelected),
            asBoolean(stillLifeButtonSelected), asBoolean(portraitButtonSelected),
            asBoolean(landscapeButtonSelected), asBoolean(marineButtonSelected),
            asBoolean(battlePaintingButtonSelected), asBoolean(interiorButtonSelected),
            asBoolean(caricatureButtonSelected), asBoolean(nudeButtonSelected),
            asBoolean(animeButtonSelected), asBoolean(horrorButtonSelected)
        )
    }

    fun saveImagesToLocalDB(imagesBitmapList: List<Bitmap>) {
        profileData = ProfileData(imagesBitmapList)
    }

    fun findPostsToRecommend(postsData : List<Map<Int, Any?>>) {
        var correspondsToPreference = 0
        for (singlePostData in postsData) {
            for (singlePostDataMap in singlePostData) {
                val singlePostDataMapValue = singlePostDataMap.value as List<*>
                val postMood = singlePostDataMapValue[0]
                val postsGenre = singlePostDataMapValue[1]
                val postTechnique = singlePostDataMapValue[2]

                if (localData.moodReference == "ignore") correspondsToPreference += 25
                else if (postMood == localData.moodReference) correspondsToPreference += 25

                for (genre in localData.genreReferences) {
                    if (genre == postsGenre) {
                        correspondsToPreference += 50
                    }
                }

                if (localData.techniqueReference == "ignore") correspondsToPreference += 25
                else if (postTechnique == localData.techniqueReference) correspondsToPreference += 25


                if (correspondsToPreference >= 50) {database.getImage(singlePostDataMapValue[4] as String,
                    singlePostDataMap.key.toString())
                Log.d("correspondsToPreference", "true")
                }
            }

        }
    }
    fun recommendationsSearch() {
        database.recommendationsSearch()
    }
}