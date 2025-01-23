package com.example.vidgram.model

data class MessageModel(
    val senderId: String = "",
    val receiverId: String = "",
    val message: String = "",
    val timestamp: Long = 0L
)
