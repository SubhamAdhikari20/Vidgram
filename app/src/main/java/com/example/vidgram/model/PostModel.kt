package com.example.vidgram.model

data class PostModel(
    var postId: String ?= null,
    var postImage:  String ?= null,
    var profileImage :String?= null,
    var postDescription:  String ?= null,
    var username:  String ?= null,
    var postTimeStamp:  String? = null
){
    constructor() : this(null, null, null, null, null,null)

}