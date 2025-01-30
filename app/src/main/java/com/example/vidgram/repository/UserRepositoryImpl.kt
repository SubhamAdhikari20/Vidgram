package com.example.vidgram.repository

import android.util.Log
import com.example.vidgram.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserRepositoryImpl:UserRepository {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var reference = database.reference.child("users")

    override fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Login Successful")
            }
            else{
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun signup(
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Sign Up Successful.", auth.currentUser?.uid.toString())
            }
            else{
                callback(false, it.exception?.message.toString(), "")
            }
        }
    }

    override fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Password reset link sent to $email")
            }
            else{
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun addUserToDatabase(
        userID: String,
        userModel: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        reference.child(userID.toString()).setValue(userModel).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true, "Registration Successful")
            }
            else{
                callback(false, it.exception?.message.toString())
            }
        }
    }


    override fun logout(callback: (Boolean, String) -> Unit) {
        try {
            auth.signOut()
            callback(true, "Logout success")
        }
        catch(e: Exception) {
            callback(false, e.message.toString())
        }
    }

    // Authentication Database
    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    // Inside your Repository class
    override fun getUserFromDatabase(
        userID: String,
        callback: (UserModel?, Boolean, String) -> Unit
    ) {
        reference.child(userID).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userModel = snapshot.getValue(UserModel::class.java)
                    Log.d("userId", userModel?.email.toString())

                    // Update LiveData with the fetched user data
                    callback(userModel, true, "Fetched")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }


    override fun editProfile(
        userID: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ) {
        reference.child(userID).updateChildren(data).addOnCompleteListener {
            if (it.isSuccessful){
                callback(true, "Profile edited")
            }
            else{
                callback(false, it.exception?.message.toString())
            }
        }
    }

}