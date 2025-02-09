package com.example.vidgram.model

data class Message(
    var messageId : String = "",
    var senderId: String = "",
    var receiverId: String = "",
    var message: String ?= null,
    var timestamp: Long = 0L,
    val isRead: Boolean = false
){
    // Optionally add a fromMap method if needed
    companion object {
        fun fromMap(data: Map<String, Any>): Message {
            return Message(
                messageId = data["messageId"] as? String ?: "",
                senderId = data["senderId"] as? String ?: "",
                receiverId = data["receiverId"] as? String ?: "",
                message = data["message"] as? String ?: "",
                timestamp = data["timestamp"] as? Long ?: 0L,
                isRead = data["isRead"] as? Boolean ?: false
            )
        }
    }

}

