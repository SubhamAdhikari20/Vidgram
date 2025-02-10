package com.example.vidgram.repository

import com.example.vidgram.model.UserChatInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatRepositoryImpl : ChatRepository {
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val reference: DatabaseReference = database.reference.child("chats")

    override fun createOrGetChat(
        chatModel: UserChatInfo,
        callback: (UserChatInfo?, Boolean, String) -> Unit
    ) {
        // Sort the user IDs to generate a consistent chat ID, regardless of the order
        val sortedUserIds = listOf(chatModel.user1Id, chatModel.user2Id).sorted()
        val chatId = "${sortedUserIds[0]}-${sortedUserIds[1]}" // Concatenate sorted IDs

        chatModel.chatId = chatId

        reference.child(chatId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    reference.child(chatId).setValue(chatModel).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            getChatById(chatId, callback)
                        } else {
                            callback(null, false, task.exception?.message ?: "Error creating chat")
                        }
                    }
                } else {
                    // Chat already exists; fetch it.
                    getChatById(chatId, callback)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
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
        callback: (UserChatInfo?, Boolean, String) -> Unit
    ) {
        reference.child(chatId).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val chatModel = snapshot.getValue(UserChatInfo::class.java)

                    // Convert messages map to list if needed
//                    val messagesList = chatModel?.messages?.values?.toList() ?: emptyList()

                    // Update chatModel with the messages list
//                    chatModel?.messages = messagesList

                    callback(chatModel, true, "Chat fetched successfully")
                }
                else {
                    callback(null, false, "Chat not found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false,error.message)
            }

        })
    }

    override fun getAllChats(
        senderId: String,
        callback: (List<UserChatInfo>?, Boolean, String) -> Unit
    ) {
//        if (senderId == )
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val chats = mutableListOf<UserChatInfo>()
                    for (eachData in snapshot.children){
                        val chatModel = eachData.getValue(UserChatInfo::class.java)
                        if (chatModel != null){
                            if (chatModel.user1Id == senderId || chatModel.user2Id == senderId){
                                chats.add(chatModel)
                            }
                        }
                    }
                    // When both queries are complete, invoke the callback.
                    if (chats.isNotEmpty()) {
                        callback(chats, true, "All chats fetched successfully")
                    } else {
                        callback(emptyList(), true, "No chats found")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }

        })
    }
}