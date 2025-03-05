package com.example.vidgram.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import com.example.vidgram.R
import com.example.vidgram.databinding.ActivityEditProfileBinding
import com.example.vidgram.model.UserModel
import com.example.vidgram.repository.UserRepositoryImpl
import com.example.vidgram.viewmodel.UserViewModel


class EditProfileActivity : AppCompatActivity() {
    lateinit var binding : ActivityEditProfileBinding
    lateinit var  userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow_resized)

//        binding.nameProfileCardView.setOnClickListener {
//            val intent = Intent(this@EditProfileActivity, NameEditProfileActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.bioProfileCardView.setOnClickListener {
//            val intent = Intent(this@EditProfileActivity, BioEditProfileActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.emailProfileCardView.setOnClickListener {
//            val intent = Intent(this@EditProfileActivity, EmailEditProfileActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.contactProfileCardView.setOnClickListener {
//            val intent = Intent(this@EditProfileActivity, ContactEditProfileActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.passwordProfileCardView.setOnClickListener {
//            val intent = Intent(this@EditProfileActivity, PasswordEditProfileActivity::class.java)
//            startActivity(intent)
//        }
        val repo = UserRepositoryImpl()
        userViewModel= UserViewModel(repo)
        userViewModel.clearUserData()

        val currentUserID = "v4xWlr2zR6hwoXG4QhezWAUnHmx1"
        currentUserID.let { userViewModel.getUserFromDatabase(it) }
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


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.editProfileLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    fun openEditProfileDetail(fieldType: String, fieldValue: String) {
        val intent = Intent(this, EditProfileDetailActivity::class.java)
        intent.putExtra("FIELD_TYPE", fieldType)
        intent.putExtra("FIELD_VALUE", fieldValue)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        // Go back to the previous fragment or activity
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}