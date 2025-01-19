package com.example.vidgram.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.vidgram.model.StoryModel
import com.example.vidgram.repository.StoryRepository

class StoryViewModel(val storyRepo: StoryRepository) {

    fun addStory(
        storyModel: StoryModel,
        callback: (Boolean, String) -> Unit
    ){
        storyRepo.addStory(storyModel, callback)
    }

    fun updateStory(
        storyId:String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ){
        storyRepo.updateStory(storyId, data, callback)
    }

    fun deleteStory(
        storyId:String,
        callback: (Boolean, String) -> Unit
    ){
        storyRepo.deleteStory(storyId, callback)
    }

    var _story = MutableLiveData<StoryModel?>()
    var story = MutableLiveData<StoryModel?>()
        get() = _story

    var _loadingStoryById = MutableLiveData<Boolean>()
    var loadingStoryById = MutableLiveData<Boolean>()
        get() = _loadingStoryById

    fun getStoryById(
        storyId:String,
    ){
        storyRepo.getStoryById(storyId){
            story, success, message ->
            if (success){
                _story.value = story
                _loadingStoryById.value = false
            }
        }
    }


    var _getAllStories = MutableLiveData<List<StoryModel>?>()
    var getAllStories = MutableLiveData<List<StoryModel>?>()
        get() = _getAllStories

    var _loadingAllStories = MutableLiveData<Boolean>()
    var loadingAllStories = MutableLiveData<Boolean>()
        get() = _loadingAllStories

    fun getAllStory(){
        storyRepo.getAllStory(){
                stories, success, message ->
            if (success){
                _getAllStories.value = stories
                _loadingAllStories.value = false
            }
        }
    }
}