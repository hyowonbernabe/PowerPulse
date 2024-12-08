package com.example.powerpulse.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.powerpulse.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DeviceActivity : AppCompatActivity() {

    // Initialize totalPowerConsumed as a mutable variable
    private var totalPowerConsumed: Float = 0F

    // Firebase Database reference
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge() // Make status bar transparent for cleaner look
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance("https://powerpulse-56790-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("device1")

        // Initialize views
        val textViewDeviceName = findViewById<TextView>(R.id.textViewDeviceName)
        val textViewDeviceDescription = findViewById<TextView>(R.id.textViewDeviceDescription)
        val textViewCurrentWatts = findViewById<TextView>(R.id.textViewCurrentWatts)
        val textViewTotalKWH = findViewById<TextView>(R.id.textViewTotalKWH)
        val TextViewPowerText = findViewById<TextView>(R.id.textViewPowerText)

        // Get data from intent
        val deviceName = intent.getStringExtra("deviceName") ?: "Prototype Device"
        val deviceDescription =
            intent.getStringExtra("deviceDescription") ?: "Prototype Description"
        val switchState = intent.getBooleanExtra("switchState", false) // Retrieve switch state

        // Set data to TextViews
        textViewDeviceName.text = deviceName
        textViewDeviceDescription.text = deviceDescription

        // Update background color based on switch state
        val backgroundColor =
            if (switchState) R.color.brand_power_green else R.color.brand_power_red
        findViewById<ConstraintLayout>(R.id.main).setBackgroundColor(
            ContextCompat.getColor(
                this,
                backgroundColor
            )
        )

        // Update Power Text
        val powerText = if (switchState) "Power On" else "Power Off"
        TextViewPowerText.text = powerText

        database.child("power").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val power = snapshot.getValue(Int::class.java) ?: 0
                val consumed: Float

                consumed = ((power * 0.0008333) / 1000).toFloat()

                totalPowerConsumed += consumed
                textViewCurrentWatts.text = "$power W"
                textViewTotalKWH.text = String.format("%.3f kWh", totalPowerConsumed)

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
}