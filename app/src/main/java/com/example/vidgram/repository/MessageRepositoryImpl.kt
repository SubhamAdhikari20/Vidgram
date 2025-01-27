package com.example.vidgram.repository

import com.example.vidgram.model.Message
import com.example.vidgram.model.MessageModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageRepositoryImpl : MessageRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val reference: DatabaseReference = database.reference.child("chats").child("messages")

    override fun sendMessage(
        messageModel: Message,
        callback: (Boolean, String) -> Unit
    ) {
        val messageId = reference.push().key.toString()
        reference.child(messageId).setValue(messageModel).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Sent")
            }
            else{
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun updateMessage(
        messageId: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ) {
        reference.child(messageId).updateChildren(data).addOnCompleteListener {
            if (it.isSuccessful){
                callback(true, "Message updated successfully")
            }
            else{
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun deleteMessage(
        messageId: String,
        callback: (Boolean, String) -> Unit
    ) {
        reference.child(messageId).removeValue().addOnCompleteListener {
            if (it.isSuccessful){
                callback(true, "Message deleted successfully")
            }
            else{
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun getMessageById(
        messageId: String, 
        callback: (Message?, Boolean, String) -> Unit
    ) {
        reference.child(messageId).addValueEventListener(object : ValueEventListener{
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
        callback: (List<Message>?, Boolean, String) -> Unit
    ) {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val messages = mutableListOf<Message>()
                    for (eachData in snapshot.children){
                        val messageModel = eachData.getValue(Message::class.java)
                        if (messageModel != null){
                            messages.add(messageModel)
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