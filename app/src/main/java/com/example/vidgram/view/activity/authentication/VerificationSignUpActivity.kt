package com.example.vidgram.view.activity.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vidgram.R
import com.example.vidgram.databinding.ActivityVerificationSignUpBinding


class VerificationSignUpActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityVerificationSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding =ActivityVerificationSignUpBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val bundle = intent.extras

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow_resized)

        binding.nextButton5.setOnClickListener{
            val name = bundle?.getString("fullName")
            val gender = bundle?.getString("gender")
            Toast.makeText(this,"$name , $gender",Toast.LENGTH_LONG).show()
        }

        binding.nextButton5.setOnClickListener{
            val intent = Intent(this, PasswordSignUpActivity::class.java)
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