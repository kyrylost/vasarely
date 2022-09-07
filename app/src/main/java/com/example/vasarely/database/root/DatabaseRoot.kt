package com.example.vasarely.database.root

import android.util.Log
import com.example.vasarely.SingleLiveEvent
import com.example.vasarely.model.LocalDbCopy
import com.google.firebase.appcheck.internal.util.Logger.TAG
import com.google.firebase.database.*
import kotlinx.coroutines.*

open class DatabaseRoot {

    var databaseReference = DatabaseInstance
        .firebaseDatabase
        .reference
        .child("profiles")

    var dataChangeExceptions: SingleLiveEvent<String> = SingleLiveEvent()

    lateinit var localDbCopy: Deferred<LocalDbCopy>
    var localDbCopyInitialized = SingleLiveEvent<Boolean>()

    //var dataReference = MutableLiveData<DataSnapshot>()
    private fun addDataEventListener(data: DatabaseReference) {
        val dataChangedListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                CoroutineScope(Dispatchers.IO).launch {
                    localDbCopy = async { LocalDbCopy(dataSnapshot) }
                    Log.d(TAG, dataSnapshot.toString())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadData:onCancelled", databaseError.toException())
            }
        }
        data.addValueEventListener(dataChangedListener)
    }


    fun retrieveAllData() {
        CoroutineScope(Dispatchers.IO).launch {
            databaseReference.get().addOnSuccessListener {
                localDbCopy = async { LocalDbCopy(it) }
                localDbCopyInitialized.postValue(true)
                addDataEventListener(databaseReference)
            }.addOnFailureListener { exception ->
                dataChangeExceptions.postValue(exception.toString())
            }
        }

    }

}
