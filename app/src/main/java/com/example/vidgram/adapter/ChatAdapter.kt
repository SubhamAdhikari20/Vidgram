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
import com.example.vidgram.model.UserChatInfo
import com.example.vidgram.model.UserModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ChatAdapter(
    val context: Context,
    val  chatModelList: ArrayList<UserChatInfo>,     // List of chats
    var userModelList: ArrayList<UserModel>,    // List of users
    val onItemClick: (UserChatInfo) -> Unit    // On click callback

) : RecyclerView.Adapter<ChatAdapter.FeedViewHolder>() {
//    var userModelList = ArrayList<UserModel>()

    inner class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImageView: ImageView = itemView.findViewById(R.id.userChatImage)
        val nameTextView: TextView = itemView.findViewById(R.id.userChatName)
        val lastMessageTextView: TextView = itemView.findViewById(R.id.userChatMessage)
        val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        // Linking recycler view with sample pictures view
        val itemView : View = LayoutInflater.from(context).inflate(R.layout.sample_user_chat, parent, false)
        return FeedViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return chatModelList.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val chat = chatModelList[position]
        val user = userModelList[position]
//        val currentUser = userViewModel.getCurrentUser()
//        currentUser.let{    // it -> currentUser
//            Log.d("userId",it?.uid.toString())
//            senderId = it?.uid.toString()
//            userViewModel.getUserFromDatabase(it?.uid.toString())
//        }


        holder.nameTextView.text = user.fullName            // Display the user's name
        holder.lastMessageTextView.text = chat.lastMessage  // Display last message
        holder.timestampTextView.text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(chat.timestamp))

//         Set profile image (if available) using Glide
//         Load default image if profile pic is missing
        if (user.profilePicture.isNullOrEmpty()) {
            Glide.with(context)
                .load(R.drawable.user)
                .circleCrop()
                .into(holder.profileImageView)
        }
        else {
            Glide.with(context)
                .load(user.profilePicture) // Load user's profile picture
                .circleCrop()
//                .placeholder(R.drawable.loading) // Optional: loading placeholder
//                .error(R.drawable.error) // Optional: error placeholder
                .into(holder.profileImageView)
        }

        // Set click listener
        holder.itemView.setOnClickListener {
            onItemClick(chat)
        }

    }

    fun updateData(chats: List<UserChatInfo>){
        chatModelList.clear()
        chatModelList.addAll(chats)
        notifyDataSetChanged()
    }


//    fun getReceiverId(position: Int):String{
//        return chatModelList[position].receiverId
//    }

}