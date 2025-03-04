package com.example.vidgram.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vidgram.adapter.UserChatAdapter
import com.example.vidgram.databinding.FragmentChatBinding
import com.example.vidgram.model.UserChatInfo
import com.example.vidgram.repository.ChatRepositoryImpl
import com.example.vidgram.repository.UserRepositoryImpl
import com.example.vidgram.view.activity.MessageActivity
import com.example.vidgram.viewmodel.ChatViewModel
import com.example.vidgram.viewmodel.UserViewModel
class ChatFragment : Fragment() {

    lateinit var binding: FragmentChatBinding
    lateinit var chatViewModel: ChatViewModel
    lateinit var userViewModel: UserViewModel
    val userChatInfoList = ArrayList<UserChatInfo>()
    lateinit var user1Id: String

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

        // Initialize ViewModels and Repositories
        val chatRepo = ChatRepositoryImpl()
        chatViewModel = ChatViewModel(chatRepo)

        val userRepo = UserRepositoryImpl()
        userViewModel = UserViewModel(userRepo)

        // Get the current user and fetch user data
        val currentUser = userViewModel.getCurrentUser()
        currentUser?.let {
            Log.d("userId", it.uid.toString())
            user1Id = it.uid.toString()
            userViewModel.getUserFromDatabase(it.uid.toString())
        }

        // Fetch user chat data
        chatViewModel.getAllChats(user1Id)
        setupObservers()

        // Setup RecyclerView
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val adapter = UserChatAdapter(userChatInfoList, requireContext()) { userChatInfo ->
            // Determine the receiverId based on the senderId
            val receiverId = userChatInfo.receiverId

            // Handle item click to navigate to the message screen
            val intent = Intent(requireContext(), MessageActivity::class.java).apply {
                putExtra("chatId", userChatInfo.chatId)
                putExtra("receiverId", receiverId)

                // The receiver's name and profile picture are already included in the UserChatInfo model
                putExtra("fullName", userChatInfo.receiverName ?: "")
            }

            // Start the message activity
            startActivity(intent)
        }

        binding.chatRecyclerView.adapter = adapter
    }

    private fun setupObservers() {
        chatViewModel.getAllchats.observe(viewLifecycleOwner) { chats ->
            chats?.let {
                Log.d("msg", "From chat fragment: $it")
                userChatInfoList.clear()

                // Just add the chats to the list, receiver details are already set in UserChatInfo
                userChatInfoList.addAll(it)

                // Notify the adapter that the data has changed
                binding.chatRecyclerView.adapter?.notifyDataSetChanged()
            }
        }

        chatViewModel.loadingAllChats.observe(viewLifecycleOwner) { isLoading ->
            // Handle loading state if needed
        }
    }


    override fun onResume() {
        super.onResume()
            super.onResume()

            // Refresh the chat list when returning to this fragment
            chatViewModel.getAllChats(user1Id)
        }


    }
