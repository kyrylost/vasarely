package com.example.vasarely.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vasarely.model.Database
import com.google.firebase.auth.FirebaseUser

class AppViewModel: ViewModel() {
    private var database = Database()
    lateinit var userMutableLiveData: MutableLiveData<FirebaseUser>

    fun initAppViewModel(application: Application) {
        database.initDatabase(application)

        userMutableLiveData = database.userMutableLiveData
    }

    fun register(email:String, password:String, username:String) {
        database.register(email, password, username)
    }
    fun login(email:String, password:String) = database.login(email, password)
}