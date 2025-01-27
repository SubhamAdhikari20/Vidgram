package com.example.vidgram.model
data class UserChatInfo(
    var chatId: String = "",
    var userId: String = "",
    var fullName: String ?=null,  // User's name
    var profilePic: String ?= null,
    var lastMessage: String ?=null,
    var timestamp: Long = 0L
)
