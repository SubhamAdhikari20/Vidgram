package com.example.vidgram.view.activity.authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vidgram.databinding.ActivityLoginBinding
import com.example.vidgram.viewmodel.UserViewModel
import com.example.vidgram.repository.UserRepositoryImpl
import com.example.vidgram.utils.LoadingDialogUtils
import com.example.vidgram.view.activity.BottomNavigationActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var loadingDialogUtils: LoadingDialogUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialogUtils =LoadingDialogUtils(this)

        // Initialize ViewModel directly
        val userRepository = UserRepositoryImpl()  // Instantiate Repository
        userViewModel = UserViewModel(userRepository)  // Create ViewModel with repository

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
                userViewModel.login(email, pass) { success, message ->
                    if (success) {

                        binding.message.text = "login success"
                        binding.message.visibility = View.GONE
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                        val intent = Intent(this, BottomNavigationActivity::class.java)
                        startActivity(intent)
                        finish() // Close LoginActivity
                    } else {
                        binding.message.text = "login failed"
//                        binding.message.visibility = View.GONE
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
