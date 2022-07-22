package com.example.vasarely.viewmodel.secondary

import android.graphics.Color
import com.example.vasarely.SingleLiveEvent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TagsForPhoto {

    var minNumberOfTechniques = 0
    var minNumberOfGenres = 0
    var minNumberOfMoods = 0

    var byHandClicked = 0
    var byHandBGColor = 0
    var byHandTextColor = 0
    val byHandColorChanged = SingleLiveEvent<Boolean>()

    private var computerGraphClicked = 0
    var computerGraphBGColor = 0
    var computerGraphTextColor = 0
    val computerGraphColorChanged = SingleLiveEvent<Boolean>()


    var stillLifeButtonClicked = 0
    var stillLifeButtonBGColor = 0
    var stillLifeButtonTextColor = 0
    val stillLifeButtonColorChanged = SingleLiveEvent<Boolean>()

    var portraitButtonClicked = 0
    var portraitButtonBGColor = 0
    var portraitButtonTextColor = 0
    val portraitButtonColorChanged = SingleLiveEvent<Boolean>()

    var landscapeButtonClicked = 0
    var landscapeButtonBGColor = 0
    var landscapeButtonTextColor = 0
    val landscapeButtonColorChanged = SingleLiveEvent<Boolean>()

    var marineButtonClicked = 0
    var marineButtonBGColor = 0
    var marineButtonTextColor = 0
    val marineButtonColorChanged = SingleLiveEvent<Boolean>()

    var battlePaintingButtonClicked = 0
    var battlePaintingButtonBGColor = 0
    var battlePaintingButtonTextColor = 0
    val battlePaintingButtonColorChanged = SingleLiveEvent<Boolean>()

    var interiorButtonClicked = 0
    var interiorButtonBGColor = 0
    var interiorButtonTextColor = 0
    val interiorButtonColorChanged = SingleLiveEvent<Boolean>()

    var caricatureButtonClicked = 0
    var caricatureButtonBGColor = 0
    var caricatureButtonTextColor = 0
    val caricatureButtonColorChanged = SingleLiveEvent<Boolean>()

    var nudeButtonClicked = 0
    var nudeButtonBGColor = 0
    var nudeButtonTextColor = 0
    val nudeButtonColorChanged = SingleLiveEvent<Boolean>()

    var animeButtonClicked = 0
    var animeButtonBGColor = 0
    var animeButtonTextColor = 0
    val animeButtonColorChanged = SingleLiveEvent<Boolean>()

    var horrorButtonClicked = 0
    var horrorButtonBGColor = 0
    var horrorButtonTextColor = 0
    val horrorButtonColorChanged = SingleLiveEvent<Boolean>()


    var funButtonClicked = 0
    var funButtonBGColor = 0
    var funButtonTextColor = 0
    var funButtonColorChanged = SingleLiveEvent<Boolean>()

    private var depressedButtonClicked = 0
    var depressedButtonBGColor = 0
    var depressedButtonTextColor = 0
    var depressedButtonColorChanged = SingleLiveEvent<Boolean>()


    @OptIn(DelicateCoroutinesApi::class)
    fun byHandClicked() {
        GlobalScope.launch {
            if (byHandClicked == 1) {
                byHandClicked = 0
                byHandBGColor = Color.parseColor("#00FFFFFF")
                byHandTextColor = Color.BLACK
                minNumberOfTechniques--

                byHandColorChanged.postValue(true)
            }
            else {
                byHandClicked++
                byHandBGColor = Color.parseColor("#0082DD")
                byHandTextColor = Color.WHITE
                minNumberOfTechniques++

                byHandColorChanged.postValue(true)

                if (computerGraphClicked == 1) {
                    computerGraphClicked = 0
                    computerGraphBGColor = Color.parseColor("#00FFFFFF")
                    computerGraphTextColor = Color.BLACK
                    minNumberOfTechniques--

                    computerGraphColorChanged.postValue(true)
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun computerGraphClicked() {
        GlobalScope.launch {
            if (computerGraphClicked == 1) {
                computerGraphClicked = 0
                computerGraphBGColor = Color.parseColor("#00FFFFFF")
                computerGraphTextColor = Color.BLACK
                minNumberOfTechniques--

                computerGraphColorChanged.postValue(true)
            }
            else {
                computerGraphClicked++
                computerGraphBGColor = Color.parseColor("#0082DD")
                computerGraphTextColor = Color.WHITE
                minNumberOfTechniques++

                computerGraphColorChanged.postValue(true)

                if (byHandClicked == 1) {
                    byHandClicked = 0
                    byHandBGColor = Color.parseColor("#00FFFFFF")
                    byHandTextColor = Color.BLACK
                    minNumberOfTechniques--

                    byHandColorChanged.postValue(true)
                }
            }
        }
    }


    private fun unclickStillLifeButton () {
        stillLifeButtonClicked = 0
        stillLifeButtonBGColor = Color.parseColor("#00FFFFFF")
        stillLifeButtonTextColor = Color.BLACK
        minNumberOfGenres--

        stillLifeButtonColorChanged.postValue(true)
    }

    private fun unclickPortraitButton () {
        portraitButtonClicked = 0
        portraitButtonBGColor = Color.parseColor("#00FFFFFF")
        portraitButtonTextColor = Color.BLACK
        minNumberOfGenres--

        portraitButtonColorChanged.postValue(true)
    }

    private fun unclickLandscapeButton () {
        landscapeButtonClicked = 0
        landscapeButtonBGColor = Color.parseColor("#00FFFFFF")
        landscapeButtonTextColor = Color.BLACK
        minNumberOfGenres--

        landscapeButtonColorChanged.postValue(true)
    }

    private fun unclickMarineButton () {
        marineButtonClicked = 0
        marineButtonBGColor = Color.parseColor("#00FFFFFF")
        marineButtonTextColor = Color.BLACK
        minNumberOfGenres--

        marineButtonColorChanged.postValue(true)
    }

    private fun unclickBattlePaintingButton () {
        battlePaintingButtonClicked = 0
        battlePaintingButtonBGColor = Color.parseColor("#00FFFFFF")
        battlePaintingButtonTextColor = Color.BLACK
        minNumberOfGenres--

        battlePaintingButtonColorChanged.postValue(true)
    }

    private fun unclickInteriorButton () {
        interiorButtonClicked = 0
        interiorButtonBGColor = Color.parseColor("#00FFFFFF")
        interiorButtonTextColor = Color.BLACK
        minNumberOfGenres--

        interiorButtonColorChanged.postValue(true)
    }

    private fun unclickCaricatureButton () {
        caricatureButtonClicked = 0
        caricatureButtonBGColor = Color.parseColor("#00FFFFFF")
        caricatureButtonTextColor = Color.BLACK
        minNumberOfGenres--

        caricatureButtonColorChanged.postValue(true)
    }

    private fun unclickNudeButton () {
        nudeButtonClicked = 0
        nudeButtonBGColor = Color.parseColor("#00FFFFFF")
        nudeButtonTextColor = Color.BLACK
        minNumberOfGenres--

        nudeButtonColorChanged.postValue(true)
    }

    private fun unclickAnimeButton () {
        animeButtonClicked = 0
        animeButtonBGColor = Color.parseColor("#00FFFFFF")
        animeButtonTextColor = Color.BLACK
        minNumberOfGenres--

        animeButtonColorChanged.postValue(true)
    }

    private fun unclickHorrorButton () {
        horrorButtonClicked = 0
        horrorButtonBGColor = Color.parseColor("#00FFFFFF")
        horrorButtonTextColor = Color.BLACK
        minNumberOfGenres--

        horrorButtonColorChanged.postValue(true)
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun stillLifeButtonClicked() {
        GlobalScope.launch {
            if (stillLifeButtonClicked == 1) {
                unclickStillLifeButton()
            }
            else {
                stillLifeButtonClicked++
                stillLifeButtonBGColor = Color.parseColor("#0082DD")
                stillLifeButtonTextColor = Color.WHITE
                minNumberOfGenres++

                stillLifeButtonColorChanged.postValue(true)

                if (portraitButtonClicked == 1) {
                    unclickPortraitButton()
                }

                if (landscapeButtonClicked == 1) {
                    unclickLandscapeButton()
                }

                if (marineButtonClicked == 1) {
                    unclickMarineButton()
                }

                if (battlePaintingButtonClicked == 1) {
                    unclickBattlePaintingButton()
                }

                if (interiorButtonClicked == 1) {
                    unclickInteriorButton()
                }

                if (caricatureButtonClicked == 1) {
                    unclickCaricatureButton()
                }

                if (nudeButtonClicked == 1) {
                    unclickNudeButton()
                }

                if (animeButtonClicked == 1) {
                    unclickAnimeButton()
                }

                if (horrorButtonClicked == 1) {
                    unclickHorrorButton()
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun portraitButtonClicked() {
        GlobalScope.launch {
            if (portraitButtonClicked == 1) {
                unclickPortraitButton()
            }
            else {
                portraitButtonClicked++
                portraitButtonBGColor = Color.parseColor("#0082DD")
                portraitButtonTextColor = Color.WHITE
                minNumberOfGenres++

                portraitButtonColorChanged.postValue(true)

                if (stillLifeButtonClicked == 1) {
                    unclickStillLifeButton()
                }

                if (landscapeButtonClicked == 1) {
                    unclickLandscapeButton()
                }

                if (marineButtonClicked == 1) {
                    unclickMarineButton()
                }

                if (battlePaintingButtonClicked == 1) {
                    unclickBattlePaintingButton()
                }

                if (interiorButtonClicked == 1) {
                    unclickInteriorButton()
                }

                if (caricatureButtonClicked == 1) {
                    unclickCaricatureButton()
                }

                if (nudeButtonClicked == 1) {
                    unclickNudeButton()
                }

                if (animeButtonClicked == 1) {
                    unclickAnimeButton()
                }

                if (horrorButtonClicked == 1) {
                    unclickHorrorButton()
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun landscapeButtonClicked() {
        GlobalScope.launch {
            if (landscapeButtonClicked == 1) {
                unclickLandscapeButton()
            }
            else {
                landscapeButtonClicked++
                landscapeButtonBGColor = Color.parseColor("#0082DD")
                landscapeButtonTextColor = Color.WHITE
                minNumberOfGenres++

                landscapeButtonColorChanged.postValue(true)

                if (stillLifeButtonClicked == 1) {
                    unclickStillLifeButton()
                }

                if (portraitButtonClicked == 1) {
                    unclickPortraitButton()
                }

                if (marineButtonClicked == 1) {
                    unclickMarineButton()
                }

                if (battlePaintingButtonClicked == 1) {
                    unclickBattlePaintingButton()
                }

                if (interiorButtonClicked == 1) {
                    unclickInteriorButton()
                }

                if (caricatureButtonClicked == 1) {
                    unclickCaricatureButton()
                }

                if (nudeButtonClicked == 1) {
                    unclickNudeButton()
                }

                if (animeButtonClicked == 1) {
                    unclickAnimeButton()
                }

                if (horrorButtonClicked == 1) {
                    unclickHorrorButton()
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun marineButtonClicked() {
        GlobalScope.launch {
            if (marineButtonClicked == 1) {
                unclickMarineButton()
            }
            else {
                marineButtonClicked++
                marineButtonBGColor = Color.parseColor("#0082DD")
                marineButtonTextColor = Color.WHITE
                minNumberOfGenres++

                marineButtonColorChanged.postValue(true)

                if (stillLifeButtonClicked == 1) {
                    unclickStillLifeButton()
                }

                if (portraitButtonClicked == 1) {
                    unclickPortraitButton()
                }

                if (landscapeButtonClicked == 1) {
                    unclickLandscapeButton()
                }

                if (battlePaintingButtonClicked == 1) {
                    unclickBattlePaintingButton()
                }

                if (interiorButtonClicked == 1) {
                    unclickInteriorButton()
                }

                if (caricatureButtonClicked == 1) {
                    unclickCaricatureButton()
                }

                if (nudeButtonClicked == 1) {
                    unclickNudeButton()
                }

                if (animeButtonClicked == 1) {
                    unclickAnimeButton()
                }

                if (horrorButtonClicked == 1) {
                    unclickHorrorButton()
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun battlePaintingButtonClicked() {
        GlobalScope.launch {
            if (battlePaintingButtonClicked == 1) {
                unclickBattlePaintingButton()
            }
            else {
                battlePaintingButtonClicked++
                battlePaintingButtonBGColor = Color.parseColor("#0082DD")
                battlePaintingButtonTextColor = Color.WHITE
                minNumberOfGenres++

                battlePaintingButtonColorChanged.postValue(true)

                if (stillLifeButtonClicked == 1) {
                    unclickStillLifeButton()
                }

                if (portraitButtonClicked == 1) {
                    unclickPortraitButton()
                }

                if (landscapeButtonClicked == 1) {
                    unclickLandscapeButton()
                }

                if (marineButtonClicked == 1) {
                    unclickMarineButton()
                }

                if (interiorButtonClicked == 1) {
                    unclickInteriorButton()
                }

                if (caricatureButtonClicked == 1) {
                    unclickCaricatureButton()
                }

                if (nudeButtonClicked == 1) {
                    unclickNudeButton()
                }

                if (animeButtonClicked == 1) {
                    unclickAnimeButton()
                }

                if (horrorButtonClicked == 1) {
                    unclickHorrorButton()
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun interiorButtonClicked() {
        GlobalScope.launch {
            if (interiorButtonClicked == 1) {
                unclickInteriorButton()
            }
            else {
                interiorButtonClicked++
                interiorButtonBGColor = Color.parseColor("#0082DD")
                interiorButtonTextColor = Color.WHITE
                minNumberOfGenres++

                interiorButtonColorChanged.postValue(true)

                if (stillLifeButtonClicked == 1) {
                    unclickStillLifeButton()
                }

                if (portraitButtonClicked == 1) {
                    unclickPortraitButton()
                }

                if (landscapeButtonClicked == 1) {
                    unclickLandscapeButton()
                }

                if (marineButtonClicked == 1) {
                    unclickMarineButton()
                }

                if (battlePaintingButtonClicked == 1) {
                    unclickBattlePaintingButton()
                }

                if (caricatureButtonClicked == 1) {
                    unclickCaricatureButton()
                }

                if (nudeButtonClicked == 1) {
                    unclickNudeButton()
                }

                if (animeButtonClicked == 1) {
                    unclickAnimeButton()
                }

                if (horrorButtonClicked == 1) {
                    unclickHorrorButton()
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun caricatureButtonClicked() {
        GlobalScope.launch {
            if (caricatureButtonClicked == 1) {
                unclickCaricatureButton()
            }
            else {
                caricatureButtonClicked++
                caricatureButtonBGColor = Color.parseColor("#0082DD")
                caricatureButtonTextColor = Color.WHITE
                minNumberOfGenres++

                caricatureButtonColorChanged.postValue(true)

                if (stillLifeButtonClicked == 1) {
                    unclickStillLifeButton()
                }

                if (portraitButtonClicked == 1) {
                    unclickPortraitButton()
                }

                if (landscapeButtonClicked == 1) {
                    unclickLandscapeButton()
                }

                if (marineButtonClicked == 1) {
                    unclickMarineButton()
                }

                if (battlePaintingButtonClicked == 1) {
                    unclickBattlePaintingButton()
                }

                if (interiorButtonClicked == 1) {
                    unclickInteriorButton()
                }

                if (nudeButtonClicked == 1) {
                    unclickNudeButton()
                }

                if (animeButtonClicked == 1) {
                    unclickAnimeButton()
                }

                if (horrorButtonClicked == 1) {
                    unclickHorrorButton()
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun nudeButtonClicked() {
        GlobalScope.launch {
            if (nudeButtonClicked == 1) {
                unclickNudeButton()
            }
            else {
                nudeButtonClicked++
                nudeButtonBGColor = Color.parseColor("#0082DD")
                nudeButtonTextColor = Color.WHITE
                minNumberOfGenres++

                nudeButtonColorChanged.postValue(true)

                if (stillLifeButtonClicked == 1) {
                    unclickStillLifeButton()
                }

                if (portraitButtonClicked == 1) {
                    unclickPortraitButton()
                }

                if (landscapeButtonClicked == 1) {
                    unclickLandscapeButton()
                }

                if (marineButtonClicked == 1) {
                    unclickMarineButton()
                }

                if (battlePaintingButtonClicked == 1) {
                    unclickBattlePaintingButton()
                }

                if (interiorButtonClicked == 1) {
                    unclickInteriorButton()
                }

                if (caricatureButtonClicked == 1) {
                    unclickCaricatureButton()
                }

                if (animeButtonClicked == 1) {
                    unclickAnimeButton()
                }

                if (horrorButtonClicked == 1) {
                    unclickHorrorButton()
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun animeButtonClicked() {
        GlobalScope.launch {
            if (animeButtonClicked == 1) {
                unclickAnimeButton()
            }
            else {
                animeButtonClicked++
                animeButtonBGColor = Color.parseColor("#0082DD")
                animeButtonTextColor = Color.WHITE
                minNumberOfGenres++

                animeButtonColorChanged.postValue(true)

                if (stillLifeButtonClicked == 1) {
                    unclickStillLifeButton()
                }

                if (portraitButtonClicked == 1) {
                    unclickPortraitButton()
                }

                if (landscapeButtonClicked == 1) {
                    unclickLandscapeButton()
                }

                if (marineButtonClicked == 1) {
                    unclickMarineButton()
                }

                if (battlePaintingButtonClicked == 1) {
                    unclickBattlePaintingButton()
                }

                if (interiorButtonClicked == 1) {
                    unclickInteriorButton()
                }

                if (caricatureButtonClicked == 1) {
                    unclickCaricatureButton()
                }

                if (nudeButtonClicked == 1) {
                    unclickNudeButton()
                }

                if (horrorButtonClicked == 1) {
                    unclickHorrorButton()
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun horrorButtonClicked() {
        GlobalScope.launch {
            if (horrorButtonClicked == 1) {
                unclickHorrorButton()
            }
            else {
                horrorButtonClicked++
                horrorButtonBGColor = Color.parseColor("#0082DD")
                horrorButtonTextColor = Color.WHITE
                minNumberOfGenres++

                horrorButtonColorChanged.postValue(true)

                if (stillLifeButtonClicked == 1) {
                    unclickStillLifeButton()
                }

                if (portraitButtonClicked == 1) {
                    unclickPortraitButton()
                }

                if (landscapeButtonClicked == 1) {
                    unclickLandscapeButton()
                }

                if (marineButtonClicked == 1) {
                    unclickMarineButton()
                }

                if (battlePaintingButtonClicked == 1) {
                    unclickBattlePaintingButton()
                }

                if (interiorButtonClicked == 1) {
                    unclickInteriorButton()
                }

                if (caricatureButtonClicked == 1) {
                    unclickCaricatureButton()
                }

                if (nudeButtonClicked == 1) {
                    unclickNudeButton()
                }

                if (animeButtonClicked == 1) {
                    unclickAnimeButton()
                }
            }
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun funButtonClicked() {
        GlobalScope.launch {
            if (funButtonClicked == 1) {
                funButtonClicked = 0
                funButtonBGColor = Color.parseColor("#00FFFFFF")
                funButtonTextColor = Color.BLACK
                minNumberOfMoods--

                funButtonColorChanged.postValue(true)
            }
            else {
                funButtonClicked++
                funButtonBGColor = Color.parseColor("#0082DD")
                funButtonTextColor = Color.WHITE
                minNumberOfMoods++

                funButtonColorChanged.postValue(true)

                if (depressedButtonClicked == 1) {
                    depressedButtonClicked = 0
                    depressedButtonBGColor = Color.parseColor("#00FFFFFF")
                    depressedButtonTextColor = Color.BLACK
                    minNumberOfMoods--

                    depressedButtonColorChanged.postValue(true)
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun depressedButtonClicked() {
        GlobalScope.launch {
            if (depressedButtonClicked == 1) {
                depressedButtonClicked = 0
                depressedButtonBGColor = Color.parseColor("#00FFFFFF")
                depressedButtonTextColor = Color.BLACK
                minNumberOfMoods--

                depressedButtonColorChanged.postValue(true)
            }
            else {
                depressedButtonClicked++
                depressedButtonBGColor = Color.parseColor("#0082DD")
                depressedButtonTextColor = Color.WHITE
                minNumberOfMoods++

                depressedButtonColorChanged.postValue(true)

                if (funButtonClicked == 1) {
                    funButtonClicked = 0
                    funButtonBGColor = Color.parseColor("#00FFFFFF")
                    funButtonTextColor = Color.BLACK
                    minNumberOfMoods--

                    funButtonColorChanged.postValue(true)
                }
            }
        }
    }
}