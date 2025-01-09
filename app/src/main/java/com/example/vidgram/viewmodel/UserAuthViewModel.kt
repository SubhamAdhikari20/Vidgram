package com.example.vidgram.viewmodel

import com.example.vidgram.repository.UserAuthRepository
import com.example.vidgram.model.UserAuthModel

class UserAuthViewModel(private val repo: UserAuthRepository) {
    // Request to Repository Interface
    fun login(
        email:String,
        password:String,
        callback:(Boolean,String) -> Unit
    ){
        repo.login(email, password, callback)
    }

    // CRUD => CUD | Different for retrieve
    fun signup(
        email:String,
        password:String,
        callback:(Boolean,String, String) -> Unit
    ){
        repo.signup(email, password, callback)
    }

    fun forgetPassword(
        email:String,
        callback:(Boolean,String) -> Unit
    ){
        repo.forgetPassword(email, callback)
    }

    fun addUserToDatabase(
        userID:String,
        userModel: UserAuthModel,
        callback:(Boolean, String) -> Unit
    ){
        repo.addUserToDatabase(userID, userModel, callback)
    }
}