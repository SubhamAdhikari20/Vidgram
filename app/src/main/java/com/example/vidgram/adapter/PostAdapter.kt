package com.example.vidgram.adapter

import android.content.Context
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
import com.example.vidgram.model.PostModel
import kotlin.math.min

class PostAdapter(val context: Context, private val posts: MutableList<PostModel>, private val onCommentClick: (PostModel) -> Unit) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]

        /*
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
         */


        /*
         */

        // Set Profile Image (Using Glide to load image from URL)
        val profileImageUrl = post.profileImage ?: ""
        if (profileImageUrl.isNotEmpty()) {
            Glide.with(context)
                .load(profileImageUrl)
                .circleCrop()
                .into(holder.userAvatar)
        } else {
            holder.userAvatar.setImageResource(R.drawable.person)
        }

        // Set Username
        holder.username.text = post.username ?: "Unknown User"

        // Set Post Image (Using Glide to load image from URL)
        val postImageUrl = post.postImaqe ?: "" // Get the image URL

// Check if the URL uses HTTP and change it to HTTPS
        val securePostImageUrl = if (postImageUrl.startsWith("http://", ignoreCase = true)) {
            postImageUrl.replace("http://", "https://", ignoreCase = true)
        } else {
            postImageUrl
        }

// Load the image with Glide using the updated URL
        if (securePostImageUrl.isNotEmpty()) {
            Glide.with(context)
                .load(securePostImageUrl)
                .into(holder.postImage)
        } else {
            //if no image found
            holder.postImage.setImageResource(R.drawable.person1)
        }

        holder.comment.setOnClickListener {
            onCommentClick(post) // Trigger callback when comment is clicked
        }

        holder.commentButton.setOnClickListener {
            onCommentClick(post) // Pass the clicked post
        }

        // Set Caption
        holder.caption.text = post.postDescription ?: "No caption provided"

        // Set Timestamp
        holder.time.text = post.postTimeStamp ?: "Unknown time"

    }

    override fun getItemCount(): Int {
        return posts.size
    }

    // Add a new post to the top of the list
    fun addPost(post: PostModel) {
        posts.add(0, post)  // Insert the new post at the top
        notifyItemInserted(0)  // Notify the adapter that a new item is inserted at position 0
    }

    // ViewHolder Class
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
    }
}