package com.example.vasarely.model

import android.app.Application
import android.net.Uri
import android.util.Log
import com.example.vasarely.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Database {

    private lateinit var application: Application
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var preferencesReference: DatabaseReference
    private lateinit var currentUser: FirebaseUser
    private lateinit var currentUserDb: DatabaseReference

    private lateinit var firebaseStore: FirebaseStorage
    private lateinit var storageReference: StorageReference
    //private lateinit var uploadsReference: StorageReference
    private lateinit var imageReference: StorageReference

    private var amountOfWorks = 999
    private lateinit var uid: String

    lateinit var userMutableLiveData: SingleLiveEvent<Boolean>
    lateinit var userData: SingleLiveEvent<Any>


    fun initDatabase(application: Application) {
        this.application = application

        firebaseAuth = FirebaseAuth.getInstance()

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = firebaseStore.reference
        //uploadsReference = storageReference.child("uploads/")


        userMutableLiveData = SingleLiveEvent()
        userData = SingleLiveEvent()
    }

    fun register(email: String, password: String, username: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            if (firebaseAuth.currentUser != null) {
                userMutableLiveData.postValue(false)

                firebaseDatabase = FirebaseDatabase.getInstance("https://vasarely-f0ed5-default-rtdb.europe-west1.firebasedatabase.app")
                databaseReference = firebaseDatabase.reference.child("profiles")
                currentUser = firebaseAuth.currentUser!!
                currentUserDb = databaseReference.child((currentUser.uid))
                uid = currentUser.uid
                currentUserDb.child("userData").child("username").setValue(username)
                currentUserDb.child("userData").child("worksAmount").setValue(-1)
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
                uid = currentUser.uid

                //currentUserDb.child("userData").child("worksAmount").setValue(-1)

                currentUserDb.child("userData").child("preferences").get().addOnSuccessListener {
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

        preferencesReference = currentUserDb.child("userData").child("preferences")

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

    fun updateName(newNickname: String) {
        currentUserDb.child("username").setValue(newNickname)
    }

    fun getData() {
        currentUserDb.child("userData").get().addOnSuccessListener {
            Log.d("dataSnapshot", it.toString())
            userData.postValue(it.value)

            val dataSnapshot = it.value as HashMap<*, *>
            Log.d("check" ,dataSnapshot["worksAmount"].toString())
            amountOfWorks = dataSnapshot["worksAmount"].toString().toInt()
        }
    }

    fun saveImage(filePath : Uri) {
        amountOfWorks += 1

        imageReference = storageReference.child("uploads/$uid/$amountOfWorks")

        currentUserDb.child("userData").child("worksAmount").setValue(amountOfWorks)

        imageReference.putFile(filePath)
    }

    fun saveHashtags() {

    }

}
