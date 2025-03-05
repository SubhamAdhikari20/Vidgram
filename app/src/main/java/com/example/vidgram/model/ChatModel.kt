package com.example.vidgram.model

data class ChatModel(
    var senderId: String = "",
    var receiverId: String = "",
    var message: String = "",
    var timestamp: Long = 0L
)
