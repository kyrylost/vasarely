package com.example.vasarely.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Database {

    private lateinit var application: Application
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var currentUser: FirebaseUser
    private lateinit var currentUserDb: DatabaseReference
    lateinit var userMutableLiveData: MutableLiveData<FirebaseUser>

    fun initDatabase(application: Application) {
        this.application = application

        firebaseAuth = FirebaseAuth.getInstance()
        userMutableLiveData = MutableLiveData<FirebaseUser>()
    }

    fun register(email: String, password: String, username: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            if (firebaseAuth.currentUser != null) {
                userMutableLiveData.postValue(firebaseAuth.currentUser)

                firebaseDatabase = FirebaseDatabase.getInstance("https://vasarely-f0ed5-default-rtdb.europe-west1.firebasedatabase.app")
                databaseReference = firebaseDatabase.reference.child("profiles")
                currentUser = firebaseAuth.currentUser!!
                currentUserDb = databaseReference.child((currentUser.uid))
                currentUserDb.child("username").setValue(username)
            }

        }.addOnFailureListener { exception ->
            //dataChangeExceptions.postValue(exception)
        }
    }

    fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener{
            if(firebaseAuth.currentUser != null) {
                userMutableLiveData.postValue(firebaseAuth.currentUser)

                firebaseDatabase = FirebaseDatabase.getInstance("https://vasarely-f0ed5-default-rtdb.europe-west1.firebasedatabase.app")
                databaseReference = firebaseDatabase.reference.child("profiles")
                currentUser = firebaseAuth.currentUser!!
                currentUserDb = databaseReference.child((currentUser.uid))
            }
        }.addOnFailureListener { exception ->
            //dataChangeExceptions.postValue(exception)
        }
    }

}