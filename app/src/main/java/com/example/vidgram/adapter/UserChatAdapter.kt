package com.example.vidgram.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vidgram.R
import com.example.vidgram.model.UserChatInfo
import com.example.vidgram.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.*
class UserChatAdapter(
    private val userList: List<UserChatInfo>,
    private val context: Context,
    private val onItemClick: (UserChatInfo) -> Unit
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
        val userChatInfo = userList[position]

        // Set the username (already fetched and passed)
        holder.usernameTextView.text = userChatInfo.receiverName

        // Bind the last message and timestamp
        holder.lastMessageTextView.text = userChatInfo.lastMessage
        holder.timestampTextView.text =
            SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(userChatInfo.timestamp))

        // Load the profile image using Glide (if needed)
        Glide.with(context)
            .load(R.drawable.user)  // Use default image if no profile picture is available
            .into(holder.profileImageView)

        // Set click listener for item view
        holder.itemView.setOnClickListener {
            onItemClick(userChatInfo)  // Send the entire UserChatInfo to the callback
        }
    }
}
