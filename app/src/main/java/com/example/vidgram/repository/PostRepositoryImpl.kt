package com.example.vidgram.repository

import com.example.vidgram.model.PostModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostRepositoryImpl : PostRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val reference: DatabaseReference = database.reference.child("posts")     // reference variable has the access to products table


    override fun addPost(
        postModel: PostModel,
        callback: (Boolean, String) -> Unit
    ) {
        val postId = reference.push().key.toString()
        postModel.postId = postId

        reference.child(postId).setValue(postModel).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Post added successfully")
            }
            else{
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun updatePost(
        postId: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ) {
        reference.child(postId).updateChildren(data).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Post updated successfully")
            }
            else{
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun deletePost(
        postId: String,
        callback: (Boolean, String) -> Unit
    ) {
        reference.child(postId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Post deleted successfully")
            }
            else{
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun getPostById(
        postId: String,
        callback: (PostModel?, Boolean, String) -> Unit
    ) {
        reference.child(postId).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val postModel = snapshot.getValue(PostModel::class.java)
                    callback(postModel, true, "Post fetched successfully")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }

        })
    }

    override fun getAllPost(
        callback: (List<PostModel>?, Boolean, String) -> Unit
    ) {
        reference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    var posts = mutableListOf<PostModel>()
                    for (eachData in snapshot.children){
                        var postModel = eachData.getValue(PostModel::class.java)
                        if (postModel != null){
                            posts.add(postModel)
                        }
                    }

                    callback(posts, true, "All posts fetched successfully")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, true, error.message)
            }

        })
    }
}