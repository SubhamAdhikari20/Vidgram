package com.example.vidgram.model
data class UserChatInfo(
    var userID: String = "",
    var fullName: String ?=null,  // User's name
    var username: String ?=null,
    var profilepic: String ?= null,
    var lastMessage: String ?=null,
    var timestamp: Long = 0L
)
