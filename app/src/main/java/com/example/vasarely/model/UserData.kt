package com.example.vasarely.model

data class UserData(val username : String,
                    val techniqueReference: String,
                    val genreReferences: List<String>,
                    val moodReference: String)