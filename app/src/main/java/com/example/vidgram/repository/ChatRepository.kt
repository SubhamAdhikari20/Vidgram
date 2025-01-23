package com.example.vidgram.repository

import com.example.vidgram.model.ChatModel

interface ChatRepository {

    fun addChat(
        chatModel: ChatModel,
        callback: (Boolean, String) -> Unit
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
        callback: (ChatModel?, Boolean, String) -> Unit
    )

    fun getAllChat(
        callback: (List<ChatModel>?, Boolean, String) -> Unit
    )
}