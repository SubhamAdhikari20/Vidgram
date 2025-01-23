package com.example.vidgram.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.vidgram.model.ChatModel
import com.example.vidgram.repository.ChatRepository

class ChatViewModel(val chatRepo: ChatRepository) {

    fun addChat(
        chatModel: ChatModel,
        callback: (Boolean, String) -> Unit
    ){
        chatRepo.addChat(chatModel, callback)
    }

    fun updateChat(
        chatId:String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ){
        chatRepo.updateChat(chatId, data, callback)
    }

    fun deleteChat(
        chatId:String,
        callback: (Boolean, String) -> Unit
    ){
        chatRepo.deleteChat(chatId, callback)
    }


    var _chats = MutableLiveData<ChatModel?>()
    var chats = MutableLiveData<ChatModel?>()
        get() = _chats

    var _loadingChatbyId = MutableLiveData<Boolean?>()
    var loadingChatbyId = MutableLiveData<Boolean?>()
        get() = _loadingChatbyId

    fun getChatById(
        chatId:String,
    ){
        _loadingChatbyId.value = true
        chatRepo.getChatById(chatId){
            chat, success, message ->
            if (success){
                _chats.value = chat
                _loadingChatbyId.value = false
            }
        }
    }


    var _getAllChats = MutableLiveData<List<ChatModel>?>()
    var getAllChats = MutableLiveData<List<ChatModel>?>()
        get() = _getAllChats

    var _loadingAllChats = MutableLiveData<Boolean?>()
    var loadingAllChats = MutableLiveData<Boolean?>()
        get() = _loadingAllChats

    fun getAllChat(){
        _loadingAllChats.value = true
        chatRepo.getAllChat(){
            chats, success, message ->
            if (success){
                _getAllChats.value = chats
                _loadingAllChats.value = false
            }
        }
    }
}