package com.example.powerpulse.activity

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.powerpulse.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserListActivity : AppCompatActivity() {

    private lateinit var usersTextView: TextView
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_list)

        usersTextView = findViewById(R.id.usersTextView)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance("https://powerpulse-56790-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users")

        // Fetch data from Firebase Realtime Database
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val stringBuilder = StringBuilder() // To build the text
                    // Loop through all users and append their details to the string
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)
                        if (user != null) {
                            stringBuilder.append("Name: ${user.fullName}\n")
                            stringBuilder.append("Email: ${user.email}\n")
                            stringBuilder.append("Role: ${user.role}\n\n")
                        }
                    }
                    // Set the text of the TextView
                    usersTextView.text = stringBuilder.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UserListActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

data class User(
    val email: String = "",
    val fullName: String = "",
    val role: String = ""
)