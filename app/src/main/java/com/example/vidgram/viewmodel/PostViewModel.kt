package com.example.vidgram.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vidgram.R
import com.example.vidgram.model.Post
import com.example.vidgram.model.PostModel
import com.example.vidgram.repository.PostRepositoryImp

class PostViewModel (val postrepo : PostRepositoryImp) {

    var _posts = MutableLiveData<PostModel>()
    var posts = MutableLiveData<PostModel>()
        get() = _posts

    var _getAllPosts = MutableLiveData<List<PostModel>>()
    var getAllPosts = MutableLiveData<List<PostModel>>()
        get() = _getAllPosts
    fun getPosts(callback:(List<PostModel>, Boolean, String) ->Unit){
            postrepo.getAllPosts(){
                posts,success,message->
                if(success){
                    _getAllPosts.value = posts

                }
            }
    }

    fun addPost(postModel: PostModel, callback: (Boolean, String) -> Unit){
        postrepo.addPost(postModel,callback)
    }
}