package com.example.vidgram.view.model

data class Post(
    val username: String,        // Username of the post author
    val userAvatar: Int,         // Resource ID for the user's avatar image
    val postImage: Int,          // Resource ID for the post image
    val caption: String,          // Caption of the post
    val time: String,          // Time of the post
    val like: String,          // Like of the post
    val dislike: String,         // DisLike of the post
    val comment: String,         // Comment of the post
    val share: String,         // Share of the post
)
