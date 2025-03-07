package com.example.vidgram.repository

import android.util.Log
import com.example.vidgram.model.ChatModel
import com.example.vidgram.model.UserChatInfo
import com.example.vidgram.viewmodel.ChatViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatRepositoryImpl : ChatRepository {
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val reference: DatabaseReference = database.reference.child("chats")
    val repo = UserRepositoryImpl()

    override fun createOrGetChat(
        chatModel: UserChatInfo,
        callback: (UserChatInfo?, Boolean, String) -> Unit
    ) {
        // Sort the user IDs to generate a consistent chat ID, regardless of the order
        val sortedUserIds = listOf(chatModel.senderId, chatModel.receiverId).sorted()
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
            if (it.isSuccessful) {
                callback(true, "Chat updated successfully")
            } else {
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun deleteChat(
        chatId: String,
        callback: (Boolean, String) -> Unit
    ) {
        reference.child(chatId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Chat deleted successfully")
            } else {
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun getChatById(
        chatId: String,
        callback: (UserChatInfo?, Boolean, String) -> Unit
    ) {
        reference.child(chatId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val chatModel = snapshot.getValue(UserChatInfo::class.java)
                    callback(chatModel, true, "Chat fetched successfully")
                } else {
                    callback(null, false, "Chat not found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }

    override fun getAllChats(
        currentUserId: String,
        callback: (List<UserChatInfo>?, Boolean, String) -> Unit
    ) {
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val chats = mutableListOf<UserChatInfo>()

                    for (eachChat in snapshot.children) {
                        val chatId = eachChat.key ?: continue

                        val chatUsers = chatId.split("-")
                        if (chatUsers.size != 2) continue

                        val firstUserId = chatUsers[0]
                        val secondUserId = chatUsers[1]

                        // to Ensure current user is part of the chat
                        if (currentUserId != firstUserId && currentUserId != secondUserId) {
                            continue
                        }

                        val senderId = if (currentUserId == firstUserId) firstUserId else secondUserId
                        val receiverId = if (currentUserId == firstUserId) secondUserId else firstUserId

                        var lastMessage: String? = null
                        var lastTimestamp: Long = 0L

                        for (messageSnapshot in eachChat.child("messages").children) {
                            val message = messageSnapshot.getValue(ChatModel::class.java)
                            if (message != null) {
                                lastMessage = message.message
                                lastTimestamp = message.timestamp
                            }
                        }

                        if (lastMessage != null) {
                            val chatInfo = UserChatInfo(
                                chatId = chatId,
                                senderId = senderId,
                                receiverId = receiverId,
                                lastMessage = lastMessage,
                                timestamp = lastTimestamp
                            )

                            val otherUserId = receiverId

                            repo.getUserFromDatabase(otherUserId) { userData, success, _ ->
                                chatInfo.receiverName = if (success && userData != null) userData.fullName ?: "Unknown" else "Unknown"

                                chats.add(chatInfo)

                                if (chats.size == snapshot.children.count { chatNode ->
                                        val chatUsers = chatNode.key?.split("-") ?: return@count false
                                        chatUsers.size == 2 && (chatUsers[0] == currentUserId || chatUsers[1] == currentUserId)
                                    }) {
                                    callback(chats, true, "All chats fetched successfully")
                                }
                            }
                        }
                    }

                    if (chats.isEmpty()) {
                        callback(emptyList(), true, "No chats found")
                    }
                } else {
                    callback(emptyList(), true, "No chats found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }




}
