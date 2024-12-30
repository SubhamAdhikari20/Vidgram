package com.example.week2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vidgram.R
import com.example.vidgram.view.model.Post

class PostAdapter(private val postList: List<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.insta_post, parent, false)
        return PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        // Bind data to the views
        holder.username.text = post.username
        holder.caption.text = post.caption
        // Set images using Glide or Picasso
        Glide.with(holder.itemView.context).load(post.userAvatar).into(holder.userAvatar)
        Glide.with(holder.itemView.context).load(post.postImage).into(holder.postImage)
    }

    override fun getItemCount() = postList.size

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById(R.id.username)
        val caption: TextView = itemView.findViewById(R.id.caption)
        val userAvatar: ImageView = itemView.findViewById(R.id.userAvatar)
        val postImage: ImageView = itemView.findViewById(R.id.postImage)
        val likeButton: ImageView = itemView.findViewById(R.id.likeButton)
        val commentButton: ImageView = itemView.findViewById(R.id.commentButton)
    }
}
