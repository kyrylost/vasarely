package com.example.vasarely.database.root

import com.google.firebase.database.FirebaseDatabase

object DatabaseInstance {
    var firebaseDatabase =
        FirebaseDatabase.getInstance(
            "https://vasarely-f0ed5-default-rtdb.europe-west1.firebasedatabase.app"
        )
}