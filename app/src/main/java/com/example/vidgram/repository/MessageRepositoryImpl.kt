package com.example.vidgram.repository

import android.util.Log
import com.example.vidgram.model.Message
import com.example.vidgram.model.MessageModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageRepositoryImpl : MessageRepository {
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val reference: DatabaseReference = database.reference.child("chats")


    override fun sendMessage(
        chatId : String,
        messageModel: Message,
        callback: (Boolean, String) -> Unit
    ) {
        Log.d("chatId Ok xa tw", chatId.toString())
        val messageId = reference.child(chatId).child("messages").push().key.toString()
        messageModel.messageId = messageId
        reference.child(chatId).child("messages").child(messageId).setValue(messageModel).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Sent")
            }
            else{
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun updateMessage(
        chatId: String,
        messageId: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ) {
        reference.child(chatId).child("messages").child(messageId).updateChildren(data).addOnCompleteListener {
            if (it.isSuccessful){
                callback(true, "Message updated successfully")
            }
            else{
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun deleteMessage(
        chatId: String,
        messageId: String,
        callback: (Boolean, String) -> Unit
    ) {
        reference.child(chatId).child("messages").child(messageId).removeValue().addOnCompleteListener {
            if (it.isSuccessful){
                callback(true, "Message deleted successfully")
            }
            else{
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun getMessageById(
        chatId: String,
        messageId: String,
        callback: (Message?, Boolean, String) -> Unit
    ) {
        reference.child(chatId).child("messages").child(messageId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val messageModel = snapshot.getValue(Message::class.java)
                    callback(messageModel, true, "Message fetched successfully")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false,error.message)
            }

        })
    }

    override fun getAllMessages(
        chatId: String,
        callback: (List<Message>?, Boolean, String) -> Unit
    ) {
        reference.child(chatId).child("messages").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val messages = mutableListOf<Message>()
                    for (eachData in snapshot.children){
                        val messageModel = eachData.getValue(Message::class.java)
                        if (messageModel != null){
                            messages.add(messageModel)
                        }else{
                            Log.d("getallmsg","no msgs found")
                        }
                    }
                    callback(messages, true, "All messages fetched successfully")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }

        })
    }


}