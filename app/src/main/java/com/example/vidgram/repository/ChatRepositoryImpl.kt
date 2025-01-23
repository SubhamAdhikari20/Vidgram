package com.example.vidgram.repository

import com.example.vidgram.model.ChatModel
import com.example.vidgram.model.StoryModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatRepositoryImpl : ChatRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val reference: DatabaseReference = database.reference.child("chats")

    override fun addChat(
        chatModel: ChatModel,
        callback: (Boolean, String) -> Unit
    ) {
        val chatId = reference.push().key.toString()
        chatModel.chatId = chatId
        reference.child(chatId).setValue(chatModel).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Chat added successfully")
            }
            else{
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun updateChat(
        chatId: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ) {
        reference.child(chatId).updateChildren(data).addOnCompleteListener {
            if (it.isSuccessful){
                callback(true, "Chat updated successfully")
            }
            else{
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun deleteChat(
        chatId: String,
        callback: (Boolean, String) -> Unit
    ) {
        reference.child(chatId).removeValue().addOnCompleteListener {
            if (it.isSuccessful){
                callback(true, "Chat deleted successfully")
            }
            else{
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun getChatById(
        chatId: String,
        callback: (ChatModel?, Boolean, String) -> Unit
    ) {
        reference.child(chatId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val chatModel = snapshot.getValue(ChatModel::class.java)
                    callback(chatModel, true, "Chat fetched successfully")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false,error.message)
            }

        })
    }

    override fun getAllChat(
        callback: (List<ChatModel>?, Boolean, String) -> Unit
    ) {
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val chats = mutableListOf<ChatModel>()
                    for (eachData in snapshot.children){
                        val chatModel = eachData.getValue(ChatModel::class.java)
                        if (chatModel != null){
                            chats.add(chatModel)
                        }
                    }
                    callback(chats, true, "All chats fetched successfully")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }

        })
    }
}