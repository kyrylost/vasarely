package com.example.vasarely.database.user

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Looper
import android.util.Log
import com.example.vasarely.SingleLiveEvent
import com.example.vasarely.database.root.StorageRoot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.File

class UserStorage(uid: String): StorageRoot() {

    private val userStorageReference = storageReference.child("uploads/$uid")
    var profilePicture: SingleLiveEvent<Bitmap> = SingleLiveEvent()
    var allUserPosts: SingleLiveEvent<List<Bitmap>> = SingleLiveEvent()

    fun getProfilePicture() {
        CoroutineScope(Dispatchers.IO).launch  {
            imageReference = userStorageReference.child("profilePhoto/avatar")
            kotlin.runCatching {
                Log.d(
                    "getProfilePictureScope",
                    (Looper.myLooper() == Looper.getMainLooper()).toString()
                )
                val localFileProfilePicture = File.createTempFile("tempImage", "jpg")
                imageReference.getFile(localFileProfilePicture).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeFile(localFileProfilePicture.absolutePath)
                    profilePicture.postValue(bitmap)
                    localFileProfilePicture.delete()
                    this.cancel()
                }
            }

        }
    }

    fun retrieveAllUserPosts(amountOfWorks: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            kotlin.runCatching {
                Log.d("retrieveAllUserPosts", (Looper.myLooper() == Looper.getMainLooper()).toString())
                val allUserPostsList = mutableListOf<Bitmap>()

                for (i in 1..amountOfWorks) {
                    Log.d("i", i.toString())
                    val localFile = File.createTempFile("tempImage", "jpg")
                    imageReference = userStorageReference.child("userPosts/$i")
                    imageReference.getFile(localFile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                        allUserPostsList.add(bitmap)

                        localFile.delete()

                        if (allUserPostsList.count() == amountOfWorks) {
                            allUserPosts.postValue(allUserPostsList)
                            this.cancel()
                        }
                    }
                }
            }

        }
    }

    fun saveProfilePicture(filePath: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            imageReference = userStorageReference.child("profilePhoto/avatar")
            imageReference.putFile(filePath).addOnSuccessListener {
                this.cancel()
            }
        }
    }

    fun saveImage(filePath : Uri, amountOfWorks: Int, callback: () -> Unit) {
        Log.d("UserStorage", "saveImage")
        CoroutineScope(Dispatchers.IO).launch {
            imageReference = userStorageReference.child("userPosts/$amountOfWorks")
            imageReference.putFile(filePath).addOnSuccessListener {
                Log.d("UserStorage", "image put")
                callback.invoke()
                this.cancel()
            }
        }
    }
}