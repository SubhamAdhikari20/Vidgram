package com.example.vidgram.model

data class PostModel(
    var postId: String ?= null,
    var postImaqe:  String ?= null,
    var postDescription:  String ?= null,
    var postBy:  String ?= null,
    var postTimeStamp:  Long? = 0L
)
