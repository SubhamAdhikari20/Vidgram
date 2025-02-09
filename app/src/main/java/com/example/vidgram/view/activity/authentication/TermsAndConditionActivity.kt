package com.example.vidgram.view.activity.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vidgram.R
import com.example.vidgram.databinding.ActivityTermsAndConditionBinding
import com.example.vidgram.model.UserModel
import com.example.vidgram.repository.UserRepositoryImpl
import com.example.vidgram.utils.LoadingDialogUtils
import com.example.vidgram.viewmodel.UserViewModel

class TermsAndConditionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTermsAndConditionBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var loadingDialogUtils: LoadingDialogUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTermsAndConditionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialogUtils = LoadingDialogUtils(this)
        val bundle = intent.extras
        val repo = UserRepositoryImpl() // Assuming the repository is created here
        userViewModel = UserViewModel(repo)

        // Handle back button click
        binding.arrowButton7.setOnClickListener {
            val intent = Intent(this, PasswordSignUpActivity::class.java)
            intent.putExtras(bundle!!)
            startActivity(intent)
            finish()
        }

        // Handle agree button click
        binding.agreeButton.setOnClickListener {
            loadingDialogUtils.show()
            val userDetails = UserModel(
                fullName = bundle?.getString("fullName"),
                dob = bundle?.getString("dob"),
                gender = bundle?.getString("gender"),
                country = bundle?.getString("country"),
                contact = bundle?.getString("phonenumber"),
                email = bundle?.getString("email"),
                password = bundle?.getString("password"),

            )

            Log.d("UserDetails", "Full Name: ${userDetails.fullName}")
            Log.d("UserDetails", "Date of Birth: ${userDetails.dob}")
            Log.d("UserDetails", "Gender: ${userDetails.gender}")
            Log.d("UserDetails", "Country: ${userDetails.country}")
            Log.d("UserDetails", "Contact: ${userDetails.contact}")
            Log.d("UserDetails", "Email: ${userDetails.email}")
            Log.d("UserDetails", "Password: ${userDetails.password}")

            // Call the signup method with the user details
            userViewModel.signup(
                userDetails.email ?: "",
                userDetails.password ?: ""
            ) { success, message, userID ->
                if (success) {
                    // Handle success
                    val userModel = UserModel(userID, userDetails.fullName,
                        userDetails.dob,
                        userDetails.gender,
                        userDetails.country,
                        userDetails.contact,
                        userDetails.email,
                        userDetails.password)
                    addUser(userModel)
                    Toast.makeText(this, "Signup Successful: $message", Toast.LENGTH_SHORT).show()
                    val intent = Intent(
                        this,
                        LoginActivity::class.java
                    )  // Assuming you want to go to MainActivity
                    startActivity(intent)
                    finish()  // Close current activity
                } else {
                    // Handle failure
                    Toast.makeText(this, "Signup Failed: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Set window insets for edge-to-edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun addUser(userModel: UserModel) {

        userViewModel.addUserToDatabase(userModel.userID?:"", userModel) { success, message ->

            if (success) {

                Toast.makeText(
                    this,
                    message, Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this,
                    message, Toast.LENGTH_LONG
                ).show()

            }
            loadingDialogUtils.dismiss()

        }
    }
}