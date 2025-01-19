package com.example.vidgram.repository

import com.example.vidgram.model.StoryModel

interface StoryRepository {

    fun addStory(
        storyModel: StoryModel,
        callback: (Boolean, String) -> Unit
    )

    fun updateStory(
        storyId:String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    )

    fun deleteStory(
        storyId:String,
        callback: (Boolean, String) -> Unit
    )

    fun getStoryById(
        storyId:String,
        callback: (StoryModel?, Boolean, String) -> Unit
    )

    fun getAllStory(
        callback: (List<StoryModel>?, Boolean, String) -> Unit
    )
}