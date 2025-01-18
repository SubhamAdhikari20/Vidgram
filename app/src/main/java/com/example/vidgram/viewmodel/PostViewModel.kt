package com.example.vidgram.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vidgram.R
import com.example.vidgram.model.Post

class PostViewModel : ViewModel() {
    private val _posts = MutableLiveData<List<Post>>(mutableListOf())  // Holds the list of posts
    val posts: LiveData<List<Post>> = _posts  // Public LiveData for observation

    fun addNewPost(post: Post) {
        val currentPosts = _posts.value?.toMutableList() ?: mutableListOf()
        currentPosts.add(0, post)  // Add the new post to the top
        _posts.value = currentPosts  // Update the LiveData to notify observers
        Log.d("post","Post added")
    }

//    fun loadPosts() {
//        val posts = listOf(
//            Post(
//                "John Doe",
//                "android.resource://com.example.vidgram/${R.drawable.my_story_icon}",
//                "android.resource://com.example.vidgram/${R.drawable.person1}",
//                "Enjoying the sunset!", "12:00", "24k", "1k", "1,080", "2.4k"
//            ),
//            // Add other posts here...
//        )
//        Log.d("PostViewModel", "Loading posts: ${posts.size} posts")
//
//        _posts.postValue(posts)  // Update the posts list asynchronously
//    }
}