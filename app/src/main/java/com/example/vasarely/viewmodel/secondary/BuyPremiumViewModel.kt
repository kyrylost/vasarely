package com.example.vasarely.viewmodel.secondary

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vasarely.database.api.ApiRepository
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

class BuyPremiumViewModel : ViewModel() {

    private val repository = ApiRepository()

    private var usdCourse: BigDecimal? = BigDecimal.ZERO
    private var eurCourse: BigDecimal? = BigDecimal.ZERO
    private var btcCourse: BigDecimal? = BigDecimal.ZERO

    private var standardPremiumCostInUsd: BigDecimal? = BigDecimal.ZERO
    private var standardPremiumCostInEur: BigDecimal? = BigDecimal.ZERO
    private var standardPremiumCostInBtc: BigDecimal? = BigDecimal.ZERO

    val costsAreCalculatedMutableLiveData = MutableLiveData<Boolean>()

    var displayedCost = MutableLiveData<String>()

    private var uahSelected = true
    var uahTextColor = MutableLiveData<Int>()

    private var usdSelected = false
    var usdTextColor = MutableLiveData<Int>()

    private var eurSelected = false
    var eurTextColor = MutableLiveData<Int>()

    private var btcSelected = false
    var btcTextColor = MutableLiveData<Int>()

    fun getCourse() {
        viewModelScope.launch {
            //coursesList.value = repository.getCourse()
            val allCourses = repository.getCourse()
            allCourses.body().let { coursesList ->
                if (coursesList != null) {
                    for (course in coursesList) {
                        when (course.ccy) {
                            "USD" -> usdCourse = course.buy.toBigDecimal()
                            "EUR" -> eurCourse = course.buy.toBigDecimal()
                            "BTC" -> btcCourse = course.buy.toBigDecimal()
                        }
                    }

                    standardPremiumCostInUsd = BigDecimal("99")
                        .divide(usdCourse!!, 2, RoundingMode.HALF_UP)

                    standardPremiumCostInEur = BigDecimal("99")
                        .divide(eurCourse!!, 2, RoundingMode.HALF_UP)

                    standardPremiumCostInBtc = (BigDecimal("99")
                        .divide(usdCourse!!, 8, RoundingMode.HALF_UP))
                        .divide(btcCourse!!, 8, RoundingMode.HALF_UP)

                    costsAreCalculatedMutableLiveData.value = true

                }
            }
        }
    }

    fun uahClicked() {
        if (!uahSelected) {

            uahTextColor.value = Color.parseColor("#0082DD")
            uahSelected = true

            if (usdSelected) {
                usdTextColor.value = Color.BLACK
                usdSelected = false
            } else if (eurSelected) {
                eurTextColor.value = Color.BLACK
                eurSelected = false
            } else if (btcSelected) {
                btcTextColor.value = Color.BLACK
                btcSelected = false
            }

            displayedCost.value = "99 ₴"
        }
    }

    fun usdClicked() {
        if (!usdSelected) {

            usdTextColor.value = Color.parseColor("#0082DD")
            usdSelected = true

            if (uahSelected) {
                uahTextColor.value = Color.BLACK
                uahSelected = false
            } else if (eurSelected) {
                eurTextColor.value = Color.BLACK
                eurSelected = false
            } else if (btcSelected) {
                btcTextColor.value = Color.BLACK
                btcSelected = false
            }

            displayedCost.value = standardPremiumCostInUsd.toString() + " $"
        }
    }

    fun eurClicked() {
        if (!eurSelected) {

            eurTextColor.value = Color.parseColor("#0082DD")
            eurSelected = true

            if (uahSelected) {
                uahTextColor.value = Color.BLACK
                uahSelected = false
            } else if (usdSelected) {
                usdTextColor.value = Color.BLACK
                usdSelected = false
            } else if (btcSelected) {
                btcTextColor.value = Color.BLACK
                btcSelected = false
            }
        }

        displayedCost.value = standardPremiumCostInEur.toString() + " €"
    }

    fun btcClicked() {
        if (!btcSelected) {

            btcTextColor.value = Color.parseColor("#0082DD")
            btcSelected = true

            if (uahSelected) {
                uahTextColor.value = Color.BLACK
                uahSelected = false
            } else if (usdSelected) {
                usdTextColor.value = Color.BLACK
                usdSelected = false
            } else if (eurSelected) {
                eurTextColor.value = Color.BLACK
                eurSelected = false
            }
        }

        displayedCost.value = standardPremiumCostInBtc.toString() + " ₿"
    }

}