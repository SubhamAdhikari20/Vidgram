package com.example.vidgram.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vidgram.R
import com.example.vidgram.databinding.FragmentMessageBinding
import com.example.vidgram.adapter.ChatRecyclerViewAdapter

class MessageFragment : Fragment() {
    private lateinit var binding : FragmentMessageBinding
    private val imageList: ArrayList<Int> = ArrayList()
    private val nameList: ArrayList<String> = ArrayList()
    private val messageList: ArrayList<String> = ArrayList()
    private lateinit var chatAdapter : ChatRecyclerViewAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMessageBinding.inflate(inflater, container, false)


        initializeData()

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


        return binding.root
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

    companion object {

    }
}