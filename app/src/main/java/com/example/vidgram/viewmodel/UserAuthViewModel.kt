package com.example.vidgram.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.vidgram.repository.UserAuthRepository
import com.example.vidgram.model.UserAuthModel
import com.google.firebase.auth.FirebaseUser

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

    fun getCurrentUser(): FirebaseUser? {
        return repo.getCurrentUser()
    }


    var _userData = MutableLiveData<UserAuthModel?>()
    var userData = MutableLiveData<UserAuthModel?>()        // continuous observation of userData
        get() = _userData

    fun getUserFromDatabase(
        userID: String,
    ) {
        repo.getUserFromDatabase(userID){
                user, success, message ->
            if(success){
                _userData.value = user
            }

        }
    }

    fun logout(
        callback: (Boolean, String) -> Unit
    ) {

    }

}