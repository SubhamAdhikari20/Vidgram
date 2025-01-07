package com.example.vidgram.view.activity.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.vidgram.databinding.ActivityPhoneNumberSignUpBinding

class PhoneNumberSignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhoneNumberSignUpBinding
    private val countryList = listOf(
        Pair("Nepal", "+977"),
        Pair("USA", "+1"),
        Pair("Canada", "+1"),
        Pair("India", "+91"),
        Pair("Germany", "+49"),
        Pair("Australia", "+61"),
        Pair("UK", "+44"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneNumberSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras

        // Set up the Spinner with the country names
        val countryNames = countryList.map { it.first }

        // Create the adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, countryNames)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        // Set the adapter to the spinner
        binding.countrySpinner.setAdapter(adapter)

        binding.countrySpinner.setOnItemClickListener { parent, view, position, id ->
            // Get the phone code based on the selected country
            val selectedCountry = countryList[position]
            val phoneCode = selectedCountry.second

            // Insert the phone code into the EditText for phone number
            binding.contactInputLayout.prefixText = "$phoneCode-"
//            binding.contactInputText.setText("$phoneCode-")
            binding.contactInputText.text = null
//            binding.contactInputText.setSelection(phoneCode.length+1)
            binding.contactInputText.requestFocus()
        }


        binding.contactInputText.doOnTextChanged { text, start, before, count ->
            if (text!!.length > 10) {
                binding.contactInputText.setText(text.substring(0, 10))
                binding.contactInputText.setSelection(10)
//                binding.contactInputText.filters = arrayOf(InputFilter.LengthFilter(10))
                binding.contactInputLayout.isCounterEnabled = true
                binding.contactInputLayout.counterMaxLength = 10

                binding.contactInputLayout.helperText = "No more"
            }
            else if (text.length <= 10){
                binding.contactInputLayout.helperText = null
                binding.contactInputLayout.isCounterEnabled = false
            }
        }
        
        /*
        binding.countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Get the phone code based on the selected country
                val selectedCountry = countryList[position]
                val phoneCode = selectedCountry.second

                // Insert the phone code into the EditText for phone number
                binding.contactInputText.setText("$phoneCode-")
                binding.contactInputText.setSelection(phoneCode.length+1)

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional: clear the phone field if nothing is selected
                binding.contactInputText.setText("")
            }
        }
        */

        binding.arrowButton3.setOnClickListener {
            val intent = Intent(this, GenderSignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.nextButton3.setOnClickListener {
            // Retrieve the phone number after selection
            val phoneNumber = binding.contactInputText.text.toString()
            val country = binding.countrySpinner.text.toString()

            // Pass the data in the bundle and start the next activity
            bundle?.putString("phonenumber", phoneNumber)
            bundle?.putString("country", country)

            val intent = Intent(this, EmailSignUpActivity::class.java)
            intent.putExtras(bundle!!)
            startActivity(intent)
            finish()
        }

        binding.useEmailButton.setOnClickListener {
            val intent = Intent(this, EmailSignUpActivity::class.java)
            intent.putExtras(bundle!!)
            startActivity(intent)
            finish()
        }
    }
}

