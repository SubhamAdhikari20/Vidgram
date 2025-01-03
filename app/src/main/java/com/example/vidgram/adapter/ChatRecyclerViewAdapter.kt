package com.example.vidgram.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vidgram.R
import com.example.vidgram.databinding.FragmentMessageBinding


class ChatRecyclerViewAdapter(
    val context : Context,
    val imageList : ArrayList<Int>,
    val nameList : ArrayList<String>,
    val messageList : ArrayList<String>

) : RecyclerView.Adapter<ChatRecyclerViewAdapter.FeedViewHolder>() {
    class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById(R.id.userChatImage)
        val userName: TextView = itemView.findViewById(R.id.userChatName)
        val userMessage: TextView = itemView.findViewById(R.id.userChatMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        // Linking recycler view with sample pictures view
        val itemView : View = LayoutInflater.from(context).inflate(R.layout.sample_user_chat, parent, false)
        return FeedViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        // Passing the data
        holder.userImage.setImageResource(imageList[position])
        holder.userName.text = nameList[position]
        holder.userMessage.text = messageList[position]
    }

}