package com.example.vidgram.repository

import androidx.appcompat.view.ActionMode.Callback
import com.example.vidgram.model.PostModel

interface PostRepository {

    //callback
    //success:true :Boolean
    //message :"Post added" :string
    fun getAllPosts(callback:(List<PostModel>, Boolean, String) ->Unit)

    fun addPost(postModel: PostModel, callback: (Boolean, String) -> Unit)
}