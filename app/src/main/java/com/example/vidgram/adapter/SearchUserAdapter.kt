package com.example.vidgram.adapter

import android.content.Context
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

class SearchUserAdapter(
    var context: Context,
    var userModelList: ArrayList<UserModel>,
    var onItemClick: (UserModel) -> Unit
) : RecyclerView.Adapter<SearchUserAdapter.FeedViewHolder>() {

    inner class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImageView: ImageView = itemView.findViewById(R.id.searchUserProfilePic)
        val fullNameTextView: TextView = itemView.findViewById(R.id.searchUserFullName)
        val usernameTextView: TextView = itemView.findViewById(R.id.searchUserUsername)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val itemView : View = LayoutInflater.from(context).inflate(R.layout.sample_user_search, parent, false)
        return FeedViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return userModelList.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val user = userModelList[position]

        holder.fullNameTextView.text = user.fullName

        var userId = user.userID
//        holder.usernameTextView.text = user.

        if (user.profilePicture.isNullOrEmpty()) {
            Glide.with(context)
                .load(R.drawable.user)
                .circleCrop()
                .into(holder.profileImageView)
        }
        else {
            Glide.with(context)
                .load(user.profilePicture)
                .circleCrop()
                .into(holder.profileImageView)
        }


        // Set click listener
        holder.itemView.setOnClickListener {
            onItemClick(user)
        }
    }

    fun setFilteredList(userModelList: ArrayList<UserModel>){
        this.userModelList = userModelList
        notifyDataSetChanged()
    }

    fun updateData(users: List<UserModel>){
        userModelList.clear()
        userModelList.addAll(users)
        notifyDataSetChanged()
    }


}