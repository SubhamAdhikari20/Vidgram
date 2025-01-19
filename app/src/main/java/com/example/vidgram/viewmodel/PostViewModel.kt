package com.example.vidgram.viewmodel

import androidx.compose.runtime.produceState
import androidx.lifecycle.MutableLiveData
import com.example.vidgram.model.PostModel
import com.example.vidgram.repository.PostRepository

class PostViewModel(val repo: PostRepository) {

    fun addPost(
        postModel: PostModel,
        callback: (Boolean, String) -> Unit
    ){
        repo.addPost(postModel, callback)
    }

    fun updatePost(
        postId:String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ){
        repo.updatePost(postId, data, callback)
    }

    fun deletePost(
        postId:String,
        callback: (Boolean, String) -> Unit
    ){
        repo.deletePost(postId, callback)
    }

    var _posts = MutableLiveData<PostModel?>()
    var posts = MutableLiveData<PostModel?>()
        get() = _posts

    var _loadingPostById = MutableLiveData<Boolean>()
    var loadingPostById = MutableLiveData<Boolean>()
        get() = _loadingPostById


    fun getPostById(
        postId:String,
    ){
        _loadingPostById.value = true
        repo.getPostById(postId){
            post, success, message ->
            if (success){
                _posts.value = post
                _loadingPostById.value = false
            }
        }
    }

    var _loadingAllPost = MutableLiveData<Boolean>()
    var loadingAllPost = MutableLiveData<Boolean>()
        get() = _loadingAllPost

    var _getAllPosts = MutableLiveData<List<PostModel>?>()
    var getAllPosts = MutableLiveData<List<PostModel>?>()
        get() = _getAllPosts


    fun getAllPost(){
        _loadingPostById.value = true
        repo.getAllPost(){
            posts, success, message ->
            if (success){
                _getAllPosts.value = posts
                _loadingPostById.value = false
            }
        }
    }
}