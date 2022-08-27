package com.example.vasarely.database.root

import com.google.firebase.storage.StorageReference

open class StorageRoot {
    var storageReference = StorageInstance.firebaseStore.reference
    lateinit var imageReference: StorageReference
}