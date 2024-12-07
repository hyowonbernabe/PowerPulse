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


class AuthActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_auth)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize views
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonSignIn = findViewById<Button>(R.id.buttonSignIn)
        val textSignUp = findViewById<TextView>(R.id.textViewSignUp)

        buttonSignIn.setOnClickListener {
            val emailSignIn = editTextEmail.text.toString().trim()
            val passwordSignIn = editTextPassword.text.toString().trim()

            if (emailSignIn.isEmpty()) {
                Toast.makeText(this, "Email cannot be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (passwordSignIn.isEmpty()) {
                Toast.makeText(this, "Password cannot be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(emailSignIn, passwordSignIn)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // If sign is successful, display a message to the user.
                        Toast.makeText(this, "Login Successful.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        textSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}