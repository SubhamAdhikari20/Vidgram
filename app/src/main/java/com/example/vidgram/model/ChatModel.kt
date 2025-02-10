package com.example.vidgram.model

data class ChatModel(
    var chatId: String = "",
    var userId: String = "",
    var fullName: String ?= null,
    var username: String ?=null,
    var profilePic: String ?= null,
    var lastMessage: String ?= null,
    var timestamp: Long = 0L
)