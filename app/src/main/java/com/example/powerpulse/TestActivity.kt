package com.example.powerpulse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class TestActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_test)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val buttonTest = findViewById<Button>(R.id.buttonTest)
        val user = auth.currentUser

        if (user == null) {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonTest.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}