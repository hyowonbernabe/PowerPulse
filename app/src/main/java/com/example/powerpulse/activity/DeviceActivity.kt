package com.example.powerpulse.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.powerpulse.R

class DeviceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge() // Make status bar transparent for cleaner look
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        val textViewDeviceName = findViewById<TextView>(R.id.textViewDeviceName)
        val textViewDeviceDescription = findViewById<TextView>(R.id.textViewDeviceDescription)
        val textViewDayEle = findViewById<TextView>(R.id.textViewDayEle)
        val textViewMonthEle = findViewById<TextView>(R.id.textViewMonthEle)
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
    }
}