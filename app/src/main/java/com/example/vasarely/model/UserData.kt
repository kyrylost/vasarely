package com.example.vasarely.model

data class UserData(var username : String,
                    var techniqueReference: String,
                    var moodReference: String,
                    var genreReferences: List<String>)