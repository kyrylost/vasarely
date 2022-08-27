package com.example.vasarely.database.root

import com.google.firebase.storage.FirebaseStorage

object StorageInstance {
    val firebaseStore = FirebaseStorage.getInstance()
}