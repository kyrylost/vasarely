package com.example.vasarely.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.vasarely.SingleLiveEvent
import com.example.vasarely.model.Database
import com.example.vasarely.model.UserData
import java.util.*
import kotlin.collections.HashMap

class AppViewModel: ViewModel() {
    private var database = Database()
    lateinit var userMutableLiveData: SingleLiveEvent<Boolean>
    lateinit var userData: SingleLiveEvent<Any>
    lateinit var localData: UserData
    lateinit var dataChangeExceptions: SingleLiveEvent<String>


    fun isLocalDataInitialized() = ::localData.isInitialized

    private fun asBoolean(int: Int): Boolean {
        return (int == 1)
    }


    fun initAppViewModel(application: Application) {
        database.initDatabase(application)

        userMutableLiveData = database.userMutableLiveData
        userData = database.userData
        dataChangeExceptions = database.dataChangeExceptions
    }

    fun register(email:String, password:String, username:String) = database.register(email, password, username)

    fun login(email:String, password:String) = database.login(email, password)

    fun logout() = database.logout()

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

    fun updateName(newNickname: String) {
         database.updateName(newNickname)
    }

    fun getData() {
        database.getData()
    }

    fun processData(data : Any) {
        val username : String
        val technique : String
        val mood : String
        val genres = mutableListOf<String>()

        val dataHashMap = data as HashMap<*, *>

        val preferenceHashMap = dataHashMap["preferences"] as HashMap<*, *>

        username = dataHashMap["username"].toString()
        technique = preferenceHashMap["technique"].toString()
        mood = preferenceHashMap["mood"].toString()
        val genresArrayList = preferenceHashMap["genres"] as ArrayList<*>
        for (value in genresArrayList) {
            genres.add(value as String)
        }

        localData = UserData(username, technique, mood, genres)


    }
}