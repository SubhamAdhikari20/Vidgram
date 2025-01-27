package com.example.vidgram.model

data class Message(
    val messageId : String = "",
    val message: String ?= null,
    val timestamp: String = "",
    val senderId: String = "",
    val receiverId: String = ""
)

