package com.example.vidgram.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.vidgram.model.UserChatInfo
import com.example.vidgram.repository.ChatRepository

class ChatViewModel(val chatRepo: ChatRepository) {

    fun addChat(
        chatModel: UserChatInfo,
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


    var _chats = MutableLiveData<UserChatInfo?>()
    var chats = MutableLiveData<UserChatInfo?>()
        get() = _chats

    var _loadingChatById = MutableLiveData<Boolean?>()
    var loadingChatbyId = MutableLiveData<Boolean?>()
        get() = _loadingChatById

    fun getChatById(
        chatId:String,
    ){
        _loadingChatById.value = true
        chatRepo.getChatById(chatId){
            chat, success, message ->
            if (success){
                _chats.value = chat
                _loadingChatById.value = false
            }
        }
    }


    var _getAllchats = MutableLiveData<List<UserChatInfo>?>()
    var getAllchats = MutableLiveData<List<UserChatInfo>?>()
        get() = _getAllchats

    var _loadingAllChats = MutableLiveData<Boolean?>()
    var loadingAllChats = MutableLiveData<Boolean?>()
        get() = _loadingAllChats

    fun getAllChats(){
        _loadingAllChats.value = true
        chatRepo.getAllChats(){
            chats, success, message ->
            if (success){
                _getAllchats.value = chats
                _loadingAllChats.value = false
            }
        }
    }
}