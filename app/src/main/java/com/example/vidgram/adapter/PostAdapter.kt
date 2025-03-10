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
import com.example.vidgram.model.PostModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.min

class PostAdapter(private val context: Context, private val posts: MutableList<PostModel>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.sample_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]

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
        val postImageUrl = post.postImage ?: "" // Get the image URL

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

        // Set Caption
        holder.caption.text = post.postDescription ?: "No caption provided"

        val timestampLong = post.postTimeStamp?.toLongOrNull() ?: 0L
        val formattedTime = if (timestampLong > 0) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            dateFormat.format(Date(timestampLong))
        } else {
            ""
        }
        // Set Timestamp
        holder.time.text = formattedTime
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