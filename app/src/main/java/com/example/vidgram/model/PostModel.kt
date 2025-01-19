package com.example.vidgram.model

data class PostModel(
    var postID: String ?= null,
    var postImaqe:  String ?= null,
    var username:  String ?= null,
    var postDescription:  String ?= null,
    var postTimeStamp:  Long = 0L
)
