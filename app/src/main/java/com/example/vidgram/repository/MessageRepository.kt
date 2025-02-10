package com.example.vidgram.repository

import com.example.vidgram.model.Message

interface MessageRepository {

    fun sendMessage(
        chatId: String,
        messageModel: Message,
        callback: (Boolean, String) -> Unit
    )

    fun updateMessage(
        chatId: String,
        messageId:String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    )

    fun deleteMessage(
        chatId: String,
        messageId:String,
        callback: (Boolean, String) -> Unit
    )

    fun getMessageById(
        chatId: String,
        messageId:String,
        callback: (Message?, Boolean, String) -> Unit
    )

    fun getAllMessages(
        chatId: String,
        callback: (List<Message>?, Boolean, String) -> Unit
    )
}