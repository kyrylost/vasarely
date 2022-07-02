package com.example.vasarely.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vasarely.SingleLiveEvent
import com.example.vasarely.model.*
import com.google.firebase.appcheck.internal.util.Logger.TAG
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.log

@OptIn(DelicateCoroutinesApi::class)
class AppViewModel: ViewModel() {
    private var database = Database()

    var userMutableLiveData = database.userMutableLiveData
    //var userData = database.userData

    lateinit var userData: UserData
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
    var foundedUserDataChanged = SingleLiveEvent<Boolean>()


    var postsAmount = 0
    var lines = 0.0
    var lastLinePosts = 0

    var foundedUserLines = 0.0
    var foundedUserLastLinePosts = 0


    fun isLocalDataInitialized() = ::userData.isInitialized

    fun isProfileDataInitialized() = ::userPostsData.isInitialized

    fun isUserDBInitialized() = ::userDB.isInitialized

    fun isProfilePictureInitialized() = userData.profilePictureIsInitialized()

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
            database.userData.observeForever {
                processData(it)
            }

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
        userData.profilePicture = bitmap
    }

    fun saveProfilePictureToLocalDB(bitmap: Bitmap) {
        if (bitmap.byteCount < 50135040) {
            userData.profilePicture = bitmap
        }
        else {
            userData.profilePicture = rotateImage(bitmap, 90F)
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

    private fun processData(data: Any) {
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

        userData = UserData(username, technique, mood, genres,
            followers, following, followingList)
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

                if (userData.moodReference == "ignore") correspondsToPreference += 25
                else if (postMood == userData.moodReference) correspondsToPreference += 25

                for (genre in userData.genreReferences) {
                    if (genre == postsGenre) {
                        correspondsToPreference += 50
                    }
                }

                if (userData.techniqueReference == "ignore") correspondsToPreference += 25
                else if (postTechnique == userData.techniqueReference) correspondsToPreference += 25


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
        val selectedUser = lastFoundedUsersData.usersList[id]

        var followersList = listOf<String>()
        if (selectedUser[5] != "empty")
            followersList = selectedUser[5].split(",")

        foundedUserData = FoundedUserData(selectedUser[0], selectedUser[1], selectedUser[2].toInt(),
            selectedUser[3].toInt(), selectedUser[4].toInt(), followersList)
    }


    fun getOtherUserPosts () {
        database.getOtherUserPosts(foundedUserData.uid, foundedUserData.worksAmount)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun processFoundedUserPhotos(imagesBitmapList: MutableList<Bitmap>) {

        foundedUserData.allFoundedUserPostsData = imagesBitmapList
        foundedUserLines = foundedUserData.worksAmount.toDouble() / 3.0
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


    fun follow() {
        var allowFollowing = true
        for (i in userData.followingList) {
            if (i == foundedUserData.uid) allowFollowing = false
        }

        if (allowFollowing) {
            val newFollowingListString: String
            if (userData.followingList.isEmpty()) {
                newFollowingListString = foundedUserData.uid
                database.addFollowing(userData.following.toInt() + 1, newFollowingListString)
                userData.addNewValue(foundedUserData.uid)
                Log.d("ifEmpty", userData.followingList.toString())
            }
            else {
                userData.addNewValue(foundedUserData.uid)
                newFollowingListString = userData.followingList.joinToString(",")
                database.addFollowing(userData.following.toInt() + 1, newFollowingListString)
                Log.d("ifNotEmpty", userData.followingList.toString())
            }

            if (foundedUserData.followersList.isEmpty()) {
                database.addFollower(foundedUserData.uid, foundedUserData.followers + 1)
            }
            else {
                database.addFollower(foundedUserData.uid, foundedUserData.followers + 1,
                    foundedUserData.followersList.joinToString(","))
            }

//            var followersListString = ""
//            for (uid in foundedUserData.followersList) {
//                if (uid!="" && uid!="empty") followersListString += "$uid,"
//            }

            changeUsersLocalDataAfterFollow()

        }
    }

    private fun changeUsersLocalDataAfterFollow () {
        val currentFollowingNumber = userData.following.toInt()
        userData.following = (currentFollowingNumber + 1).toString()
        //userData.followingList = newFollowingList

        val currentNumberOfFollowers = foundedUserData.followers
        foundedUserData.followers = (currentNumberOfFollowers + 1)
        //change followersList
        foundedUserDataChanged.postValue(true)
    }
}