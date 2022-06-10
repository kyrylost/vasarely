package com.example.vasarely.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import android.util.Log.DEBUG
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.vasarely.SingleLiveEvent
import com.example.vasarely.model.Database
import com.example.vasarely.model.ProfileData
import com.example.vasarely.model.UserData
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
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
    lateinit var profilePicture: SingleLiveEvent<Bitmap>

    lateinit var postsProcessed: SingleLiveEvent<Bitmap>

    lateinit var savingImageFilePath: SavingImageFilePath

    lateinit var userDB: String

    var postsAmount = 0
    var lines = 0.0
    var lastLinePosts = 0


    fun isLocalDataInitialized() = ::localData.isInitialized

    fun isProfileDataInitialized() = ::profileData.isInitialized

    fun isUserDBInitialized() = ::userDB.isInitialized

    fun isProfilePictureInitialized() = localData.profilePictureIsInitialized()

    fun setUserDBStatus() {
        userDB = "initialized"
    }

    private fun asBoolean(int: Int): Boolean {
        return (int == 1)
    }


    private fun rotateImage(source: Bitmap, angle: Float) : Bitmap {
        Log.d("rotateImage", "--------")
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    private fun compressBitmap(bitmap: Bitmap, quality: Int) : Bitmap{
        Log.d("compressBitmap", "--------")
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        val byteArray = stream.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }


    fun initAppViewModel(application: Application) {
        database.initDatabase(application)

        userMutableLiveData = database.userMutableLiveData
        userData = database.userData
        allUserPosts = database.allUserPosts
        recommendationsToProcess = database.recommendationsToProcess
        dataChangeExceptions = database.dataChangeExceptions
        recommendedPost = database.recommendation
        profilePicture = database.profilePicture
        postsProcessed = SingleLiveEvent()
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

    fun profilePhotoToLocalDB(bitmap: Bitmap) {
        localData.profilePicture = bitmap
    }


    fun saveImageFilePath(filePath: Uri) {
        savingImageFilePath = SavingImageFilePath(filePath)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun saveImageAndData(
        byHandSelected: Int, funButtonSelected: Int,
        stillLifeButtonSelected: Int, portraitButtonSelected: Int,
        landscapeButtonSelected: Int, marineButtonSelected: Int,
        battlePaintingButtonSelected: Int, interiorButtonSelected: Int,
        caricatureButtonSelected: Int, nudeButtonSelected: Int,
        animeButtonSelected: Int, horrorButtonSelected: Int,
        description: String) {

        fun checkWhatGenreIsSelected(): String {
            if (asBoolean(stillLifeButtonSelected)) return "stillLife"
            if (asBoolean(portraitButtonSelected)) return "portrait"
            if (asBoolean(landscapeButtonSelected)) return "landscape"
            if (asBoolean(marineButtonSelected)) return "marine"
            if (asBoolean(battlePaintingButtonSelected)) return "battlePainting"
            if (asBoolean(interiorButtonSelected)) return "interior"
            if (asBoolean(caricatureButtonSelected)) return "caricature"
            if (asBoolean(nudeButtonSelected)) return "nude"
            if (asBoolean(animeButtonSelected)) return "anime"
            if (asBoolean(horrorButtonSelected)) return "horror"
            return "null"
        }

        fun checkWhatTechniqueIsSelected(): String {
            return if (asBoolean(byHandSelected)) "byHand"
            else "computerGraphics"
        }

        fun checkWhatMoodIsSelected(): String {
            return if (asBoolean(funButtonSelected)) "fun"
            else "depressed"
        }

        GlobalScope.launch (Dispatchers.IO) {
            val selectedGenre = async { checkWhatGenreIsSelected() }
            val technique = async { checkWhatTechniqueIsSelected() }
            val mood = async { checkWhatMoodIsSelected() }
            database.saveImage(savingImageFilePath.filePath)
            database.saveImageDescription(description)
            database.saveHashtags(technique.await(), mood.await(), selectedGenre.await())
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    fun saveImagesToLocalDB(imagesBitmapList: MutableList<Bitmap>) {

        profileData = ProfileData(imagesBitmapList)
        postsAmount = profileData.postsAmount
        lines = postsAmount / 3.0
        lastLinePosts = ((lines * 10).toInt() % 10) / 3

        GlobalScope.launch (Dispatchers.IO) {
            Log.d("GlobalScope", "Launched")
            for ((index, bitmap) in imagesBitmapList.withIndex()) {
                val compressedBitmap = async { compressBitmap(bitmap, 50) }
                profileData.allUserPostsData[index] = compressedBitmap.await()
                if (bitmap.byteCount < 50135040) {
                    if (index == imagesBitmapList.count() - 1)
                        postsProcessed.postValue(compressedBitmap.await())
                }
                else {
                    val rotatedBitmap = async { rotateImage(compressedBitmap.await(), 90f) }
                    profileData.allUserPostsData[index] = rotatedBitmap.await()

                    if (index == imagesBitmapList.count() - 1)
                        postsProcessed.postValue(rotatedBitmap.await())
                }
            }

            Log.d("processing", "finished")
        }

        Log.d("vm", "end")
    }

    fun saveNewImageToLocalDB(imageBitmap: Bitmap) {
        var imageBitmap = imageBitmap

        imageBitmap = compressBitmap(imageBitmap, 50)
        if (imageBitmap.byteCount < 50135040) {
            Log.d("bytesVM", imageBitmap.byteCount.toString())
        }
        else {
            imageBitmap = rotateImage(imageBitmap, 90f)
        }

        if (isProfileDataInitialized()) {
            profileData.allUserPostsData.add(imageBitmap)
            postsAmount++
            lines = postsAmount / 3.0
            lastLinePosts = ((lines * 10).toInt() % 10) / 3
        }
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
    fun saveProfilePicture(filePath: Uri) {
        database.saveProfilePicture(filePath)
    }
}