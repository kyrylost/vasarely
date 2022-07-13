package com.example.vasarely.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class SearchViewModel: ViewModel() {
    var name = MutableLiveData<String>()
    var stopWaiting = MutableLiveData<Boolean>()
    private var getNameScopesList = mutableListOf<CoroutineScope>()
    lateinit var waitScope : CoroutineScope


    @OptIn(DelicateCoroutinesApi::class)
    fun getNameWithDelay(it: String) {

        fun asyncGetNameWithDelay() : String {
            val scopeNumber = getNameScopesList.count()
            if (scopeNumber != 1) {
                getNameScopesList[scopeNumber - 2].cancel()
            }

            Thread.sleep(1300)
            return it
        }

        GlobalScope.launch (Dispatchers.IO) {
            val text = async {asyncGetNameWithDelay()}
            getNameScopesList.add(this)
            name.postValue(text.await())
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun waitForEightSec() {
        GlobalScope.launch (Dispatchers.IO) {
            waitScope = this
            Thread.sleep(8000)
            stopWaiting.postValue(true)
        }
    }
}