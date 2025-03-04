package com.example.vidgram.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.vidgram.R
import com.example.vidgram.adapter.MessageAdapter
import com.example.vidgram.databinding.ActivityMessageBinding
import com.example.vidgram.model.Message
import com.example.vidgram.model.UserChatInfo
import com.example.vidgram.repository.ChatRepositoryImpl
import com.example.vidgram.repository.MessageRepositoryImpl
import com.example.vidgram.repository.UserRepositoryImpl
import com.example.vidgram.services.ZegoService
import com.example.vidgram.viewmodel.ChatViewModel
import com.example.vidgram.viewmodel.MessageViewModel
import com.example.vidgram.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import java.util.Collections

class MessageActivity : AppCompatActivity() {
    lateinit var binding: ActivityMessageBinding
    lateinit var messageViewModel: MessageViewModel
    lateinit var userViewModel: UserViewModel
    lateinit var chatViewModel: ChatViewModel
    lateinit var messageAdapter: MessageAdapter
    lateinit var chatId: String
    lateinit var senderId: String
    lateinit var receiverId: String
    lateinit var receiverFullName: String
    var receiverProfileImage: String? = null

    lateinit var zegoService: ZegoService

    var chatModel = UserChatInfo()
    var messageModelList = ArrayList<Message>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow_resized)
        binding.toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.three_dot_icon2)




        // Message Backend Binding
        val messageRepo = MessageRepositoryImpl()
        messageViewModel = MessageViewModel(messageRepo)

        // User Backend Binding
        val userRepo = UserRepositoryImpl()
        userViewModel = UserViewModel(userRepo)

        // Chat Backend Binding
        val chatRepo = ChatRepositoryImpl()
        chatViewModel = ChatViewModel(chatRepo)

        val currentUser = userViewModel.getCurrentUser()
        currentUser?.let {
            senderId = it.uid.toString()
            userViewModel.getUserFromDatabase(it.uid.toString())
        }

        userViewModel.userData.observe(this) {
            senderId = it?.userID.toString()
        }

        // Get user data passed from ChatFragment or OthersProfileFragment
        chatId = intent.getStringExtra("chatId").toString()
        receiverId = intent.getStringExtra("receiverId").toString()
        receiverFullName = intent.getStringExtra("fullName").toString()
        receiverProfileImage = intent.getStringExtra("profilePicture").toString()
        binding.profileFullName.text = receiverFullName
        Glide.with(this).load(receiverProfileImage).circleCrop().into(binding.messageProfilePicture)


        ZegoService.initZego(application, senderId)

        ZegoService.prepareVideoCall(binding, receiverId)
        ZegoService.prepareAudioCall(binding, receiverId)

        Log.d("user","currentuser:$senderId, recevierid:$receiverId")

        messageViewModel.getAllMessages(chatId)
        // Retrieve and display messageModelList from the database
        messageViewModel.getAllmessages.observe(this) { messages ->
            if (messages != null && messages.isNotEmpty()) {
                messages.forEach { msg ->
                }
            } else {
            }
        }
        setupObservers()

        // Set up RecyclerView
        binding.messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageAdapter = MessageAdapter(this, messageModelList, senderId)
        binding.messageRecyclerView.adapter = messageAdapter

        // Handle touch event on message input (detect click on drawableEnd)
        binding.messageChatEditText.setOnTouchListener { v, event ->
            val drawables = binding.messageChatEditText.compoundDrawables
            val drawableEnd = drawables[2]  // The third drawable is drawableEnd (right side)

            if (event.action == MotionEvent.ACTION_UP) {
                // Check if the touch was on the drawableEnd (send icon)
                if (event.x >= binding.messageChatEditText.width - binding.messageChatEditText.paddingRight - drawableEnd.bounds.width()) {
                    // Action when the send icon is clicked

                    chatModel = UserChatInfo(chatId = chatId, senderId = senderId, receiverId = receiverId)

                    if (chatId == "") {
                        chatViewModel.createOrGetChat(chatModel) { chat, success, message ->
                            if (success && chat != null) {
                                chatId = chat.chatId
                                sendMessage()
                                Toast.makeText(this@MessageActivity, message, Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this@MessageActivity, message, Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        sendMessage()
                    }
                    return@setOnTouchListener true
                }
            }
            false
        }

        // Apply window insets for edge-to-edge experience
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        // Go back to the previous fragment or activity
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    // Inflate the toolbar menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.chat_message_toolbar, menu)
        return true
    }





    private fun setupObservers() {
        messageViewModel.getAllmessages.observe(this) { messages ->
            messages?.let {
                messageAdapter.updateData(it)
                binding.messageRecyclerView.post {
                    binding.messageRecyclerView.smoothScrollToPosition(it.size - 1)
                }
            }
        }

        messageViewModel.loadingAllmessages.observe(this) { isLoading ->
            // Handle loading state if needed
        }
    }

    private fun sendMessage() {
        val messageText = binding.messageChatEditText.text.toString().trim()
        if (messageText.isNotEmpty()) {
            val messageModel = Message(senderId = senderId, receiverId = receiverId, message = messageText, timestamp = System.currentTimeMillis())

            messageViewModel.sendMessage(chatId, messageModel) { success, message ->
                if (success) {
                    binding.messageChatEditText.text.clear()    // Clear the input field after sending
                }
            }
        }
    }


}
