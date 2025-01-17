package com.example.vidgram.model

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val username: String,        // Username of the post author
    val userAvatar: String,         // Resource ID for the user's avatar image
    val postImage: String,          // Resource ID for the post image
    val caption: String,          // Caption of the post
    val time: String,          // Time of the post
    val like: String,          // Like of the post
    val dislike: String,         // DisLike of the post
    val comment: String,         // Comment of the post
    val share: String,         // Share of the post
) : Parcelable
