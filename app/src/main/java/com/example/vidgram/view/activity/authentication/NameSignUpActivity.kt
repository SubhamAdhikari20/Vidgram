package com.example.vidgram.view.activity.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vidgram.R
import com.example.vidgram.databinding.ActivityNameSignUpBinding
import com.example.vidgram.repository.UserAuthRepositoryImpl
import com.example.vidgram.utils.LoadingDialogUtils
import com.example.vidgram.viewmodel.UserAuthViewModel

class NameSignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNameSignUpBinding
    lateinit var userAuthViewModel: UserAuthViewModel
    lateinit var loadingDialogUtils: LoadingDialogUtils

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

        // Next Button
        loadingDialogUtils = LoadingDialogUtils(this)
        val repo = UserAuthRepositoryImpl()
        userAuthViewModel = UserAuthViewModel(repo)

        binding.nextButton1.setOnClickListener{
            val fullName : String = binding.nameInputText.text.toString()
            val dob : String = binding.dob.text.toString()

            if (fullName.isNotEmpty() && dob.isNotEmpty()) {
                val intent = Intent(this@NameSignUpActivity, GenderSignUpActivity::class.java)

                val bundle = Bundle()
                bundle.putString("fullName", fullName)
                bundle.putString("dob",dob)

                intent.putExtras(bundle)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_LONG).show()
            }


        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}