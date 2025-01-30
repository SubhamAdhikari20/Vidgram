package com.example.vidgram.view.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vidgram.databinding.ActivityEditProfileDetailBinding
import com.example.vidgram.repository.UserRepositoryImpl
import com.example.vidgram.viewmodel.UserViewModel

class EditProfileDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileDetailBinding
    lateinit var  userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve passed values
        val fieldType = intent.getStringExtra("FIELD_TYPE") ?: ""
        val fieldValue = intent.getStringExtra("FIELD_VALUE") ?: ""

        // Set toolbar title dynamically
        binding.nameTitleTexView.text = fieldType

        // Set EditText hint and value
        binding.nameEditProfileEditTextField.hint = "Enter $fieldType"
        binding.nameEditProfileEditTextField.setText(fieldValue)

        // Adjust the height for "Bio" field only

        val repo = UserRepositoryImpl()
        userViewModel = UserViewModel(repo)

        binding.nameSaveChangesButton.setOnClickListener {
            val updatedValue = binding.nameEditProfileEditTextField.text.toString()
            val mappedFieldType = mapFieldTypeToModel(fieldType)

            // Check if the field is not empty before updating
            if (updatedValue.isNotEmpty()) {
                if (mappedFieldType != null) {

                    // Create a map of the updated data
                    val data = mutableMapOf<String, Any>()
                    data[mappedFieldType] = updatedValue

                    Log.d("message", "$fieldType updated with value: $updatedValue")
                    // Call the method to update the profile
                    val currentUserID =
                        "v4xWlr2zR6hwoXG4QhezWAUnHmx1"  // Replace this with the actual user ID
                    userViewModel.editProfile(currentUserID, data) { isSuccess, message ->
                        if (isSuccess) {
                            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT)
                                .show()
                            finish()  // Close the activity after successful update
                        } else {
                            Toast.makeText(
                                this,
                                "Failed to update profile: $message",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Please enter a value for $fieldType", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun mapFieldTypeToModel(fieldType: String): String? {
        return when (fieldType.uppercase()) {
            "NAME" -> "fullName"
            "DOB" -> "dob"
            "GENDER" -> "gender"
            "COUNTRY" -> "country"
            "CONTACT" -> "contact"
            "EMAIL" -> "email"
            "PASSWORD" -> "password"
            else -> null
        }
    }
}
