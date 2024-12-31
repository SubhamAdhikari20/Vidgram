package com.example.vidgram

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vidgram.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class UserChatAdapter(
    private val userList: List<UserChatInfo>, // List of users
    private val context: Context,
    private val onItemClick: (UserChatInfo) -> Unit // On click callback
) : RecyclerView.Adapter<UserChatAdapter.UserChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_user_chat, parent, false)
        return UserChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserChatViewHolder, position: Int) {
        val user = userList[position]
        holder.usernameTextView.text = user.name // Display the user's name

        Log.d("UserChatAdapter", "User: ${user.name}")

        holder.lastMessageTextView.text = user.lastMessage // Display last message
        holder.timestampTextView.text =
            SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(user.timestamp))

        // Set profile image (if available)
        if (user.profilepic.isNullOrEmpty()) {
            Picasso.get().load(R.drawable.user) // Load default image if profile pic is missing
                .into(holder.profileImageView)
        } else {
            Picasso.get().load(user.profilepic) // Load user's profile picture
                .into(holder.profileImageView)
        }

        // Set click listener
        holder.itemView.setOnClickListener {
            onItemClick(user)
        }
    }

    override fun getItemCount(): Int = userList.size

    inner class UserChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImageView: ImageView = itemView.findViewById(R.id.userChatImage)
        val usernameTextView: TextView = itemView.findViewById(R.id.userChatName)
        val lastMessageTextView: TextView = itemView.findViewById(R.id.userChatMessage)
        val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
    }
}
