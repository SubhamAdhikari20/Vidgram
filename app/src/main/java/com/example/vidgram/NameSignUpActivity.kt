package com.example.vidgram

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vidgram.databinding.ActivityNameSignUpBinding

class NameSignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNameSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityNameSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.arrowButton1.setOnClickListener{
            val intent = Intent(this@NameSignUpActivity, LoginActivity::class.java)
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