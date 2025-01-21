package com.example.vidgram.model

data class UserModel(
    var userID: String ?= null,
    var fullName:  String ?= null,
    var dob:  String ?= null,
    var gender:  String ?= null,
    var country:  String ?= null,
    var contact: String ?= null,
    var email: String ?= null,
    var password:  String ?= null
)