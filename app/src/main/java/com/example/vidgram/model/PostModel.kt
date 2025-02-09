package com.example.vidgram.model

data class PostModel(
    var postId: String ?= null,
    var postImageUrl:  String ?= null,
    var profileImageUrl :String?= null,
    var postDescription:  String ?= null,
    var postBy:  String ?= null,
    var postTimeStamp:  String? = null
)
