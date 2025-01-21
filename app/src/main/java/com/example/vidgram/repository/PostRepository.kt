package com.example.vidgram.repository

import android.content.Context
import android.net.Uri
import com.example.vidgram.model.PostModel

interface PostRepository {
    // {
    // "success": true
    // "message" "Product Added successfully"
    //  }

    fun addPost(
        postModel: PostModel,
        callback: (Boolean, String) -> Unit
    )

    fun updatePost(
        postId:String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    )

    fun deletePost(
        postId:String,
        callback: (Boolean, String) -> Unit
    )

    fun getPostById(
        postId:String,
        callback: (PostModel?, Boolean, String) -> Unit
    )

    fun getAllPost(
        callback: (List<PostModel>?, Boolean, String) -> Unit
    )
}