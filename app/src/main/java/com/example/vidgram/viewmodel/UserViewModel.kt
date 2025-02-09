package com.example.vidgram.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.vidgram.model.UserChatInfo
import com.example.vidgram.repository.UserRepository
import com.example.vidgram.model.UserModel
import com.google.firebase.auth.FirebaseUser

class UserViewModel(private val repo: UserRepository) {
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
        userModel: UserModel,
        callback:(Boolean, String) -> Unit
    ){
        repo.addUserToDatabase(userID, userModel, callback)
    }

    fun getCurrentUser(): FirebaseUser? {
        return repo.getCurrentUser()
    }


    var _userData = MutableLiveData<UserModel?>()
    var userData = MutableLiveData<UserModel?>()        // continuous observation of userData
        get() = _userData

    fun getUserFromDatabase(
        userId: String,
    ) {
        repo.getUserFromDatabase(userId){
            user, success, message ->
            if(success){
                _userData.value = user
            }

        }
    }


    var _getAllusers = MutableLiveData<List<UserModel>?>()
    var getAllusers = MutableLiveData<List<UserModel>?>()
        get() = _getAllusers

    var _loadingAllUsers = MutableLiveData<Boolean?>()
    var loadingAllUsers = MutableLiveData<Boolean?>()
        get() = _loadingAllUsers

    fun getAllUsers(
        callback: (ArrayList<UserModel>?, Boolean, String) -> Unit
    ){
        _loadingAllUsers.value = true
        repo.getAllUsers(){
                users, success, message ->
            if (success){
                _getAllusers.value = users
                _loadingAllUsers.value = false
            }
        }
    }

    fun logout(
        callback: (Boolean, String) -> Unit
    ) {

    }

}