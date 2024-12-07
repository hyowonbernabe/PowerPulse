package com.example.powerpulse.activity

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.powerpulse.R

class DeviceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge() // Make status bar transparent for cleaner look
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        // Access the arguments (device ID) passed to the activity
        val deviceId = intent.getIntExtra("deviceId", -1)
        // Fetch device details based on the device ID and update the UI
        // For example:
        // val device = getDeviceById(deviceId) // Fetch device details from a data source

        val deviceName = findViewById<TextView>(R.id.device_name)
        val deviceDescription = findViewById<TextView>(R.id.device_description)
    }
}