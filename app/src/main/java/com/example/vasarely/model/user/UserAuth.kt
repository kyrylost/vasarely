package com.example.vasarely.model.user

import androidx.lifecycle.MutableLiveData
import com.example.vasarely.SingleLiveEvent
import com.example.vasarely.model.root.DatabaseRoot
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference

open class UserAuth : DatabaseRoot() {

    private var firebaseAuth = FirebaseAuth.getInstance()

    private lateinit var currentUser: FirebaseUser
    lateinit var currentUserDb: DatabaseReference

    lateinit var uid: String
    var uidInit = MutableLiveData<String>()

    var userMutableLiveData: SingleLiveEvent<Boolean> = SingleLiveEvent()


    fun register(email: String, password: String, username: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            if (firebaseAuth.currentUser != null) {
                userMutableLiveData.postValue(false)

                currentUser = firebaseAuth.currentUser!!
                uid = currentUser.uid
                uidInit.postValue(uid)
                currentUserDb = databaseReference.child((uid))

                currentUserDb.child("userData").child("followers").setValue(0)
                currentUserDb.child("userData").child("following").setValue(0)
                currentUserDb.child("userData").child("followersList").setValue("empty")
                currentUserDb.child("userData").child("followingList").setValue("empty")
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

                currentUser = firebaseAuth.currentUser!!
                uid = currentUser.uid
                uidInit.postValue(uid)
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

}