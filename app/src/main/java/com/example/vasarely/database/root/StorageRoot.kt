package com.example.vasarely.database.root

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

open class StorageRoot {
    var firebaseStore = FirebaseStorage.getInstance()
    var storageReference = firebaseStore.reference
    lateinit var imageReference: StorageReference
}