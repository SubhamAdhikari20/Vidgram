package com.example.vidgram.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.vidgram.model.PostModel
import com.example.vidgram.services.CloudinaryService
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostRepositoryImpl : PostRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val reference: DatabaseReference =
        database.reference.child("posts")     // reference variable has the access to products table


    override fun addPost(
        postModel: PostModel,
        callback: (Boolean, String) -> Unit
    ) {
        val imageUriString = postModel.postImage  // URI string from postModel
        val imageUri = Uri.parse(imageUriString)  // Assuming postModel has a postImageUri

        // Call CloudinaryService to upload the image
        CloudinaryService.uploadImage(imageUri) { success, message, imageUrl ->
            if (success) {
                // Set the uploaded image URL to the postModel
                postModel.postImage = imageUrl

                // Generate a unique post ID and save the post to Firebase
                val postId = reference.push().key.toString()
                postModel.postId = postId

                reference.child(postId).setValue(postModel).addOnCompleteListener {
                    if (it.isSuccessful) {
                        callback(true, "Post added successfully")
                    } else {
                        callback(false, it.exception?.message.toString())
                    }
                }
            } else {
                // Handle failure from Cloudinary upload
                callback(false, message)
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
            } else {
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
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun getPostById(
        postId: String,
        callback: (PostModel?, Boolean, String) -> Unit
    ) {
        reference.child(postId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val postModel = snapshot.getValue(PostModel::class.java)
                    callback(postModel, true, "Post fetched successfully")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }

        })
    }

    override fun getAllPost(callback: (List<PostModel>?, Boolean, String) -> Unit) {
        reference
            .orderByChild("timestamp")  // Replace with the field you want to sort by
            .limitToLast(50)  // Limit the results to the most recent 50 posts, if needed
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val posts = mutableListOf<PostModel>()
                        for (eachData in snapshot.children) {
                            val postModel = eachData.getValue(PostModel::class.java)
                            if (postModel != null) {
                                posts.add(postModel)
                            }
                        }

                        // Since we're using limitToLast(), posts are in ascending order. Reverse to get descending order.
                        callback(posts.reversed(), true, "All posts fetched successfully")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(null, false, error.message)
                }
            })
    }

    override fun getPostsByUsername(username: String, callback: (List<PostModel>?) -> Unit) {
        val postsList = mutableListOf<PostModel>()

        // Query the posts node
        reference.child("posts").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Iterate through each postId node
                    for (postSnapshot in snapshot.children) {
                        // Get the post data from the postId node
                        val post = postSnapshot.getValue(PostModel::class.java)

                        // Check if the post contains the correct username
                        if (post?.username == username) {
                            postsList.add(post)
                        }
                    }
                }

                // Once the posts are fetched, call the callback with the list
                callback(postsList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("PostRepository", "Failed to load posts", error.toException())
            }
        })
    }


}




