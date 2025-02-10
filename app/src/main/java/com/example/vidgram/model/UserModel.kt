package com.example.vidgram.model

data class UserModel(
    var userID: String ?= "",
    var fullName:  String ?= "",
    var dob:  String ?= "",
    var gender:  String ?= "",
    var country:  String ?= "",
    var contact: String ?= "",
    var email: String ?= "",
    var password:  String ?= "",
    var profilePicture: String ?= null,
    var profileCoverImage: String ?= null
)