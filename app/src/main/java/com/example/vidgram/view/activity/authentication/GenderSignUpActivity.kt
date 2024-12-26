package com.example.vidgram.view.activity.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vidgram.R
import com.example.vidgram.databinding.ActivityGenderSignUpBinding

class GenderSignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGenderSignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding =ActivityGenderSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle male layout and button clicks
        binding.maleLayout.setOnClickListener {
            binding.radioGroup.check(binding.male.id) // Ensure RadioGroup reflects the selection
        }
        binding.male.setOnClickListener {
            binding.radioGroup.check(binding.male.id) // Ensure RadioGroup reflects the selection
        }

        // Handle female layout and button clicks
        binding.femaleLayout.setOnClickListener {
            binding.radioGroup.check(binding.female.id) // Ensure RadioGroup reflects the selection
        }
        binding.female.setOnClickListener {
            binding.radioGroup.check(binding.female.id) // Ensure RadioGroup reflects the selection
        }


        val bundle = intent.extras
//        val name = bundle?.getString("fullName")  // Retrieve the fullName
//        if (name != null) {
//            Toast.makeText(this, "Full Name: $name", Toast.LENGTH_LONG).show()
//        } else {
//            Toast.makeText(this, "No name received", Toast.LENGTH_LONG).show()
//        }


        // Next Button
        binding.nextButton2.setOnClickListener {
            val selectedId = binding.radioGroup.checkedRadioButtonId
            //need this to catch null pointer exception
            if (selectedId == -1) {
                // No RadioButton is selected
                Toast.makeText(applicationContext, "Please select a gender", Toast.LENGTH_SHORT).show()
            }
            else {
                // Get the selected RadioButton and its text
                val selectedRadioButton: RadioButton = binding.root.findViewById(selectedId)
                val gender = selectedRadioButton.text.toString()

                Toast.makeText(applicationContext, "Selected Gender: $gender", Toast.LENGTH_SHORT)
                    .show()

                // Pass the selected gender to the next activity
                bundle?.putString("gender", gender)
                val intent = Intent(this, PhoneNumberSignUpActivity::class.java)
                intent.putExtras(bundle!!)
                startActivity(intent)
                finish() //
            }
        }

        binding.arrowButton2.setOnClickListener{
            val intent = Intent(this, NameSignUpActivity::class.java)
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