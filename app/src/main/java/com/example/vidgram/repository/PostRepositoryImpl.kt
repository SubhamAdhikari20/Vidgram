package com.example.vidgram.repository

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import android.util.Log
import com.cloudinary.Cloudinary
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.cloudinary.utils.ObjectUtils
import com.example.vidgram.model.PostModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.InputStream
import java.util.concurrent.Executors

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
            else {
                callback(false, "${it.exception?.message}")
            }
        }

        /*
        val imageUriString = postModel.postImageUrl  // This is the URI string in the postModel
        val imageUri = Uri.parse(imageUriString) // assuming postModel has a postImageUri
        val postDescription = postModel.postDescription

        try {
            var imageUrl : String ?= null
            // Obtain InputStream from the content:// URI
            if (imageUri != null) {
                // Upload image to Cloudinary
                val uploadRequest = MediaManager.get().upload(imageUri).callback(object : UploadCallback {
                    override fun onStart(requestId: String) {
                        // Optional: Show loading indicator
                    }

                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                        // Optional: You can update a progress bar if needed
                    }

                    override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                        imageUrl = resultData["secure_url"] as? String
                        Log.d("Cloudinary", "Upload successful: ${resultData["url"]}")
                    }

                    override fun onError(requestId: String?, error: ErrorInfo?) {
                        callback(false, "Error uploading image: ${error?.description}")
                    }

                    override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                        callback(false, "Error uploading image: ${error?.description}")
                    }
                })

                // Dispatch the upload request
                uploadRequest.dispatch()
            }
//            else if (postDescription != null){
//                // Now add the post to Firebase
//                val postId = reference.push().key.toString()
//                postModel.postId = postId
//
//                reference.child(postId).setValue(postModel).addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        callback(true, "Post added successfully")
//                    }
//                    else {
//                        callback(false, "${it.exception?.message}")
//                    }
//                }
//            }
            else {
                callback(false, "Unable to open image stream.")
            }
            // Set the image URL in the postModel
            postModel.postImageUrl = imageUrl

            // Now add the post to Firebase
            val postId = reference.push().key.toString()
            postModel.postId = postId

            reference.child(postId).setValue(postModel).addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true, "Post added successfully")
                }
                else {
                    callback(false, "${it.exception?.message}")
                }
            }

        }
        catch (e: Exception) {
            callback(false, "Error: ${e.message}")
        }

         */
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

    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "dbukovsi1",
            "api_key" to "718742783263144",
            "api_secret" to "udtElGelBPWkalRKw-RQrnqFRI8"
        )
    )

    override fun uploadPostImage(
        context: Context,
        imageUri: Uri,
        callback: (String?) -> Unit
    ) {
        // Run two methods at the same time. Create new background task
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                // contentResolver is the mediator with third party app
                val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
                var fileName = getFileNameFromUri(context, imageUri)

                fileName = fileName?.substringBeforeLast(".") ?: "uploaded_image"

                val response = cloudinary.uploader().upload(
                    inputStream, ObjectUtils.asMap(
                        "public_id", fileName,
                        "resource_type", "image"
                    )
                )

                var imageUrl = response["url"] as String?

                imageUrl = imageUrl?.replace("http://", "https://")

                Handler(Looper.getMainLooper()).post {
                    callback(imageUrl)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Handler(Looper.getMainLooper()).post {
                    callback(null)
                }
            }
        }
    }

    override fun getFileNameFromUri(
        context: Context,
        uri: Uri
    ): String? {
        var fileName: String? = null
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }


}