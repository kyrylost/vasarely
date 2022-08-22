package com.example.vasarely.database.users

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.vasarely.SingleLiveEvent
import com.example.vasarely.database.root.StorageRoot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class FoundedUserStorage : StorageRoot() {

    var foundedUserPosts: SingleLiveEvent<List<Bitmap>> = SingleLiveEvent()

    fun getOtherUserPosts (uid : String, amountOfWorks : Int) {
        CoroutineScope(Dispatchers.IO).launch {
            kotlin.runCatching {
                val allUserPostsList = mutableListOf<Bitmap>()
                for (i in 1..amountOfWorks) {
                    val localFile = File.createTempFile("tempImage", "jpg")
                    imageReference = storageReference.child("uploads/$uid/userPosts/$i")
                    imageReference.getFile(localFile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                        allUserPostsList.add(bitmap)
                        localFile.delete()
                        if (allUserPostsList.count() == amountOfWorks)
                            foundedUserPosts.postValue(allUserPostsList)
                    }
                }
            }
        }
    }

}