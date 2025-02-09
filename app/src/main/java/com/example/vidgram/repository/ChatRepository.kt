package com.example.vidgram.repository

import com.example.vidgram.model.UserChatInfo

interface ChatRepository {

    fun createOrGetChat(
        chatModel: UserChatInfo,
        callback: (UserChatInfo?, Boolean, String) -> Unit
    )

    fun updateChat(
        chatId:String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    )

    fun deleteChat(
        chatId:String,
        callback: (Boolean, String) -> Unit
    )

    fun getChatById(
        chatId:String,
        callback: (UserChatInfo?, Boolean, String) -> Unit
    )

    fun getAllChats(
        senderId: String,
        callback: (List<UserChatInfo>?, Boolean, String) -> Unit
    )
}