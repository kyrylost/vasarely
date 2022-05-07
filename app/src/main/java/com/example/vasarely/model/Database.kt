package com.example.vasarely.model

import android.app.Application
import android.util.Log
import com.example.vasarely.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Database {

    private lateinit var application: Application
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var preferencesReference: DatabaseReference
    private lateinit var currentUser: FirebaseUser
    private lateinit var currentUserDb: DatabaseReference
    lateinit var userMutableLiveData: SingleLiveEvent<Boolean>

    fun initDatabase(application: Application) {
        this.application = application

        firebaseAuth = FirebaseAuth.getInstance()
        userMutableLiveData = SingleLiveEvent()
    }

    fun register(email: String, password: String, username: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            if (firebaseAuth.currentUser != null) {
                userMutableLiveData.postValue(false) //firebaseAuth.currentUser

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
                //userMutableLiveData.postValue(true)

                firebaseDatabase = FirebaseDatabase.getInstance("https://vasarely-f0ed5-default-rtdb.europe-west1.firebasedatabase.app")
                databaseReference = firebaseDatabase.reference.child("profiles")
                currentUser = firebaseAuth.currentUser!!
                currentUserDb = databaseReference.child((currentUser.uid))

                currentUserDb.child("preferences").get().addOnSuccessListener {
                    if (!it.exists()) {
                        userMutableLiveData.postValue(false)
                    }
                    else {
                        userMutableLiveData.postValue(true)
                    }
                }
            }
        }.addOnFailureListener { exception ->
            //dataChangeExceptions.postValue(exception)
        }
    }


    fun logout() {
        firebaseAuth.signOut()
    }

    fun savePreference(byHandSelected: Boolean, computerGraphicsSelected: Boolean,
                       depressedButtonSelected: Boolean, funButtonSelected: Boolean,
                       stillLifeButtonSelected: Boolean, portraitButtonSelected: Boolean,
                       landscapeButtonSelected: Boolean, marineButtonSelected: Boolean,
                       battlePaintingButtonSelected: Boolean, interiorButtonSelected: Boolean,
                       caricatureButtonSelected: Boolean, nudeButtonSelected: Boolean,
                       animeButtonSelected: Boolean, horrorButtonSelected: Boolean) {

        val selectedGenres = mutableListOf<String>()

        preferencesReference = currentUserDb.child("preferences")

        val techniqueReference = preferencesReference.child("technique")
        val moodReference = preferencesReference.child("mood")
        val genresReference = preferencesReference.child("genres")

        if (byHandSelected && computerGraphicsSelected) techniqueReference.setValue("ignore")
        else if (byHandSelected) techniqueReference.setValue("byHand")
        else techniqueReference.setValue("computerGraphics")

        if (depressedButtonSelected) moodReference.setValue("depressed")
        else if (funButtonSelected) moodReference.setValue("fun")
        else moodReference.setValue("ignore")

        if (stillLifeButtonSelected) selectedGenres.add("stillLife")
        if (portraitButtonSelected) selectedGenres.add("portrait")
        if (landscapeButtonSelected) selectedGenres.add("landscape")
        if (marineButtonSelected) selectedGenres.add("marine")
        if (battlePaintingButtonSelected) selectedGenres.add("battlePainting")
        if (interiorButtonSelected) selectedGenres.add("interior")
        if (caricatureButtonSelected) selectedGenres.add("caricature")
        if (nudeButtonSelected) selectedGenres.add("nude")
        if (animeButtonSelected) selectedGenres.add("anime")
        if (horrorButtonSelected) selectedGenres.add("horror")

        genresReference.setValue(selectedGenres)
    }

    fun updateName(newNickname: String){
        currentUserDb.child("username").setValue(newNickname)
    }

    fun getData() {
        currentUserDb.child("preferences").get().addOnSuccessListener {
            Log.d("dataSnapshot", it.toString())
        }

        currentUserDb.child("username").get().addOnSuccessListener {
            Log.d("username", it.toString())
        }
    }
}
