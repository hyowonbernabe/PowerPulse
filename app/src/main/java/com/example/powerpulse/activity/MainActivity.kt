package com.example.powerpulse.activity

import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.powerpulse.R
import com.example.powerpulse.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference

    private lateinit var textViewConsumption: TextView
    private lateinit var switchPower: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge() // Make status bar transparent for cleaner look
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of IDs because each
        // menu should be considered as top-level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_analytics, R.id.navigation_profile
            )
        )

        navView.setupWithNavController(navController)

        textViewConsumption = findViewById(R.id.textViewConsumption)
        switchPower = findViewById(R.id.switchPower)

        fetchData()
    }

    private fun fetchData() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("device1")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val power = snapshot.child("power").getValue(Int::class.java)
                if (power != null) {
                    textViewConsumption.text = "$power W"
                } else {
                    textViewConsumption.text = "0 W"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                textViewConsumption.text = "Error: ${error.message}"
            }
        })
    }
}
