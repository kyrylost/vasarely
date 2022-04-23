package com.example.vasarely.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.vasarely.SingleLiveEvent
import com.example.vasarely.model.Database

class AppViewModel: ViewModel() {
    private var database = Database()
    lateinit var userMutableLiveData: SingleLiveEvent<Boolean>


    fun asBoolean(int: Int): Boolean {
        return (int == 1)
    }


    fun initAppViewModel(application: Application) {
        database.initDatabase(application)

        userMutableLiveData = database.userMutableLiveData
    }

    fun register(email:String, password:String, username:String) {
        database.register(email, password, username)
    }
    fun login(email:String, password:String) = database.login(email, password)

    fun savePreference(byHandSelected: Int, computerGraphicsSelected: Int,
                       depressedButtonSelected: Int, funButtonSelected: Int,
                       stillLifeButtonSelected: Int, portraitButtonSelected: Int,
                       landscapeButtonSelected: Int, marineButtonSelected: Int,
                       battlePaintingButtonSelected: Int, interiorButtonSelected: Int,
                       caricatureButtonSelected: Int, nudeButtonSelected: Int,
                       animeButtonSelected: Int, horrorButtonSelected: Int) {

        database.savePreference(asBoolean(byHandSelected), asBoolean(computerGraphicsSelected),
            asBoolean(depressedButtonSelected), asBoolean(funButtonSelected),
            asBoolean(stillLifeButtonSelected), asBoolean(portraitButtonSelected),
            asBoolean(landscapeButtonSelected), asBoolean(marineButtonSelected),
            asBoolean(battlePaintingButtonSelected), asBoolean(interiorButtonSelected),
            asBoolean(caricatureButtonSelected), asBoolean(nudeButtonSelected),
            asBoolean(animeButtonSelected), asBoolean(horrorButtonSelected))

    }
}