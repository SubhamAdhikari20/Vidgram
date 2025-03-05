package com.example.vidgram.view.activity.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vidgram.R
import com.example.vidgram.databinding.ActivityForgotPasswordBinding
import com.example.vidgram.repository.UserRepositoryImpl
import com.example.vidgram.utils.LoadingDialogUtils
import com.example.vidgram.viewmodel.UserViewModel

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityForgotPasswordBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var loadingDialogUtils: LoadingDialogUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow_resized)

        loadingDialogUtils = LoadingDialogUtils(this)
        val bundle = intent.extras
        val repo = UserRepositoryImpl()
        userViewModel = UserViewModel(repo)

        binding.sendCodeButtonFP.setOnClickListener {
            loadingDialogUtils.show()
            val email = binding.emailFPInputText.text.toString()

            if (email.isNotEmpty()){
                userViewModel.forgetPassword(email){
                        success, message ->
                    if (success){
                        loadingDialogUtils.dismiss()
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else{
                        loadingDialogUtils.dismiss()
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            else{
                loadingDialogUtils.dismiss()
                Toast.makeText(
                    this@ForgotPasswordActivity,
                    "Please, fill in the email fields!!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}