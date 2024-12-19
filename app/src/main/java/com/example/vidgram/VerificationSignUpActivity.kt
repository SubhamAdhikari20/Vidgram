package com.example.vidgram

import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vidgram.databinding.ActivityGenderSignUpBinding
import com.example.vidgram.databinding.ActivityPhoneNumberSignUpBinding
import com.example.vidgram.databinding.ActivityVerificationSignUpBinding


class VerificationSignUpActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityVerificationSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding =ActivityVerificationSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle = intent.extras

        binding.nextButton5.setOnClickListener{
            val name = bundle?.getString("fullName")
               val gender = bundle?.getString("gender")
               Toast.makeText(this,"$name , $gender",Toast.LENGTH_LONG).show()
        }

        binding.arrowButton5.setOnClickListener{
            val intent = Intent(this,PhoneNumberSignUpActivity::class.java)
            intent.putExtras(bundle!!)
            startActivity(intent)
            finish()
        }

        binding.nextButton5.setOnClickListener{
            val intent = Intent(this,PasswordSignUpActivity::class.java)
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