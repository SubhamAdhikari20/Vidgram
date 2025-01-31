package com.example.vidgram.model

data class StoryModel(
    var storyId: String ?= null,
    var storyImage:  String ?= null,
    var username:  String ?= null,
    var storyTimeStamp:  Long? = 0L
)