package com.example.vidgram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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


    private val _userData = MutableLiveData<UserModel?>()
    val userData: LiveData<UserModel?> get() = _userData

    fun getUserFromDatabase(userID: String) {
        repo.getUserFromDatabase(userID) { user, success, message ->
            if (success) {
                // Only post the new value if it's different from the current one
                _userData.postValue(user)
            } else {
                _userData.postValue(null) // Optional: handle null data case
            }
        }
    }

    // Optionally, call this to clear the LiveData before reloading:
    fun clearUserData() {
        _userData.postValue(null)
    }

    fun editProfile(
        userID: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ) {
        // Call the editProfile method from the repository
        repo.editProfile(userID, data) { success, message ->
            // Pass the result back to the callback
            callback(success, message)
        }
    }

    fun logout(
        callback: (Boolean, String) -> Unit
    ) {

    }

}