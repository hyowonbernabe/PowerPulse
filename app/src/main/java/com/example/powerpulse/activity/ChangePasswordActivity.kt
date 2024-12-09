package com.example.powerpulse.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.powerpulse.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var realtimeDB: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_change_password)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        realtimeDB = FirebaseDatabase.getInstance("https://powerpulse-56790-default-rtdb.asia-southeast1.firebasedatabase.app/").reference

        val editTextProfileOldPassword = findViewById<EditText>(R.id.editTextProfileOldPassword)
        val editTextProfileNewPassword = findViewById<EditText>(R.id.editTextProfileNewPassword)
        val editTextChangeProfileConfirmPassword = findViewById<EditText>(R.id.editTextChangeProfileConfirmPassword)
        val buttonChangePassword = findViewById<Button>(R.id.buttonChangePassword)

        buttonChangePassword.setOnClickListener {
            val oldPassword = editTextProfileOldPassword.text.toString().trim()
            val newPassword = editTextProfileNewPassword.text.toString().trim()
            val confirmPassword = editTextChangeProfileConfirmPassword.text.toString().trim()

            if (TextUtils.isEmpty(oldPassword)) {
                editTextProfileOldPassword.error = "Old Password cannot be empty"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(newPassword)) {
                editTextProfileNewPassword.error = "New Password cannot be empty"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(confirmPassword)) {
                editTextChangeProfileConfirmPassword.error = "Confirm Password cannot be empty"
                return@setOnClickListener
            }

            if (confirmPassword != newPassword) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.currentUser?.updatePassword(newPassword)?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password changed successfully.", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Failed to change password.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}