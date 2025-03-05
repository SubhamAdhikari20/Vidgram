package com.example.vidgram.view.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import com.example.vidgram.R
import com.example.vidgram.databinding.ActivityEditProfileBinding
import com.example.vidgram.model.UserModel
import com.example.vidgram.repository.UserRepositoryImpl
import com.example.vidgram.utils.LoadingDialogUtils
import com.example.vidgram.view.activity.authentication.LoginActivity
import com.example.vidgram.viewmodel.UserViewModel


class EditProfileActivity : AppCompatActivity() {
    lateinit var binding : ActivityEditProfileBinding
    lateinit var  userViewModel: UserViewModel
    lateinit var userId : String
    private lateinit var sharePreferences : SharedPreferences
    private lateinit var loadingDialogUtils: LoadingDialogUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow_resized)

        loadingDialogUtils =LoadingDialogUtils(this)

        val repo = UserRepositoryImpl()
        userViewModel= UserViewModel(repo)
        userViewModel.clearUserData()
        val currentUser = userViewModel.getCurrentUser()


        currentUser.let{    // it -> currentUser
            Log.d("current user userId",it?.uid.toString())
            userViewModel.getUserFromDatabase(it?.uid.toString())
            userId = it?.uid.toString()
        }

//        userViewModel.getUserFromDatabase(currentUser)
//

        userViewModel.userData.observe(this) { user ->
            // Ensure the 'user' variable is properly defined here
            user?.let { userModel ->
                // Now use 'userModel' instead of 'it' to reference the UserModel
                binding.nameTextView.text = userModel.fullName ?: "No Name"
                binding.emailTextView.text = userModel.email ?: "No Email"
                binding.contactTextView.text = userModel.contact ?: "No Contact"
                binding.passwordTextView.text = userModel.password ?: "No Password"

                // Handling onClick events
                binding.nameProfileCardView.setOnClickListener { openEditProfileDetail("Name", userModel.fullName ?: "") }
                binding.bioProfileCardView.setOnClickListener { openEditProfileDetail("Bio", "MISIIGIN FIELD") }
                binding.emailProfileCardView.setOnClickListener { openEditProfileDetail("Email", userModel.email ?: "") }
                binding.contactProfileCardView.setOnClickListener { openEditProfileDetail("Contact", userModel.contact ?: "") }
                binding.passwordProfileCardView.setOnClickListener { openEditProfileDetail("Password", "********") }
            }
        }

        binding.logoutBtn.setOnClickListener {
            confirmLogoutDialog()
        }

        binding.deleteAccountBtn.setOnClickListener {
            confirmDeleteAccountDialog(userId)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.editProfileLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    fun openEditProfileDetail(fieldType: String, fieldValue: String) {
        val intent = Intent(this, EditProfileDetailActivity::class.java)
        intent.putExtra("userID",userId)
        intent.putExtra("FIELD_TYPE", fieldType)
        intent.putExtra("FIELD_VALUE", fieldValue)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        // Go back to the previous fragment or activity
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun confirmLogoutDialog(){
        AlertDialog.Builder(this@EditProfileActivity)
            .setTitle("Confirm Logout")
            .setMessage("Do you want to logout?")
            .setPositiveButton("Logout") { dialog, _ ->
                loadingDialogUtils.show()
                userViewModel.logout {
                    success, message ->
                    if (success){
                        loadingDialogUtils.dismiss()
                        dialog.dismiss()
                        Toast.makeText(
                            this@EditProfileActivity,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()

                        sharePreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
                        sharePreferences.edit().clear().apply()
                        val intent = Intent(this@EditProfileActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        loadingDialogUtils.dismiss()
                        dialog.dismiss()
                        Toast.makeText(
                            this@EditProfileActivity,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun confirmDeleteAccountDialog(userId: String){
        AlertDialog.Builder(this)
            .setTitle("Confirm Delete Account")
            .setMessage("Are you sure you want to delete your account?")
            .setPositiveButton("Delete") { dialog, _ ->
                loadingDialogUtils.show()
                userViewModel.deleteAccount(userId) {
                        success, message ->
                    if (success){
                        loadingDialogUtils.show()
                        dialog.dismiss()
                        Toast.makeText(
                            this@EditProfileActivity,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()

                        sharePreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
                        sharePreferences.edit().clear().apply()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        loadingDialogUtils.dismiss()
                        dialog.dismiss()
                        Toast.makeText(
                            this@EditProfileActivity,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}