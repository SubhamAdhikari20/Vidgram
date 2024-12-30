package com.example.vidgram.view.model

data class Post(
    val username: String,        // Username of the post author
    val userAvatar: Int,         // Resource ID for the user's avatar image
    val postImage: Int,          // Resource ID for the post image
    val caption: String          // Caption of the post
)
