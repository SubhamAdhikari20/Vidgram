package com.example.vidgram.view.activity.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vidgram.databinding.ActivityLoginBinding
import com.example.vidgram.viewmodel.UserAuthViewModel
import com.example.vidgram.repository.UserAuthRepository
import com.example.vidgram.repository.UserAuthRepositoryImpl
import com.example.vidgram.utils.LoadingDialogUtils
import com.example.vidgram.view.activity.BottomNavigationActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userAuthViewModel: UserAuthViewModel
    private lateinit var loadingDialogUtils: LoadingDialogUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialogUtils =LoadingDialogUtils(this)

        // Initialize ViewModel directly
        val userAuthRepository = UserAuthRepositoryImpl()  // Instantiate Repository
        userAuthViewModel = UserAuthViewModel(userAuthRepository)  // Create ViewModel with repository

        binding.createAccountButton.setOnClickListener {
            val intent = Intent(this, NameSignUpActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            loadingDialogUtils.show()
            val email = binding.emailInputText.text.toString()
            val pass = binding.passwordInputText.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                // Call ViewModel to login
                userAuthViewModel.login(email, pass) { success, message ->
                    if (success) {

                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                        val intent = Intent(this, BottomNavigationActivity::class.java)
                        startActivity(intent)
                        finish() // Close LoginActivity
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            }

            loadingDialogUtils.dismiss()
        }
    }
}
