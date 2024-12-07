package com.example.powerpulse.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.powerpulse.R
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge() // Make status bar transparent for cleaner look
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_auth)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize views
        val editTextEmail = findViewById<EditText>(R.id.editTextEmailSignUp)
        val editTextFullName = findViewById<EditText>(R.id.editTextFullNameSignUp)
        val editTextPassword = findViewById<EditText>(R.id.editTextPasswordSignUp)
        val editTextConfirmPassword = findViewById<EditText>(R.id.editTextConfirmPasswordSignUp)
        val buttonSignUp = findViewById<Button>(R.id.buttonSignUp)
        val textViewSignIn = findViewById<TextView>(R.id.textViewSignIn)

        buttonSignUp.setOnClickListener {
            val emailSignUp = editTextEmail.text.toString().trim()
            val fullNameSignUp = editTextFullName.text.toString().trim()
            val passwordSignUp = editTextPassword.text.toString().trim()
            val confirmPasswordSignUp = editTextConfirmPassword.text.toString().trim()

            if (fullNameSignUp.isEmpty()) {
                editTextFullName.error = "Fullname cannot be empty"
                return@setOnClickListener
            }

            if (emailSignUp.isEmpty()) {
                editTextFullName.error = "Email cannot be empty"
                return@setOnClickListener
            }

            if (passwordSignUp.isEmpty()) {
                editTextPassword.error = "Password cannot be empty"
                return@setOnClickListener
            }

            if (confirmPasswordSignUp.isEmpty()) {
                editTextConfirmPassword.error = "Confirm Password cannot be empty"
                return@setOnClickListener
            }

            if (passwordSignUp != confirmPasswordSignUp) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(emailSignUp, passwordSignUp)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // If sign up is successful, display a message to the user.
                        Toast.makeText(this, "Account created successfully.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, AuthActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign up fails, display a message to the user.
                        Toast.makeText(this, "Account creation failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        textViewSignIn.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
    }
}