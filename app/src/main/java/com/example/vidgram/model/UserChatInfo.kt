package com.example.vidgram.model

data class UserChatInfo(
    var chatId: String = "",
    var lastMessage: String ?=null,
    var timestamp: Long = 0L,
    val unreadCount: Int = 0,
    var user1Id: String = "",
    var user2Id: String = "",
//    var messages: MutableMap<String, Message> = mutableMapOf()
)



