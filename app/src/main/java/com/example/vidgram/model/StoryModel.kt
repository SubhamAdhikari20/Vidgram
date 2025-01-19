package com.example.vidgram.model

data class StoryModel(
    var storyId: String ?= null,
    var storyImaqe:  String ?= null,
    var storyDesc:  String ?= null,
    var storyBy:  String ?= null,
    var storyTimeStamp:  Long? = 0L
)