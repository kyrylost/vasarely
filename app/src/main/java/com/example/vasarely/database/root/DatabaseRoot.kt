package com.example.vasarely.database.root

import android.util.Log
import com.example.vasarely.SingleLiveEvent
import com.example.vasarely.model.LocalDbCopy
import com.google.firebase.appcheck.internal.util.Logger.TAG
import com.google.firebase.database.*
import kotlinx.coroutines.*

open class DatabaseRoot {

    private var firebaseDatabase = FirebaseDatabase.getInstance("https://vasarely-f0ed5-default-rtdb.europe-west1.firebasedatabase.app")
    var databaseReference = firebaseDatabase.reference.child("profiles")
    //addDataEventListener(databaseReference)

    var dataChangeExceptions: SingleLiveEvent<String> = SingleLiveEvent()

    lateinit var localDbCopy: Deferred<LocalDbCopy>
    var localDbCopyInitialized = SingleLiveEvent<Boolean>()

    //var dataReference = MutableLiveData<DataSnapshot>()
    @OptIn(DelicateCoroutinesApi::class)
    fun addDataEventListener(data: DatabaseReference) {
        val dataChangedListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                GlobalScope.launch {
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


    @OptIn(DelicateCoroutinesApi::class)
    fun retrieveAllData() {
        GlobalScope.launch {
            databaseReference.get().addOnSuccessListener {
                localDbCopy = async { LocalDbCopy(it) }
                localDbCopyInitialized.postValue(true)
                addDataEventListener(databaseReference)
            } .addOnFailureListener { exception ->
                dataChangeExceptions.postValue(exception.toString())
            }
        }

    }


//    fun recommendationsSearch_() {
//        localDbCopyLiveEvent.postValue(localDbCopy.allData)
//    }


}
