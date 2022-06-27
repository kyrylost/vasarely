package com.example.vasarely.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vasarely.SingleLiveEvent
import com.example.vasarely.model.*
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.HashMap

@OptIn(DelicateCoroutinesApi::class)
class AppViewModel: ViewModel() {
    private var database = Database()

    var userMutableLiveData = database.userMutableLiveData
    var userData = database.userData

    lateinit var localData: UserData
    lateinit var userPostsData: UserPostsData

    var userPostsFound = SingleLiveEvent<Boolean>()
    var postsProcessed = SingleLiveEvent<Boolean>()

    var recommendedPost = database.recommendation
    var dataChangeExceptions = database.dataChangeExceptions
    var profilePicture = database.profilePicture

    lateinit var savingImageFilePath: SavingImageFilePath

    lateinit var userDB: String

    var foundedUser = database.foundedUser
    lateinit var lastFoundedUsersData: LastFoundedUsersData
    lateinit var foundedUserData: FoundedUserData
    var foundedUserPosts = database.foundedUserPosts
    var foundedUserPostProcessed = SingleLiveEvent<Boolean>()


    var postsAmount = 0
    var lines = 0.0
    var lastLinePosts = 0

    var foundedUserLines = 0.0
    var foundedUserLastLinePosts = 0


    fun isLocalDataInitialized() = ::localData.isInitialized

    fun isProfileDataInitialized() = ::userPostsData.isInitialized

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


    init {
        viewModelScope.launch {
            database.localDbCopyLiveEvent.observeForever {
                Log.d("scope", "VM")
                val recommendationsToProcessList = mutableListOf<Map<Int, Any?>>()
                for (snapshot in it.children) {
                    if (snapshot.key != database.uid) {
                        val postsData = mutableMapOf<Int, Any?>()
                        for ((dataIndex, data) in snapshot.children.withIndex()) {
                            if (dataIndex == 0) {
                                for (post in data.children) {
                                    for (postNumberAndTags in post.children) {
                                        val postNumberAndTagsHashMap = postNumberAndTags.value as HashMap<*, *>
                                        val postTagsList = postNumberAndTagsHashMap.values
                                        val tags = mutableListOf<String>()
                                        for ((jIndex, j) in postTagsList.withIndex()) {
                                            if (jIndex == 0) {
                                                j as HashMap<*, *>
                                                tags.add(j["mood"].toString())
                                                tags.add(j["genre"].toString())
                                                tags.add(j["technique"].toString())
                                            }
                                            else {
                                                tags.add(j as String)
                                                tags.add(snapshot.key as String)
                                            }
                                        }
                                        postsData[postNumberAndTags.key!!.toInt()] = tags
                                    }
                                }
                            }

                            else {
                                for (child in data.children) {
                                    if (child.key == "worksAmount" && child.value != 0L) {
                                        recommendationsToProcessList.add(postsData)
                                        Log.d("Posts", postsData.toString())
                                    }
                                }
                            }
                        }
                    }
                }
                findPostsToRecommend(recommendationsToProcessList)
            }
        }

        database.allUserPosts.observeForever {
            Log.d("it", it.count().toString())

            userPostsFound.postValue(true)

            val imagesBitmapList = it as MutableList

            userPostsData = UserPostsData(imagesBitmapList)
            postsAmount = userPostsData.postsAmount
            Log.d("postsAmount", postsAmount.toString())
            lines = postsAmount / 3.0
            lastLinePosts = ((lines * 10).toInt() % 10) / 3

            GlobalScope.launch (Dispatchers.IO) {
                for ((index, bitmap) in imagesBitmapList.withIndex()) {
                    val compressedBitmap = async { compressBitmap(bitmap, 50) }
                    userPostsData.allUserPostsData[index] = compressedBitmap.await()
                    if (bitmap.byteCount < 50135040) {
                        if (index == imagesBitmapList.count() - 1)
                            postsProcessed.postValue(true)
                    }
                    else {
                        val rotatedBitmap = async { rotateImage(compressedBitmap.await(), 90f) }
                        userPostsData.allUserPostsData[index] = rotatedBitmap.await()

                        if (index == imagesBitmapList.count() - 1)
                            postsProcessed.postValue(true) //*????????????????????????????????
                    }
                }
            }
        }
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


    fun saveProfilePicture() {
        database.saveProfilePicture(savingImageFilePath.filePath)
    }

    fun saveAddedProfilePictureToLocalDB(bitmap: Bitmap) {
        localData.profilePicture = bitmap
    }

    fun saveProfilePictureToLocalDB(bitmap: Bitmap) {
        if (bitmap.byteCount < 50135040) {
            localData.profilePicture = bitmap
        }
        else {
            localData.profilePicture = rotateImage(bitmap, 90F)
        }
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

    fun saveNewImageToLocalDB(imageBitmap: Bitmap) {
        var imageBitmap = imageBitmap

        imageBitmap = compressBitmap(imageBitmap, 50)
        if (imageBitmap.byteCount >= 50135040)
            imageBitmap = rotateImage(imageBitmap, 90f)

        if (isProfileDataInitialized()) {
            userPostsData.allUserPostsData.add(imageBitmap)
            postsAmount++
            lines = postsAmount / 3.0
            lastLinePosts = ((lines * 10).toInt() % 10) / 3
        }

        else {
            val bitmapList = mutableListOf<Bitmap>()
            bitmapList.add(imageBitmap)
            userPostsData = UserPostsData(bitmapList)
            postsAmount++
            lines = postsAmount / 3.0
            lastLinePosts = ((lines * 10).toInt() % 10) / 3
        }
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


    fun databaseRecommendationsSearch() {
        database.recommendationsSearch()
    }


    private fun findPostsToRecommend(postsData : List<Map<Int, Any?>>) {
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



    fun findByUsername(name: String) {
        database.findByUsername(name.trimStart().trimEnd())
    }

    fun saveLastFoundedUsersData(usersList: List<List<String>>) {
        lastFoundedUsersData = LastFoundedUsersData(usersList)
    }

    fun saveFoundedUser(id : Int) {
        foundedUserData = FoundedUserData(lastFoundedUsersData.usersList[id])
    }


    fun getOtherUserPosts () {
        database.getOtherUserPosts(foundedUserData[0], foundedUserData[2].toInt())
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun processFoundedUserPhotos(imagesBitmapList: MutableList<Bitmap>) {

        foundedUserData.allFoundedUserPostsData = imagesBitmapList
        foundedUserLines = foundedUserData[2].toDouble() / 3.0
        foundedUserLastLinePosts = ((foundedUserLines * 10).toInt() % 10) / 3

        GlobalScope.launch (Dispatchers.IO) {
            for ((index, bitmap) in imagesBitmapList.withIndex()) {
                val compressedBitmap = async { compressBitmap(bitmap, 50) }
                foundedUserData.allFoundedUserPostsData[index] = compressedBitmap.await()
                if (bitmap.byteCount < 50135040) {
                    if (index == imagesBitmapList.count() - 1)
                        foundedUserPostProcessed.postValue(true)
                }
                else {
                    val rotatedBitmap = async { rotateImage(compressedBitmap.await(), 90f) }
                    foundedUserData.allFoundedUserPostsData[index] = rotatedBitmap.await()

                    if (index == imagesBitmapList.count() - 1)
                        foundedUserPostProcessed.postValue(true)
                }
            }
        }
    }


    fun addFollower() {
        database.addFollower(foundedUserData[0], foundedUserData[4])
    }

    fun addFollowing() {
        //database.addFollowing(profileData.followingNumber)
    }
}