package com.example.vidgram.services

import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.UploadCallback
import android.net.Uri
import android.util.Log
import com.cloudinary.android.callback.ErrorInfo

object CloudinaryService {

    fun uploadImage(uri: Uri, callback: (Boolean, String, String?) -> Unit) {
        try {
            // Upload image to Cloudinary
            val uploadRequest = MediaManager.get().upload(uri).callback(object : UploadCallback {
                override fun onStart(requestId: String?) {
                    // Handle start of upload (e.g., show progress)
                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                    // Optionally, you can track progress here
                }

                override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                    // Retrieve the image URL after successful upload
                    val imageUrl = resultData?.get("secure_url") as? String
                    Log.d("Cloudinary", "Upload successful: ${resultData?.get("url")}")
                    callback(true, "Image uploaded successfully", imageUrl)
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    // Handle errors during upload
                    callback(false, "Error uploading image: ${error?.description}", null)
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                    // Optionally handle retries
                }
            })
            uploadRequest.dispatch()
        } catch (e: Exception) {
            callback(false, "Error: ${e.message}", null)
        }
    }
}
