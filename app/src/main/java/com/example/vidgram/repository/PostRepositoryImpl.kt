package com.example.vidgram.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.vidgram.model.PostModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostRepositoryImpl : PostRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val reference: DatabaseReference = database.reference.child("posts")     // reference variable has the access to products table




    override  fun addPost(
        postModel: PostModel,
        context: Context,  // Add the Context parameter

        callback: (Boolean, String) -> Unit
    ) {
        val imageUriString = postModel.postImaqe  // This is the URI string in the postModel
        val imageUri = Uri.parse(imageUriString) // assuming postModel has a postImageUri

        try {
            // Obtain InputStream from the content:// URI

            if (imageUri != null) {
                // Upload image to Cloudinary
                val uploadRequest =
                    MediaManager.get().upload(imageUri).callback(object : UploadCallback {
                        override fun onStart(requestId: String) {
                            // Optional: Show loading indicator
                        }

                        override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                            // Optional: You can update a progress bar if needed
                        }

                        override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                            val imageUrl = resultData["secure_url"] as? String
                            Log.d("Cloudinary", "Upload successful: ${resultData["url"]}")


                            if (imageUrl != null) {
                                // Set the image URL in the postModel
                                postModel.postImaqe = imageUrl

                                // Now add the post to Firebase
                                val postId = reference.push().key.toString()
                                postModel.postId = postId

                                reference.child(postId).setValue(postModel).addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        callback(true, "Post added successfully")
                                    } else {
                                        callback(false, "${it.exception?.message}")
                                    }
                                }
                            } else {
                                callback(false, "Image upload failed. Please try again.")
                            }
                        }

                        override fun onError(requestId: String, error: ErrorInfo) {
                            callback(false, "Error uploading image: ${error.description}")
                        }

                        override fun onReschedule(requestId: String, error: ErrorInfo) {
                            // Optional: Handle the case when upload is rescheduled
                        }
                    })

                // Dispatch the upload request
                uploadRequest.dispatch()

            } else {
                callback(false, "Unable to open image stream.")
            }

        } catch (e: Exception) {
            callback(false, "Error: ${e.message}")
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