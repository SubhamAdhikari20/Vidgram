package com.example.vidgram.repository
import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.vidgram.model.StoryModel
import com.example.vidgram.services.CloudinaryService
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class StoryRepositoryImpl : StoryRepository {
    val databse : FirebaseDatabase = FirebaseDatabase.getInstance()
    val reference : DatabaseReference = databse.reference.child("stories")
    override fun addStory(
        storyModel: StoryModel,
        callback: (Boolean, String) -> Unit
    ) {
        val imageUriString = storyModel.storyImage
        val imageUri = Uri.parse(imageUriString)

        // Call the CloudinaryService to upload the image
        CloudinaryService.uploadImage(imageUri) { success, message, imageUrl ->
            if (success) {
                // Set the uploaded image URL to the story model
                storyModel.storyImage = imageUrl

                // Generate story ID and save the story to Firebase
                val storyId = reference.push().key.toString()
                storyModel.storyId = storyId
                reference.child(storyId).setValue(storyModel).addOnCompleteListener {
                    if (it.isSuccessful) {
                        callback(true, "Story added successfully")
                    } else {
                        callback(false, it.exception?.message.toString())
                    }
                }
            } else {
                // Handle error from Cloudinary
                callback(false, message)
            }
        }
    }


    override fun updateStory(
        storyId: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ) {

        reference.child(storyId).updateChildren(data).addOnCompleteListener {
            if (it.isSuccessful){
                callback(true, "Story updated successfully")
            }
            else{
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun deleteStory(
        storyId: String,
        callback: (Boolean, String) -> Unit
    ) {
        reference.child(storyId).removeValue().addOnCompleteListener {
            if (it.isSuccessful){
                callback(true, "Story deleted successfully")
            }
            else{
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun getStoryById(
        storyId: String,
        callback: (StoryModel?, Boolean, String) -> Unit
    ) {
        reference.child(storyId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val storyModel = snapshot.getValue(StoryModel::class.java)
                    callback(storyModel, true, "Story fetched successfully")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false,error.message)
            }

        })
    }

    override fun getAllStory(
        callback: (List<StoryModel>?, Boolean, String) -> Unit
    ) {
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val stories = mutableListOf<StoryModel>()
                    for (eachData in snapshot.children){
                        val storyModel = eachData.getValue(StoryModel::class.java)
                        if (storyModel != null){
                            stories.add(storyModel)
                        }
                    }
                    callback(stories, true, "All stories fetched successfully")
                }

            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false,error.message)
            }

        })
    }


}