package com.example.vasarely.viewmodel.primary

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vasarely.database.users.RecommendationsStorage
import com.example.vasarely.model.user.UserData
import com.google.firebase.database.DataSnapshot

class RecommendationsViewModel() : ViewModel() {

    lateinit var userData: UserData

    private val recommendationsStorage = RecommendationsStorage()
    val recommendedPost = recommendationsStorage.recommendation

    fun getPostsData(dataSnapshot: DataSnapshot, userData: UserData) {
        val recommendationsToProcessList = mutableListOf<Map<Int, Any?>>()
        for (snapshot in dataSnapshot.children) {
            if (snapshot.key != userData.uid) {
                val postsData = mutableMapOf<Int, Any?>()
                for ((dataIndex, data) in snapshot.children.withIndex()) {
                    if (dataIndex == 0) {
                        for (post in data.children) {
                            for (postNumberAndTags in post.children) {
                                val postNumberAndTagsHashMap = postNumberAndTags.value as HashMap<*, *>
                                val postTagsList = postNumberAndTagsHashMap.values
                                val tags = mutableListOf<String>()
                                for ((jIndex, j) in postTagsList.withIndex()) {
                                    if (jIndex == 0) {
                                        j as HashMap<*, *>
                                        tags.add(j["mood"].toString())
                                        tags.add(j["genre"].toString())
                                        tags.add(j["technique"].toString())
                                    }
                                    else {
                                        tags.add(j as String)
                                        tags.add(snapshot.key as String)
                                    }
                                }
                                postsData[postNumberAndTags.key!!.toInt()] = tags
                            }
                        }
                    }

                    else {
                        for (child in data.children) {
                            if (child.key == "worksAmount" && child.value != 0L) {
                                recommendationsToProcessList.add(postsData)
                                Log.d("Posts", postsData.toString())
                            }
                        }
                    }
                }
            }
        }
        findPostsToRecommend(recommendationsToProcessList, userData)
    }

    private fun findPostsToRecommend(postsData : List<Map<Int, Any?>>, userData: UserData) {
        var correspondsToPreference = 0
        for (singlePostData in postsData) {
            for (singlePostDataMap in singlePostData) {
                val singlePostDataMapValue = singlePostDataMap.value as List<*>
                val postMood = singlePostDataMapValue[0]
                val postsGenre = singlePostDataMapValue[1]
                val postTechnique = singlePostDataMapValue[2]

                if (userData.moodReference == "ignore") correspondsToPreference += 25
                else if (postMood == userData.moodReference) correspondsToPreference += 25

                for (genre in userData.genreReferences) {
                    if (genre == postsGenre) {
                        correspondsToPreference += 50
                    }
                }

                if (userData.techniqueReference == "ignore") correspondsToPreference += 25
                else if (postTechnique == userData.techniqueReference) correspondsToPreference += 25


                if (correspondsToPreference >= 50) {recommendationsStorage.getImage(singlePostDataMapValue[4] as String,
                    singlePostDataMap.key)
                    Log.d("correspondsToPreference", "true")
                }
            }

        }
    }
}