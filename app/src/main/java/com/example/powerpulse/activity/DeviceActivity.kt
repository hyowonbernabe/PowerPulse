package com.example.powerpulse.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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
        val ImageViewPowerButton = findViewById<ImageView>(R.id.imageViewPowerButton)
        val mainLayout = findViewById<ConstraintLayout>(R.id.main)

        // Get data from intent
        val deviceName = intent.getStringExtra("deviceName") ?: "Prototype Device"
        val deviceDescription = intent.getStringExtra("deviceDescription") ?: "Prototype Description"
        var booleanPower = intent.getBooleanExtra("powerState", false) // Retrieve power state

        // Set data to TextViews
        textViewDeviceName.text = deviceName
        textViewDeviceDescription.text = deviceDescription

        ImageViewPowerButton.setOnClickListener {
            if (!booleanPower) {
                mainLayout.setBackgroundColor(resources.getColor(R.color.brand_power_green, theme))
                TextViewPowerText.text = "Power On"
                booleanPower = true
            } else {
                mainLayout.setBackgroundColor(resources.getColor(R.color.brand_power_red, theme))
                TextViewPowerText.text = "Power Off"
                booleanPower = false
            }
        }

    }
}