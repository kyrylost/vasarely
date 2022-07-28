package com.example.vasarely.database.api

import com.example.vasarely.model.currency.Course
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("p24api/pubinfo?exchange&json&coursid=11")
    suspend fun getCourse(): Response<Course>

}