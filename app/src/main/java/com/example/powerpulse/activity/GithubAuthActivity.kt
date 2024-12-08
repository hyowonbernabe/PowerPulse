package com.example.powerpulse.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.powerpulse.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider

class GithubAuthActivity : AppCompatActivity() {
    private lateinit var inputEmail : EditText
    private lateinit var btnSignin : Button
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val emailPattern: String = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_github_auth)

        inputEmail = findViewById(R.id.editTextEmail)
        btnSignin = findViewById(R.id.buttonSignIn)

        btnSignin.setOnClickListener {
            val email = inputEmail.text.toString()
            if (!email.matches(emailPattern.toRegex())) {
                Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show()
            } else {
                val provider = OAuthProvider.newBuilder("github.com")
                // Target specific email with login hint.
                provider.addCustomParameter("login", email)
                // Request read access to a user's email addresses.
                // This must be preconfigured in the app's API permissions.
                provider.scopes = listOf("user:email")

                val pendingResultTask = firebaseAuth.pendingAuthResult
                if (pendingResultTask != null) {
                    // There's something already here! Finish the sign-in for your user.
                    pendingResultTask
                        .addOnSuccessListener {
                            // User is signed in.
                            // IdP data available in
                            // authResult.getAdditionalUserInfo().getProfile().
                            // The OAuth access token can also be retrieved:
                            // ((OAuthCredential)authResult.getCredential()).getAccessToken().
                            // The OAuth secret can be retrieved by calling:
                            // ((OAuthCredential)authResult.getCredential()).getSecret().
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    firebaseAuth
                        .startActivityForSignInWithProvider(this, provider.build())
                        .addOnSuccessListener {
                            openNextActivity()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }

    private fun openNextActivity() {
        var intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}