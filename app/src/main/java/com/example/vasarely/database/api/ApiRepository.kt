package com.example.vasarely.database.api

import com.example.vasarely.model.currency.Course
import retrofit2.Response

class ApiRepository {
    suspend fun getCourse(): Response<Course> {
        return RetrofitInstance.api.getCourse()
    }
}