package com.example.vidgram.repository

import com.google.firebase.auth.FirebaseAuth

class SignupRepoImp(var auth: FirebaseAuth):SignupRepo {
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
        }    }
}