package com.example.vidgram.view.activity.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vidgram.R
import com.example.vidgram.databinding.ActivityEmailSignUpBinding

class EmailSignUpActivity : AppCompatActivity() {
    private  lateinit var binding :ActivityEmailSignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEmailSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle =intent.extras

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow_resized)

        binding.useContactButton.setOnClickListener{
            val intent = Intent(this, PhoneNumberSignUpActivity::class.java)
            intent.putExtras(bundle!!)
            startActivity(intent)
            finish()
        }

        binding.nexButton4.setOnClickListener{
            val email = binding.emailSignUpInputText.text.toString()
            Log.d("EmailSignUpActivity", "Email entered: $email")

            bundle?.putString("email",email)

            val intent = Intent(this, VerificationSignUpActivity::class.java)
            intent.putExtras(bundle!!)
            startActivity(intent)
            finish()
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