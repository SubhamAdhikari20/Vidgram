package com.example.vidgram.view.activity.authentication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
    private lateinit var sharePreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialogUtils =LoadingDialogUtils(this)
        sharePreferences = getSharedPreferences("user", Context.MODE_PRIVATE)

        // Initialize ViewModel directly
        val userRepository = UserRepositoryImpl()  // Instantiate Repository
        userViewModel = UserViewModel(userRepository)  // Create ViewModel with repository

        // Auto Login
        val editor = sharePreferences
        val savedEmail : String = editor.getString("email", null).toString()
        val savedPassword : String = editor.getString("password", null).toString()

        if (!savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
            autoLogin(savedEmail, savedPassword)
        }

        binding.createAccountButton.setOnClickListener {
            val intent = Intent(this, NameSignUpActivity::class.java)
            startActivity(intent)
        }

        binding.forgotPasswordTextView.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
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

                        if (binding.rememberMe.isChecked){
                            val editor = sharePreferences.edit()
                            editor.putString("email", email)
                            editor.putString("password", pass)
                            editor.apply()

                            val intent = Intent(
                                this@LoginActivity, BottomNavigationActivity::class.java
                            )
                            intent.putExtra("email", email)
                            intent.putExtra("password", pass)
                            startActivity(intent)
                            finish()
                        }
                        else {
                            // Clear credentials if "Remember Me" is unchecked
                            sharePreferences.edit().clear().apply()
                        }

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

    private fun autoLogin(email: String, password: String) {
        loadingDialogUtils.show()
        userViewModel.login(email, password) { success, message ->
            loadingDialogUtils.dismiss()
            if (success) {
                startActivity(Intent(this@LoginActivity, BottomNavigationActivity::class.java))
                finish()
            }
            else {
                // Clear invalid credentials
                sharePreferences.edit().clear().apply()
            }
        }
    }
}
