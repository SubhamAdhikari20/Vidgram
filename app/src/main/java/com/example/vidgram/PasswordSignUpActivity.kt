package com.example.vidgram

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vidgram.databinding.ActivityEmailSignUpBinding
import com.example.vidgram.databinding.ActivityPasswordSignUpBinding

class PasswordSignUpActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityPasswordSignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPasswordSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras

        binding.arrowButton6.setOnClickListener{
            val intent = Intent(this,VerificationSignUpActivity::class.java)
            intent.putExtras(bundle!!)
            startActivity(intent)
            finish()
        }
        binding.nextButton6.setOnClickListener{
            val password = binding.passwordSignUpInputText.text.toString()
            bundle?.putString("password",password)
            val intent = Intent(this,TermsAndConditionActivity::class.java)
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
}