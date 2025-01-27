package com.example.vidgram.repository

import com.example.vidgram.model.Message

interface MessageRepository {

    fun sendMessage(
        messageModel: Message,
        callback: (Boolean, String) -> Unit
    )

    fun updateMessage(
        messageId:String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    )

    fun deleteMessage(
        messageId:String,
        callback: (Boolean, String) -> Unit
    )

    fun getMessageById(
        messageId:String,
        callback: (Message?, Boolean, String) -> Unit
    )

    fun getAllMessages(
        callback: (List<Message>?, Boolean, String) -> Unit
    )
}