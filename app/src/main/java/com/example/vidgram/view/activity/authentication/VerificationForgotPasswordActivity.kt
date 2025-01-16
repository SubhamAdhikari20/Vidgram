package com.example.vidgram.view.activity.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vidgram.R
import com.example.vidgram.databinding.ActivityVerificationForgotPasswordBinding
import com.example.vidgram.repository.UserRepositoryImpl
import com.example.vidgram.utils.LoadingDialogUtils
import com.example.vidgram.viewmodel.UserViewModel

class VerificationForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityVerificationForgotPasswordBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var loadingDialogUtils: LoadingDialogUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityVerificationForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialogUtils = LoadingDialogUtils(this)
        val bundle = intent.extras
        val repo = UserRepositoryImpl()
        userViewModel = UserViewModel(repo)


        // Handle back button click
        binding.arrowButtonFP2.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            intent.putExtras(bundle!!)
            startActivity(intent)
            finish()
        }

        binding.enterCodeButtonFP.setOnClickListener {
            loadingDialogUtils.show()
            val code1 = binding.code1.text.toString()
            val code2 = binding.code2.text.toString()
            val code3 = binding.code3.text.toString()
            val code4 = binding.code4.text.toString()
            val code5 = binding.code5.text.toString()
            val code6 = binding.code6.text.toString()

            if (code1.isNotEmpty() && code2.isNotEmpty() && code3.isNotEmpty() && code4.isNotEmpty() && code5.isNotEmpty() && code5.isNotEmpty() && code6.isNotEmpty()) {
                val intent = Intent(this@VerificationForgotPasswordActivity, ChangePasswordActivity::class.java)
                intent.putExtras(bundle!!)
                startActivity(intent)
                finish()
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