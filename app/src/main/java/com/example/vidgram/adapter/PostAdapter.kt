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
    private val postList: List<Post> = mutableListOf(),
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.sample_post, parent, false)
        return PostViewHolder(itemView)
    }

    override fun getItemCount() = postList.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]

        // Bind text data
        holder.username.text = post.username
        holder.caption.text = post.caption
        holder.time.text = post.time
        holder.like.text = post.like
        holder.dislike.text = post.dislike
        holder.comment.text = post.comment
        holder.share.text = post.share

//        // Bind user avatar and post image with Glide
//        val context = holder.itemView.context
//        val density = context.resources.displayMetrics.density
//        val avatarSize = (40 * density).toInt()  // 40dp converted to pixels
//
//        Glide.with(context)
//            .load(post.userAvatar) // Load image from URI string
//            .placeholder(R.drawable.user) // Placeholder if image not found
//            .override(avatarSize, avatarSize) // Resize to 40dp
//            .into(holder.userAvatar)
//
//        Glide.with(context)
//            .load(post.postImage) // Load image from URI string
//            .placeholder(R.drawable.husky) // Placeholder if image not found
//            .centerCrop()
//            .into(holder.postImage)

        // Set click listeners
        holder.comment.setOnClickListener {
            onCommentClick(post)
        }
        holder.commentButton.setOnClickListener {
            onCommentClick(post)
        }
    }

//    fun addPost(post: Post) {
//        postList.add(post) // Add the post to the list
//        notifyItemInserted(postList.size - 1) // Notify RecyclerView about the new item
//    }
//    // Update posts list and refresh RecyclerView
//    fun updatePosts(newPosts: List<Post>) {
//        (postList as MutableList).clear()  // Clear the existing list
//        postList.addAll(newPosts)  // Add new posts
////        notifyDataSetChanged()  // Notify adapter that the data has changed
//    }

}
