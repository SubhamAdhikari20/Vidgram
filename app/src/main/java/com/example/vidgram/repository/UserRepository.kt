package com.example.vidgram.repository

import com.example.vidgram.model.PostModel
import com.example.vidgram.model.UserModel
import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    fun login(
        email:String,
        password:String,
        callback:(Boolean,String) -> Unit
    )

    fun signup(
        email:String,
        password:String,
        callback:(Boolean,String, String) -> Unit
    )

    fun forgetPassword(
        email:String,
        callback:(Boolean,String) -> Unit
    )

    fun addUserToDatabase(
        userID:String,
        userModel: UserModel,
        callback:(Boolean, String) -> Unit
    )

    fun logout(
        callback: (Boolean, String) -> Unit
    )

    // Authentication Database
    fun getCurrentUser(

    ) : FirebaseUser?

    // Real-time Database
    fun getUserFromDatabase(
        userID: String,
        callback: (UserModel?, Boolean, String) -> Unit
    )

    fun getAllUsers(
        callback: (ArrayList<UserModel>?, Boolean, String) -> Unit
    )

    fun editProfile(
        userID: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    )

    fun deleteAccount(
        userId: String,
        callback: (Boolean, String) -> Unit
    )

}