package com.example.vasarely.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class SearchViewModel: ViewModel() {
    var name = MutableLiveData<String>()
    var scopesList = mutableListOf<CoroutineScope>()

    @OptIn(DelicateCoroutinesApi::class)
    fun getNameWithDelay(it: String) {

        fun asyncGetNameWithDelay() : String {
            val scopeNumber = scopesList.count()
            if (scopeNumber != 1) {
                scopesList[scopeNumber - 2].cancel()
            }

            Thread.sleep(1300)
            return it
        }

        GlobalScope.launch (Dispatchers.IO) {
            val text = async {asyncGetNameWithDelay()}
            scopesList.add(this)
            name.postValue(text.await())
        }
    }
}