package com.example.vidgram.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vidgram.model.Post

class SharedViewModel : ViewModel() {
    private val _posts = MutableLiveData<MutableList<Post>>(mutableListOf()) // LiveData for posts
    val posts: LiveData<MutableList<Post>> get() = _posts

    fun addPost(post: Post) {
        _posts.value?.add(post) // Add the new post to the list
        _posts.value = _posts.value // Trigger observers
    }
}
