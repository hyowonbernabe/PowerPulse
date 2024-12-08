package com.example.powerpulse.activity

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.powerpulse.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DeviceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge() // Make status bar transparent for cleaner look
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        val textViewDeviceName = findViewById<TextView>(R.id.textViewDeviceName)
        val textViewDeviceDescription = findViewById<TextView>(R.id.textViewDeviceDescription)
        val textViewDayEle = findViewById<TextView>(R.id.textViewDayEle)
        val textViewMonthEle = findViewById<TextView>(R.id.textViewConsumption)
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

        fetchData()
    }

    private fun fetchData() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("device1")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val consumption = snapshot.child("power").getValue(Int::class.java)
                if (consumption != null) {
                    findViewById<TextView>(R.id.textViewConsumption)?.text = "${consumption ?: 0.0} kWh"
                } else {
                    findViewById<TextView>(R.id.textViewConsumption)?.text = "${consumption ?: 0.0} kWh"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                findViewById<TextView>(R.id.textViewConsumption)?.text = "Error: ${error.message}"
            }
        })
    }


}