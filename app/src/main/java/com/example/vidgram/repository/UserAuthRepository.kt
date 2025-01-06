package com.example.vidgram.repository

import com.example.vidgram.model.UserAuthModel

interface UserAuthRepository {
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
        userModel: UserAuthModel,
        callback:(Boolean, String) -> Unit
    )

}