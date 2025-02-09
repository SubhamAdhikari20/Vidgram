package com.example.vidgram.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vidgram.R
import com.example.vidgram.model.ChatModel
import com.example.vidgram.model.UserChatInfo
import java.text.SimpleDateFormat
import java.util.*

class UserChatAdapter(
    val userList: List<ChatModel>, // List of users
    val context: Context,
    val onItemClick: (ChatModel) -> Unit // On click callback
) : RecyclerView.Adapter<UserChatAdapter.UserChatViewHolder>() {

    inner class UserChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImageView: ImageView = itemView.findViewById(R.id.userChatImage)
        val usernameTextView: TextView = itemView.findViewById(R.id.userChatName)
        val lastMessageTextView: TextView = itemView.findViewById(R.id.userChatMessage)
        val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_user_chat, parent, false)
        return UserChatViewHolder(view)
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: UserChatViewHolder, position: Int) {
        val user = userList[position]
        holder.usernameTextView.text = user.fullName // Display the user's name

        Log.d("UserChatAdapter", "User: ${user.fullName}")

        holder.lastMessageTextView.text = user.lastMessage // Display last message
        holder.timestampTextView.text =
            SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(user.timestamp))

        // Set profile image (if available) using Glide
        if (user.profilePic.isNullOrEmpty()) {
            Glide.with(context)
                .load(R.drawable.user) // Load default image if profile pic is missing
                .into(holder.profileImageView)
        } else {
            Glide.with(context)
                .load(user.profilePic) // Load user's profile picture
//                .placeholder(R.drawable.loading) // Optional: loading placeholder
//                .error(R.drawable.error) // Optional: error placeholder
                .into(holder.profileImageView)
        }

        // Set click listener
        holder.itemView.setOnClickListener {
            onItemClick(user)
        }
    }


}
