package com.example.vasarely.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.example.vasarely.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import kotlin.collections.HashMap

class Database {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var currentUser: FirebaseUser
    private lateinit var currentUserDb: DatabaseReference
    private lateinit var preferencesReference: DatabaseReference

    private var firebaseStore: FirebaseStorage = FirebaseStorage.getInstance()
    private var storageReference: StorageReference = firebaseStore.reference
    private lateinit var imageReference: StorageReference

    var userMutableLiveData: SingleLiveEvent<Boolean> = SingleLiveEvent()

    var userData: SingleLiveEvent<Any> = SingleLiveEvent()
    var allUserPosts: SingleLiveEvent<List<Bitmap>> = SingleLiveEvent()
    var profilePicture: SingleLiveEvent<Bitmap> = SingleLiveEvent()

    var localDbCopyLiveEvent: SingleLiveEvent<DataSnapshot> = SingleLiveEvent()
    var recommendation: SingleLiveEvent<Bitmap> = SingleLiveEvent()

    var foundedUser: SingleLiveEvent<MutableList<List<String>>> = SingleLiveEvent()
    var foundedUserPosts: SingleLiveEvent<List<Bitmap>> = SingleLiveEvent()

    var dataChangeExceptions: SingleLiveEvent<String> = SingleLiveEvent()

    lateinit var localDbCopy: LocalDbCopy

    lateinit var uid: String
    private var amountOfWorks = 0


    fun register(email: String, password: String, username: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            if (firebaseAuth.currentUser != null) {
                userMutableLiveData.postValue(false)

                firebaseDatabase = FirebaseDatabase.getInstance("https://vasarely-f0ed5-default-rtdb.europe-west1.firebasedatabase.app")
                databaseReference = firebaseDatabase.reference.child("profiles")
                currentUser = firebaseAuth.currentUser!!
                uid = currentUser.uid
                currentUserDb = databaseReference.child((uid))

                currentUserDb.child("userData").child("followers").setValue(0)
                currentUserDb.child("userData").child("following").setValue(0)
                currentUserDb.child("userData").child("worksAmount").setValue(0)
                currentUserDb.child("userData").child("username").setValue(username)
                currentUserDb.child("profileData").child("posts").setValue(1)
            }
        }.addOnFailureListener { exception ->
            dataChangeExceptions.postValue(exception.toString())
        }
    }

    fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener{
            if(firebaseAuth.currentUser != null) {

                firebaseDatabase = FirebaseDatabase.getInstance("https://vasarely-f0ed5-default-rtdb.europe-west1.firebasedatabase.app")
                databaseReference = firebaseDatabase.reference.child("profiles")
                currentUser = firebaseAuth.currentUser!!
                uid = currentUser.uid
                currentUserDb = databaseReference.child((uid))

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
            dataChangeExceptions.postValue(exception.toString())
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }


    fun savePreference(technique : String, mood : String, selectedGenres : List<String>) {
        preferencesReference = currentUserDb.child("userData").child("preferences")

        val techniqueReference = preferencesReference.child("technique")
        val moodReference = preferencesReference.child("mood")
        val genresReference = preferencesReference.child("genres")

        techniqueReference.setValue(technique).addOnFailureListener { exception ->
            dataChangeExceptions.postValue(exception.toString())
        }

        moodReference.setValue(mood).addOnFailureListener { exception ->
            dataChangeExceptions.postValue(exception.toString())
        }

        genresReference.setValue(selectedGenres).addOnFailureListener { exception ->
            dataChangeExceptions.postValue(exception.toString())
        }
    }

    fun saveProfilePicture(filePath: Uri) {
        imageReference = storageReference.child("uploads/$uid/profilePhoto/avatar")
        imageReference.putFile(filePath)
    }

    fun saveImage(filePath : Uri) {
        amountOfWorks += 1
        imageReference = storageReference.child("uploads/$uid/userPosts/$amountOfWorks")

        imageReference.putFile(filePath).addOnSuccessListener {
            currentUserDb.child("userData").child("worksAmount").setValue(amountOfWorks)
        }
    }

    fun saveImageDescription(description: String) {
        currentUserDb.child("profileData").child("posts")
            .child("$amountOfWorks").child("description").setValue(description)
    }

    fun saveHashtags(technique : String, mood : String, genre : String) {
        currentUserDb.child("profileData").child("posts")
            .child("$amountOfWorks").child("hashtags")
            .child("mood").setValue(mood)

        currentUserDb.child("profileData").child("posts")
            .child("$amountOfWorks").child("hashtags")
            .child("technique").setValue(technique)

        currentUserDb.child("profileData").child("posts")
            .child("$amountOfWorks").child("hashtags")
            .child("genre").setValue(genre)
    }

    fun updateName(newNickname: String) {
        currentUserDb.child("userData").child("username").setValue(newNickname).addOnFailureListener { exception ->
            dataChangeExceptions.postValue(exception.toString())
        }
    }


    fun getData() {
        currentUserDb.child("userData").get().addOnSuccessListener {
            userData.postValue(it.value)

            val localFileProfilePicture = File.createTempFile("tempImage", "jpg")
            imageReference = storageReference.child("uploads/$uid/profilePhoto/avatar")
            imageReference.getFile(localFileProfilePicture).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFileProfilePicture.absolutePath)
                profilePicture.postValue(bitmap)
                localFileProfilePicture.delete()
            }

            val dataSnapshot = it.value as HashMap<*, *>

            amountOfWorks = if (dataSnapshot["worksAmount"] != null)
                dataSnapshot["worksAmount"].toString().toInt()
            else
                0

            val allUserPostsList = mutableListOf<Bitmap>()

            for (i in 1..amountOfWorks) {
                Log.d("i", i.toString())
                val localFile = File.createTempFile("tempImage", "jpg")
                imageReference = storageReference.child("uploads/$uid/userPosts/$i")
                imageReference.getFile(localFile).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                    allUserPostsList.add(bitmap)

                    localFile.delete()

                    if (allUserPostsList.count() == amountOfWorks)
                        allUserPosts.postValue(allUserPostsList)

                }
            }

        }.addOnFailureListener { exception ->
            dataChangeExceptions.postValue(exception.toString())
            getData()
        }

    }


    fun recommendationsSearch() {
        databaseReference.get().addOnSuccessListener {
            localDbCopy = LocalDbCopy(it)
            localDbCopyLiveEvent.postValue(localDbCopy.allData)

        } .addOnFailureListener { exception ->
            dataChangeExceptions.postValue(exception.toString())
        }
    }

    fun getImage(uid : String, postNumber : String) {
        val localFile = File.createTempFile("tempImage", "jpg")
        val p = postNumber.toInt()
        imageReference = storageReference.child("uploads/$uid/userPosts/$p")
        imageReference.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            recommendation.postValue(bitmap)
            localFile.delete()
        }
    }


    fun findByUsername(name: String) {
        val foundedUsersData = mutableListOf<List<String>>()

        for (userData in localDbCopy.allData.children) {
            val username = userData.child("userData").child("username").value.toString()

            val nameLength = name.count()

            if (username.lowercase() == name.lowercase()) {
                val foundedUserData = mutableListOf<String>()
                foundedUserData.add(userData.key.toString())
                foundedUserData.add(username)
                foundedUserData.add(userData.child("userData").child("worksAmount").value.toString())
                foundedUserData.add(userData.child("userData").child("followers").value.toString())
                foundedUserData.add(userData.child("userData").child("following").value.toString())
                foundedUsersData.addAll(0, listOf(foundedUserData))
                //foundedUser.postValue(foundedUsersData)
                Log.d("yes", userData.key.toString())
            }

            if (nameLength < username.count()) {
                val usernameFirstN = (username.subSequence(0, nameLength)).toString()

                if(usernameFirstN.lowercase() == name.lowercase()) {
                    val foundedUserData = mutableListOf<String>()
                    foundedUserData.add(userData.key.toString())
                    foundedUserData.add(username)
                    foundedUserData.add(userData.child("userData").child("worksAmount").value.toString())
                    foundedUserData.add(userData.child("userData").child("followers").value.toString())
                    foundedUserData.add(userData.child("userData").child("following").value.toString())
                    foundedUsersData.add(foundedUserData)
                }
            }

        }
        foundedUser.postValue(foundedUsersData)
    }

    fun getOtherUserPosts (uid : String, amountOfWorks : Int) {
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

    fun addFollower (uid : String, followersNumber : String) {
        databaseReference.child(uid).child("userData").child(followersNumber)
    }
}
