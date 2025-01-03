package com.example.vidgram.view.activity

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vidgram.model.Message
import com.example.vidgram.adapter.MessageAdapter
import com.example.vidgram.databinding.ActivityChatBinding
import com.example.vidgram.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var messageList: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var receiverId: String
    private lateinit var senderId: String
    private lateinit var chatId: String

    // Define a mutable list to hold messages
    private val messages = mutableListOf<Message>()

    // ViewBinding instance
    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase instances
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Initialize RecyclerView and other views via binding
        messageList = binding.messageRecyclerView
        val messageInput = binding.messageInput
        val sendButton = binding.sendButton
        val receiverNameTextView = binding.receiverNameTextView

        // Get user data passed from MessageListActivity
        receiverId = intent.getStringExtra("receiverId") ?: ""
        senderId = "123" // Example sender ID

        // Create chat ID based on user IDs
        chatId = if (senderId < receiverId) "$senderId-$receiverId" else "$receiverId-$senderId"

        // Set up RecyclerView
        messageAdapter = MessageAdapter(messages, senderId)
        messageList.adapter = messageAdapter
        messageList.layoutManager = LinearLayoutManager(this)

        // Retrieve and display messages from the database
        loadMessages()

        // Handle sending new messages
        sendButton.setOnClickListener {
            sendMessage(messageInput)
        }

        // Set receiver name from intent
        val receiverName = intent.getStringExtra("name") ?: "Receiver"
        receiverNameTextView.text = receiverName
    }

    private fun loadMessages() {
        database.child("chats").child(chatId).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                message?.let {
                    messages.add(it) // Add message to the list
                    messageAdapter.notifyItemInserted(messages.size - 1) // Notify adapter of new message
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun sendMessage(messageInput: EditText) {
        val messageText = messageInput.text.toString()
        if (messageText.isNotEmpty()) {
            val message = Message(senderId, receiverId, messageText, System.currentTimeMillis())
            database.child("chats").child(chatId).push().setValue(message)
            messageInput.text.clear()
        }
    }
}
