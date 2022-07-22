package com.example.vasarely.model.user

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.example.vasarely.SingleLiveEvent
import com.example.vasarely.model.root.StorageRoot
import java.io.File

class UserStorage(uid: String): StorageRoot() {

    private val userStorageReference = storageReference.child("uploads/$uid")
    var profilePicture: SingleLiveEvent<Bitmap> = SingleLiveEvent()
    var allUserPosts: SingleLiveEvent<List<Bitmap>> = SingleLiveEvent()

    fun getProfilePicture() {
        val localFileProfilePicture = File.createTempFile("tempImage", "jpg")
        imageReference = userStorageReference.child("profilePhoto/avatar")
        imageReference.getFile(localFileProfilePicture).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFileProfilePicture.absolutePath)
            profilePicture.postValue(bitmap)
            localFileProfilePicture.delete()
        }
    }

    fun retrieveAllUserPosts(amountOfWorks: Int) {
        val allUserPostsList = mutableListOf<Bitmap>()

        for (i in 1..amountOfWorks) {
            Log.d("i", i.toString())
            val localFile = File.createTempFile("tempImage", "jpg")
            imageReference = userStorageReference.child("userPosts/$i")
            imageReference.getFile(localFile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                allUserPostsList.add(bitmap)

                localFile.delete()

                if (allUserPostsList.count() == amountOfWorks)
                    allUserPosts.postValue(allUserPostsList)
            }
        }
    }

    fun saveProfilePicture(filePath: Uri) {
        imageReference = userStorageReference.child("profilePhoto/avatar")
        imageReference.putFile(filePath)
    }

    fun saveImage(filePath : Uri, amountOfWorks: Int) {
        imageReference = userStorageReference.child("userPosts/$amountOfWorks")

        imageReference.putFile(filePath).addOnSuccessListener {
            //
        }
    }
}