package com.example.vidgram.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vidgram.ChatActivity
import com.example.vidgram.R
import com.example.vidgram.UserChatAdapter
import com.example.vidgram.UserChatInfo
import com.example.vidgram.databinding.FragmentMessageBinding
import com.example.vidgram.adapter.ChatRecyclerViewAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MessageFragment : Fragment() {
    private lateinit var binding : FragmentMessageBinding
    private val imageList: ArrayList<Int> = ArrayList()
    private val nameList: ArrayList<String> = ArrayList()
    private val messageList: ArrayList<String> = ArrayList()
    private lateinit var chatAdapter : ChatRecyclerViewAdapter;
    private lateinit var userChatInfoList: MutableList<UserChatInfo>
    private lateinit var adapter: UserChatAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference
    private lateinit var chatsRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase references
        database = FirebaseDatabase.getInstance()
        usersRef = database.getReference("users")
        chatsRef = database.getReference("chats")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMessageBinding.inflate(inflater, container, false)

        initializeData()
        
        // Initialize RecyclerView and Adapter
        userChatInfoList = mutableListOf()
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = UserChatAdapter(userChatInfoList, requireContext()) { user ->
            val chatId = generateChatId(user)
            val intent = Intent(requireContext(), ChatActivity::class.java).apply {
                putExtra("name", user.name)
                putExtra("chatID", chatId)
                putExtra("receiverId", user.userID)
                putExtra("receiverName", user.username)
            }
            startActivity(intent)
        }
        binding.chatRecyclerView.adapter = adapter

        // Set up adapter
        chatAdapter = ChatRecyclerViewAdapter(
            requireContext(),
            imageList,
            nameList,
            messageList
        )

        // Vertical Scroll
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.chatRecyclerView.adapter = chatAdapter

        // Fetch user data from Firebase
        fetchUserData()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // Clear and fetch data only once to avoid duplication
        fetchUserData()
    }

    private fun initializeData() {
        // Populate data
        imageList.add(R.drawable.user_chat_male)
        imageList.add(R.drawable.user_chat_female)
        imageList.add(R.drawable.user_chat_male)
        imageList.add(R.drawable.user_chat_female)
        imageList.add(R.drawable.user_chat_male)
        imageList.add(R.drawable.user_chat_male)
        imageList.add(R.drawable.user_chat_female)
        imageList.add(R.drawable.user_chat_male)
        imageList.add(R.drawable.user_chat_female)
        imageList.add(R.drawable.user_chat_male)

        nameList.add("Subham")
        nameList.add("Rijan")
        nameList.add("Yogesh")
        nameList.add("Helan")
        nameList.add("Prajesh")
        nameList.add("Subham")
        nameList.add("Rijan")
        nameList.add("Yogesh")
        nameList.add("Helan")
        nameList.add("Prajesh")

        messageList.add("This is hell a good")
        messageList.add("This is hell a good")
        messageList.add("This is hell a good")
        messageList.add("This is hell a good")
        messageList.add("This is hell a good")
        messageList.add("This is hell a good")
        messageList.add("This is hell a good")
        messageList.add("This is hell a good")
        messageList.add("This is hell a good")
        messageList.add("This is hell a good")
    }

    private fun fetchUserData() {
        userChatInfoList.clear()

        val loggedInUserId = "123"
//            FirebaseAuth.getInstance().currentUser?.uid
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(UserChatInfo::class.java)
                    val userID = userSnapshot.key ?: continue
                    if (userID != loggedInUserId) {
                        val chatID = generateChatId(UserChatInfo(userID = userID))

                        // Fetch last message for each chat
                        chatsRef.child(chatID).limitToLast(1).addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(chatSnapshot: DataSnapshot) {
                                var lastMessage = "No messages yet"
                                var timestamp = 0L

                                if (chatSnapshot.exists()) {
                                    for (messageSnapshot in chatSnapshot.children) {
                                        lastMessage = messageSnapshot.child("message")
                                            .getValue(String::class.java) ?: "No messages yet"
                                        timestamp = messageSnapshot.child("timestamp")
                                            .getValue(Long::class.java) ?: 0L
//                                        if (timestamp != 0L) {
//                                            // Convert timestamp to Date
//                                            val date = Date(timestamp)
//
//                                            // Format the date into 12-hour format with AM/PM
//                                            val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
//                                            val formattedTime = formatter.format(date)
//                                            timestamp = formattedTime
                                    }
                                }

                                val userChatInfo = UserChatInfo(
                                    userID = userID,
                                    name = user?.name,
                                    username = user?.username ?: "Unknown",
                                    profilepic = user?.profilepic ?: "",
                                    lastMessage = lastMessage,
                                    timestamp = timestamp
                                )

                                if (!userChatInfoList.any { it.userID == userID }) {
                                    userChatInfoList.add(userChatInfo)
                                }
                                adapter.notifyDataSetChanged()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("Firebase", "Error fetching chat data: ${error.message}")
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching users: ${error.message}")
            }
        })
    }

    private fun generateChatId(user: UserChatInfo): String {
        val loggedInUserId = "123"
//            FirebaseAuth.getInstance().currentUser?.uid
        return if (loggedInUserId != null) {
            if (loggedInUserId < user.userID) {
                "$loggedInUserId-${user.userID}"
            } else {
                "${user.userID}-$loggedInUserId"
            }
        } else {
            ""
        }
    }
}