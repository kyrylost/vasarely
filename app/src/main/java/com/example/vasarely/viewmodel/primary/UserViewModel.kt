package com.example.vasarely.viewmodel.primary

import android.graphics.Bitmap
import android.net.Uri
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vasarely.SingleLiveEvent
import com.example.vasarely.database.user.UserDatabase
import com.example.vasarely.model.user.UserData
import com.example.vasarely.model.user.UserPostsData
import com.example.vasarely.viewmodel.secondary.ImageFilePath
import com.example.vasarely.viewmodel.secondary.ImageInterface
import kotlinx.coroutines.*

@OptIn(DelicateCoroutinesApi::class)
class UserViewModel : ViewModel(), ImageInterface {

    var userDatabase = UserDatabase()

    var dataChangeExceptions = userDatabase.dataChangeExceptions

    var userMutableLiveData = userDatabase.userMutableLiveData

    var userPostsFound = SingleLiveEvent<Boolean>()
    var postsProcessed = SingleLiveEvent<Boolean>()

    lateinit var userData: UserData
    lateinit var userPostsData: UserPostsData
    private var userPostsProcessed = false
    var profilePicture = SingleLiveEvent<Bitmap>()

    private var postsAmount = 0

    private lateinit var imageFilePath: ImageFilePath

    lateinit var userDB: String

    fun isUserDataInitialized() = ::userData.isInitialized
    fun isUserPostsDataInitializedAndProcessed(): Boolean {
        return if (userPostsProcessed)
            ::userPostsData.isInitialized
        else false
    }

    fun isProfilePictureInitialized() = userData.profilePictureIsInitialized()
    fun isUserDBInitialized() = ::userDB.isInitialized

    fun setUserDBStatus() {
        userDB = "initialized"
    }

    private fun asBoolean(int: Int): Boolean {
        return int == 1
    }


    init {
        viewModelScope.launch {
            userDatabase.userDataFound.observeForever { userData ->
                processData(userData)
            }

            userDatabase.userStorageInitialized.observeForever {
                userDatabase.allUserPosts.observeForever {
                    CoroutineScope(Dispatchers.IO).launch {
                        Log.d("it", it.count().toString())

                        userPostsFound.postValue(true)

                        val imagesBitmapList = it as MutableList

                        userPostsData = UserPostsData(imagesBitmapList)
                        postsAmount = userPostsData.postsAmount
                        Log.d("postsAmount", postsAmount.toString())

                        Log.d(
                            "GlobalScope",
                            (Looper.myLooper() == Looper.getMainLooper()).toString()
                        )
                        for ((index, bitmap) in imagesBitmapList.withIndex()) {
                            val compressedBitmap = async { compressBitmap(bitmap, 50) }
                            userPostsData.allUserPostsData[index] = compressedBitmap.await()
                            Log.d("-----bytes-----", bitmap.byteCount.toString())

                            if (bitmap.byteCount < 50135040) {
                                Log.d("-----bytes2-----", bitmap.byteCount.toString())
                                if (index == imagesBitmapList.count() - 1) {
                                    postsProcessed.postValue(true)
                                    userPostsProcessed = true
                                }
                            } else {
                                val rotatedBitmap =
                                    async { rotateImage(compressedBitmap.await(), 90f) }
                                userPostsData.allUserPostsData[index] = rotatedBitmap.await()

                                if (index == imagesBitmapList.count() - 1) {
                                    postsProcessed.postValue(true)
                                    userPostsProcessed = true
                                }
                            }
                        }
                        this.cancel()
                    }
                }

                profilePicture = userDatabase.profilePicture
            }
        }

    }


    private fun processData(data: Any) {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDatabase.uid
            val username: String
            val technique: String
            val mood: String
            val followers: String
            val following: String
            var followingList = listOf<String>()
            val genres = mutableListOf<String>()

            val dataHashMap = data as HashMap<*, *>

            val preferenceHashMap = dataHashMap["preferences"] as HashMap<*, *>

            username = dataHashMap["username"].toString()
            technique = preferenceHashMap["technique"].toString()
            mood = preferenceHashMap["mood"].toString()
            followers = dataHashMap["followers"].toString()
            following = dataHashMap["following"].toString()
            if (dataHashMap["followingList"].toString() != "empty")
                followingList = dataHashMap["followingList"].toString().split(",")
            Log.d("followingList", followingList.toString())

            val genresArrayList = preferenceHashMap["genres"] as ArrayList<*>
            for (value in genresArrayList) {
                genres.add(value as String)
            }

            userData = UserData(
                uid, username, technique, mood, genres,
                followers, following, followingList
            )

            this.cancel()
        }
    }


    fun register(email: String, password: String, username: String) =
        userDatabase.register(email, password, username)

    fun login(email: String, password: String) =
        userDatabase.login(email, password)

    fun logout() = userDatabase.logout()

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

        val technique: String =
            if (asBoolean(byHandSelected) && asBoolean(computerGraphicsSelected)) "ignore"
            else if (asBoolean(byHandSelected)) "byHand"
            else "computerGraphics"

        val mood: String =
            if (asBoolean(depressedButtonSelected) && asBoolean(funButtonSelected)) "ignore"
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

        userDatabase.savePreference(
            technique, mood, selectedGenres
        )
    }

    fun saveProfilePicture() {
        userDatabase.saveProfilePicture(imageFilePath.filePath)
    }

    fun saveAddedProfilePictureToLocalDB(bitmap: Bitmap) {
        userData.profilePicture = bitmap
    }

    fun saveProfilePictureToLocalDB(bitmap: Bitmap) {
        if (bitmap.byteCount < 50135040) {
            userData.profilePicture = bitmap
        } else {
            userData.profilePicture = rotateImage(bitmap, 90F)
        }
    }


    fun saveImageFilePath(filePath: Uri) {
        imageFilePath = ImageFilePath(filePath)
    }

    fun saveImageAndData(
        byHandSelected: Int, funButtonSelected: Int,
        stillLifeButtonSelected: Int, portraitButtonSelected: Int,
        landscapeButtonSelected: Int, marineButtonSelected: Int,
        battlePaintingButtonSelected: Int, interiorButtonSelected: Int,
        caricatureButtonSelected: Int, nudeButtonSelected: Int,
        animeButtonSelected: Int, horrorButtonSelected: Int,
        description: String
    ) {

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

        CoroutineScope(Dispatchers.IO).launch {
            val selectedGenre = async { checkWhatGenreIsSelected() }
            val technique = async { checkWhatTechniqueIsSelected() }
            val mood = async { checkWhatMoodIsSelected() }
            userDatabase.saveImage(imageFilePath.filePath)
            userDatabase.saveImageDescription(description)
            userDatabase.saveHashtags(
                technique.await(),
                mood.await(),
                selectedGenre.await()
            ).let {
                this.cancel()
            }
        }

    }

    fun saveNewImageToLocalDB(bitmap: Bitmap) {
        CoroutineScope(Dispatchers.IO).launch {
            var imageBitmap = bitmap

            imageBitmap = compressBitmap(imageBitmap, 50)
            if (imageBitmap.byteCount >= 50135040)
                imageBitmap = rotateImage(imageBitmap, 90f)

            if (isUserDataInitialized()) {
                userPostsData.allUserPostsData.add(imageBitmap)
                postsAmount++
            } else {
                val bitmapList = mutableListOf<Bitmap>()
                bitmapList.add(imageBitmap)
                userPostsData = UserPostsData(bitmapList)
                postsAmount++
            }
            this.cancel()
        }
    }


    fun updateName(newNickname: String) {
        userDatabase.updateName(newNickname)
    }

    fun getData() {
        GlobalScope.launch {
            userDatabase.getData()
        }
    }
}