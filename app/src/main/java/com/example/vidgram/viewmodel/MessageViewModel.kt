package com.example.vidgram.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.vidgram.model.Message
import com.example.vidgram.repository.MessageRepository

class MessageViewModel(val messageRepo: MessageRepository) {
    
    fun sendMessage(
        messageModel: Message,
        callback: (Boolean, String) -> Unit
    ){
        messageRepo.sendMessage(messageModel, callback)
    }

    fun updateMessage(
        messageId:String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ){
        messageRepo.updateMessage(messageId, data, callback)
    }

    fun deleteMessage(
        messageId:String,
        callback: (Boolean, String) -> Unit
    ){
        messageRepo.deleteMessage(messageId, callback)
    }


    var _chatMessages = MutableLiveData<Message?>()
    var chatMessages = MutableLiveData<Message?>()
        get() = _chatMessages

    var _loadingMessageById = MutableLiveData<Boolean?>()
    var loadingMessageById = MutableLiveData<Boolean?>()
        get() = _loadingMessageById
    
    fun getMessageById(
        messageId:String,
    ){
        _loadingMessageById.value = true
        messageRepo.getMessageById(messageId){
            chatMessage, success, message->
            if (success){
                _chatMessages.value = chatMessage
                _loadingMessageById.value = false
            }
        }
    }

    var _getAllmessages = MutableLiveData<List<Message>?>()
    var getAllmessages = MutableLiveData<List<Message>?>()
        get() = _getAllmessages

    var _loadingAllmessages = MutableLiveData<Boolean?>()
    var loadingAllmessages = MutableLiveData<Boolean?>()
        get() = _loadingAllmessages
    
    fun getAllMessages(
    ){
        _loadingAllmessages.value = true
        messageRepo.getAllMessages(){
            chatMessages, success, message ->
            if (success){
                _getAllmessages.value = chatMessages
                _loadingAllmessages.value = false
            }
        }
    }
}