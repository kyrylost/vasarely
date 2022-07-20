package com.example.vasarely.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.vasarely.SingleLiveEvent
import java.io.File

class RecommendationsStorage  : StorageRoot() {
    var recommendation: SingleLiveEvent<Bitmap> = SingleLiveEvent()

    fun getImage(userUid : String, postNumber : Int) {
        val localFile = File.createTempFile("tempImage", "jpg")
        //val p = postNumber.toInt()
        imageReference = storageReference.child("uploads/$userUid/userPosts/$postNumber")
        imageReference.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            recommendation.postValue(bitmap)
            localFile.delete()
        }
    }
}