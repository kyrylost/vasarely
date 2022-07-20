package com.example.vasarely.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vasarely.SingleLiveEvent
import com.example.vasarely.model.UsersDatabase
import com.example.vasarely.repository.FoundedUserData
import com.example.vasarely.repository.LastFoundedUsersData
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream

class UsersViewModel : ViewModel() {

    var usersDatabase = UsersDatabase()

    var dataChangeExceptions = usersDatabase.dataChangeExceptions
//    var recommendedPost = usersDatabase.recommendation

    var foundedUser = usersDatabase.foundedUser
    lateinit var lastFoundedUsersData: LastFoundedUsersData
    lateinit var foundedUserData: FoundedUserData
    var foundedUserPosts = usersDatabase.foundedUserPosts
    var foundedUserPostProcessed = SingleLiveEvent<Boolean>()
    var foundedUserDataChanged = SingleLiveEvent<Boolean>()

    var foundedUserLines = 0.0
    var foundedUserLastLinePosts = 0

    private fun rotateImage(source: Bitmap, angle: Float) : Bitmap {
        Log.d("rotateImage", "--------")
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    private fun compressBitmap(bitmap: Bitmap, quality: Int) : Bitmap {
        Log.d("compressBitmap", "--------")
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        val byteArray = stream.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }


    fun retrieveAllData() { // retrievingAllData  (go to init?)
        usersDatabase.retrieveAllData()
    }

    suspend fun findByUsername(name: String) {
        usersDatabase.findByUsername(name.trimStart().trimEnd())
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
        usersDatabase.getOtherUserPosts(foundedUserData.uid, foundedUserData.worksAmount)
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

}