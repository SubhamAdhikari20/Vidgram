package com.example.vidgram.repository

import com.example.vidgram.model.Post
import com.example.vidgram.model.PostModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostRepositoryImp:PostRepository {
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var ref : DatabaseReference =database.reference.child("Posts")
    override fun getAllPosts(callback: (List<PostModel>, Boolean, String) -> Unit) {
        ref.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var products = mutableListOf<PostModel>()
                        for (eachData in snapshot.children) {
                            var model = eachData.getValue(PostModel::class.java)
                            if (model != null) {
                                products.add(model)
                            }
                        }
                        callback(products, true, "Post Fetched")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(emptyList(), false, error.message)
                }
            }
        )}

    override fun addPost(postModel: PostModel, callback: (Boolean, String) -> Unit) {
        var id = ref.push().key.toString()
        postModel.postID = id
        ref.child(id).setValue(postModel).addOnCompleteListener{
            if(it.isSuccessful){
                callback(true,"Product Added successfully")
            }
            else{
                callback(false,it.exception?.message.toString())
            }
        }
    }
}