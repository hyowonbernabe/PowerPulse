package com.example.powerpulse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge() // Make status bar transparent for cleaner look
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        firebaseAuth = FirebaseAuth.getInstance()

        // Initialize views
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val textSignUp = findViewById<TextView>(R.id.textViewSignUp)

        // Set up the login button click listener
        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (validateLoginInput(email, password)) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Set up the sign-up text click listener
        textSignUp.setOnClickListener {
            // Navigate to the sign-up screen (replace SignUpActivity with your actual sign-up activity)
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    // Function to validate input (you can enhance this as needed)
    private fun validateLoginInput(email: String, password: String): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            findViewById<EditText>(R.id.editTextEmail).error = "Email cannot be empty"
            isValid = false
        }

        if (password.isEmpty()) {
            findViewById<EditText>(R.id.editTextPassword).error = "Password cannot be empty"
            isValid = false
        }

        return isValid
    }
}