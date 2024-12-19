package com.example.vidgram

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vidgram.databinding.ActivityPasswordSignUpBinding
import com.example.vidgram.databinding.ActivityTermsAndConditionBinding

class TermsAndConditionActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityTermsAndConditionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTermsAndConditionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle = intent.extras

        binding.arrowButton7.setOnClickListener{
            val intent = Intent(this,PasswordSignUpActivity::class.java)
            intent.putExtras(bundle!!)
            startActivity(intent)
            finish()
        }
        binding.agreeButton.setOnClickListener{
            val name  = bundle?.getString("fullName")
            val dob = bundle?.getString("dob")
            val gender = bundle?.getString("gender")
            val country = bundle?.getString("country")
            val phonenumber = bundle?.getString("phonenumber")
            val email = bundle?.getString("email")
            val password = bundle?.getString("password")

            Log.d("UserDetails", "Name: $name, DOB: $dob, Gender: $gender, Country: $country, Phone: $phonenumber, Email: $email,Password:$password")

//            val intent = Intent(this,#dashboardactivty::class.java)
//            intent.putExtras(bundle!!)
//            startActivity(intent)
//            finish()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}