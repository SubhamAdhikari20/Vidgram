package com.example.vidgram.repository

import com.example.vidgram.model.UserAuthModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserAuthRepositoryImpl:UserAuthRepository {
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
        userModel: UserAuthModel,
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


}