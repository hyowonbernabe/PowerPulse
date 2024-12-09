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

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var realtimeDB: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        realtimeDB = FirebaseDatabase.getInstance("https://powerpulse-56790-default-rtdb.asia-southeast1.firebasedatabase.app/").reference

        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val buttonResetPassword = findViewById<Button>(R.id.buttonResetPassword)

        buttonResetPassword.setOnClickListener {
            val email = editTextEmail.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                editTextEmail.error = "Email cannot be empty"
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Password reset email sent.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to send reset email.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}