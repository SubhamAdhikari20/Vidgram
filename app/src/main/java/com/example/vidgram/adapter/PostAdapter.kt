package com.example.vidgram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vidgram.R
import com.example.vidgram.model.Post

class PostAdapter(
    private val postList: List<Post>,
    private val onCommentClick: (Post) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById(R.id.postUserName)
        val caption: TextView = itemView.findViewById(R.id.postFeedTitle)
        val time: TextView = itemView.findViewById(R.id.postFeedTime)
        val like: TextView = itemView.findViewById(R.id.postFeedLikeText)
        val dislike: TextView = itemView.findViewById(R.id.postFeedDislikeText)
        val comment: TextView = itemView.findViewById(R.id.postFeedCommentText)
        val commentButton: Button = itemView.findViewById(R.id.postFeedCommentButton)
        val share: TextView = itemView.findViewById(R.id.postFeedShareText)
        val userAvatar: ImageView = itemView.findViewById(R.id.postFeedProfileImage)
        val postImage: ImageView = itemView.findViewById(R.id.postFeedMedia)
//        val likeButton: ImageView = itemView.findViewById(R.id.likeButton)
//        val commentButton: ImageView = itemView.findViewById(R.id.commentButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.sample_post, parent, false)
        return PostViewHolder(itemView)
    }

    override fun getItemCount() = postList.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        // Bind data to the views
        holder.username.text = post.username
        holder.caption.text = post.caption
        holder.time.text = post.time
        holder.like.text = post.like
        holder.dislike.text = post.dislike
        holder.comment.text = post.comment
        holder.share.text = post.share
        holder.userAvatar.setImageResource(post.userAvatar)
        holder.postImage.setImageResource(post.postImage)

        // Set click listener for the comment
        holder.comment.setOnClickListener {
            onCommentClick(post) // Trigger callback when comment is clicked
        }

        holder.commentButton.setOnClickListener {
            onCommentClick(post) // Pass the clicked post
        }

        // Set images using Glide or Picasso
//        Glide.with(holder.itemView.context).load(post.userAvatar).into(holder.userAvatar)
//        Glide.with(holder.itemView.context).load(post.postImage).into(holder.postImage)
    }


    fun updatePosts(newPosts: List<Post>) {
        (postList as MutableList).clear()
        postList.addAll(newPosts)
        notifyDataSetChanged()
    }


}
