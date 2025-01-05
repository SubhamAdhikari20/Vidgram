package com.example.vidgram.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MotionEvent
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vidgram.R
import com.example.vidgram.adapter.MessageAdapter
import com.example.vidgram.databinding.ActivityChatMessageBinding
import com.example.vidgram.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatMessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatMessageBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var messageList: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var receiverId: String
    private lateinit var senderId: String
    private lateinit var chatId: String

    private val messages = mutableListOf<Message>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityChatMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Initialize RecyclerView and other views via binding
        messageList = binding.messageRecyclerView
        val messageInput = binding.messageChatEditText
        val receiverNameTextView = binding.titleProfileTexView

        // Get user data passed from MessageListActivity
        receiverId = intent.getStringExtra("receiverId") ?: ""
        senderId = "123" // Example sender ID (replace with actual sender ID)

        // Create chat ID based on user IDs
        chatId = if (senderId < receiverId) "$receiverId-$senderId" else "$senderId-$receiverId"

        // Set up RecyclerView
        messageAdapter = MessageAdapter(messages, senderId)
        messageList.adapter = messageAdapter
        messageList.layoutManager = LinearLayoutManager(this)

        // Retrieve and display messages from the database
        loadMessages()

        // Handle touch event on message input (detect click on drawableEnd)
        messageInput.setOnTouchListener { v, event ->
            val drawables = messageInput.compoundDrawables
            val drawableEnd = drawables[2]  // The third drawable is drawableEnd (right side)

            if (event.action == MotionEvent.ACTION_UP) {
                // Check if the touch was on the drawableEnd (send icon)
                if (event.x >= messageInput.width - messageInput.paddingRight - drawableEnd.bounds.width()) {
                    // Action when the send icon is clicked
                    sendMessage(messageInput)
                    return@setOnTouchListener true
                }
            }
            false
        }

        // Set receiver name from intent
        val receiverName = intent.getStringExtra("name") ?: "Receiver"
        receiverNameTextView.text = receiverName

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow_resized)
        binding.toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.three_dot_icon2)

        // Apply window insets for edge-to-edge experience
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // Go back to the previous fragment or activity
        onBackPressed()
        return true
    }
    // Inflate the toolbar menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.chat_message_toolbar, menu)
        return true
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
            messageInput.text.clear() // Clear the input field after sending
        }
    }
}
