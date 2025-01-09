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
import com.example.vidgram.repository.UserAuthRepositoryImpl
import com.example.vidgram.utils.LoadingDialogUtils
import com.example.vidgram.viewmodel.UserAuthViewModel

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityForgotPasswordBinding
    private lateinit var userAuthViewModel: UserAuthViewModel
    private lateinit var loadingDialogUtils: LoadingDialogUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialogUtils = LoadingDialogUtils(this)
        val bundle = intent.extras
        val repo = UserAuthRepositoryImpl()
        userAuthViewModel = UserAuthViewModel(repo)


        // Handle back button click
        binding.arrowButtonFP1.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtras(bundle!!)
            startActivity(intent)
            finish()
        }

        binding.sendCodeButtonFP.setOnClickListener {
            loadingDialogUtils.show()
            val email = binding.emailFPInputText.text.toString()

            if (email.isNotEmpty()) {

                val intent = Intent(this@ForgotPasswordActivity, VerificationForgotPasswordActivity::class.java)

                val bundle = Bundle()
                bundle.putString("email", email)
                intent.putExtras(bundle)
                startActivity(intent)
                finish()

                // Call ViewModel to login
//                userAuthViewModel.forgetPassword(email) { success, message ->
//                    if (success) {
//                        loadingDialogUtils.dismiss()
//                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//
//                    } else {
//                        loadingDialogUtils.dismiss()
//                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//                    }
//                }
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