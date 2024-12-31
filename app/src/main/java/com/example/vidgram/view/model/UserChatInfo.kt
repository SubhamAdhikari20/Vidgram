package com.example.vidgram
data class UserChatInfo(
    var userID: String = "",
    var name: String ?=null,  // User's name
    var username: String ?=null,
    var profilepic: String ?= null,
    var lastMessage: String ?=null,
    var timestamp: Long = 0L
)
