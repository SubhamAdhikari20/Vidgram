package com.example.vidgram.repository

interface SignupRepo {
    fun signup(
        email:String,
        password:String,
        callback:(Boolean,String, String) -> Unit
    )

}