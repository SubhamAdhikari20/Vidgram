package com.example.vidgram.view.activity.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vidgram.R
import com.example.vidgram.databinding.ActivityChangePasswordBinding
import com.example.vidgram.repository.UserRepositoryImpl
import com.example.vidgram.utils.LoadingDialogUtils
import com.example.vidgram.viewmodel.UserViewModel

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChangePasswordBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var loadingDialogUtils: LoadingDialogUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialogUtils = LoadingDialogUtils(this)
        val bundle = intent.extras
        val repo = UserRepositoryImpl()
        userViewModel = UserViewModel(repo)


        // Handle back button click
        binding.arrowButtonFP3.setOnClickListener {
            val intent = Intent(this, VerificationForgotPasswordActivity::class.java)
            intent.putExtras(bundle!!)
            startActivity(intent)
            finish()
        }

        binding.confirmButtonFP.setOnClickListener {
            loadingDialogUtils.show()
            val newPasswordActivity = binding.newPasswordInputText.text.toString()
            val confirmPasswordActivity = binding.confirmPasswordInputText.text.toString()

            val email = bundle?.getString("email").toString()

            if (newPasswordActivity.isNotEmpty() && confirmPasswordActivity.isNotEmpty()) {
                // Call ViewModel to login
                userViewModel.forgetPassword(email) { success, message ->
                    if (success) {
                        loadingDialogUtils.dismiss()
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

                        val intent = Intent(this@ChangePasswordActivity, LoginActivity::class.java)
                        intent.putExtras(bundle!!)
                        startActivity(intent)
                        finish()

                    } else {
                        loadingDialogUtils.dismiss()
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    }
                }
            }
            else {
                loadingDialogUtils.dismiss()
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            }

        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}