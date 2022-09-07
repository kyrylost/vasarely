package com.example.vasarely.database.user

import android.util.Log
import com.example.vasarely.SingleLiveEvent
import com.example.vasarely.database.root.DatabaseRoot
import com.google.firebase.appcheck.internal.util.Logger
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class FollowersAmountChangeListener(uid: String) : DatabaseRoot() {

    var followersAmountLiveData = SingleLiveEvent<String>()
    var listenerIsJustAttached = true

    init {
        val userFollowersReference = databaseReference
            .child(uid)
            .child("userData")
            .child("followers")
        setNewFollowerListener(userFollowersReference)
    }


    private fun setNewFollowerListener(data: DatabaseReference) {
        val dataChangedListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (listenerIsJustAttached) {
                    listenerIsJustAttached = false
                } else {
                    followersAmountLiveData.postValue(dataSnapshot.value.toString())
                    Log.d(Logger.TAG, dataSnapshot.toString())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(Logger.TAG, "loadData:onCancelled", databaseError.toException())
            }

        }
        data.addValueEventListener(dataChangedListener)
    }

}