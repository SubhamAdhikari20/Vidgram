package com.example.vidgram.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vidgram.adapter.UserChatAdapter
import com.example.vidgram.adapter.ChatAdapter
import com.example.vidgram.databinding.FragmentChatBinding
import com.example.vidgram.model.ChatModel
import com.example.vidgram.model.UserChatInfo
import com.example.vidgram.model.UserModel
import com.example.vidgram.repository.ChatRepositoryImpl
import com.example.vidgram.repository.MessageRepositoryImpl
import com.example.vidgram.repository.UserRepositoryImpl
import com.example.vidgram.view.activity.MessageActivity
import com.example.vidgram.viewmodel.ChatViewModel
import com.example.vidgram.viewmodel.MessageViewModel
import com.example.vidgram.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatFragment : Fragment() {
    lateinit var binding: FragmentChatBinding
    lateinit var chatViewModel: ChatViewModel
    lateinit var userViewModel: UserViewModel
    lateinit var chatAdapter: ChatAdapter
//    lateinit var adapter: UserChatAdapter
    val chatModelList = ArrayList<UserChatInfo>()
    val userModelList = ArrayList<UserModel>()
    lateinit var user1Id : String
    lateinit var user2Id : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Chat Backend Binding
        val chatRepo = ChatRepositoryImpl()
        chatViewModel = ChatViewModel(chatRepo)

        // User Backend Binding
        val userRepo = UserRepositoryImpl()
        userViewModel = UserViewModel(userRepo)


        val currentUser = userViewModel.getCurrentUser()
        currentUser.let{    // it -> currentUser
            Log.d("userId",it?.uid.toString())
            user1Id = it?.uid.toString()
            userViewModel.getUserFromDatabase(it?.uid.toString())
        }

        // Fetch user data from Firebase
        chatViewModel.getAllChats(user1Id)
        setupObservers()

        binding.chatRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // Initialize RecyclerView and Adapter
        chatAdapter = ChatAdapter(requireContext(), chatModelList, userModelList) { chat ->
            // navigate to chat
            val intent = Intent(requireContext(), MessageActivity::class.java).apply {
                putExtra("chatId", chat.chatId)
                if (user1Id == chat.user1Id){
                    putExtra("senderId", user1Id)
                    putExtra("receiverId", chat.user2Id)
                    userViewModel.getUserFromDatabase(chat.user2Id)
                    userViewModel.userData.observe(requireActivity()){
                        putExtra("fullName", it?.fullName.toString())
                        putExtra("profilePicture", it?.profilePicture.toString())
                    }
                }

                else if(user1Id == chat.user2Id){
                    putExtra("senderId", user1Id)
                    putExtra("receiverId", chat.user1Id)
                    userViewModel.getUserFromDatabase(chat.user1Id)
                    userViewModel.userData.observe(requireActivity()){
                        putExtra("fullName", it?.fullName.toString())
                        putExtra("profilePicture", it?.profilePicture.toString())
                    }
                }
            }
            startActivity(intent)
        }

        binding.chatRecyclerView.adapter = chatAdapter


    }

    fun setupObservers() {
        chatViewModel.getAllchats.observe(requireActivity()){ messages ->
            messages?.let {
                chatAdapter.updateData(messages)
            }
        }
//        binding.chatRecyclerView.smoothScrollToPosition(chatModelList.size - 1)

        chatViewModel.loadingAllChats.observe(this) { isLoading ->
            // Handle loading state if needed
        }
    }



}
