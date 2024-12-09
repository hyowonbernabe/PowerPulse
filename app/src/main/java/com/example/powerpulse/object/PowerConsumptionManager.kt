package com.example.powerpulse.`object`

import android.content.Context

object PowerConsumptionManager {
    var totalPowerConsumed: Float = 0.0f

    fun savePowerConsumed(context: Context) {
        val sharedPreferences = context.getSharedPreferences("PowerPulsePrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("totalPowerConsumed", totalPowerConsumed)
        editor.apply()
    }

    fun loadPowerConsumed(context: Context) {
        val sharedPreferences = context.getSharedPreferences("PowerPulsePrefs", Context.MODE_PRIVATE)
        totalPowerConsumed = sharedPreferences.getFloat("totalPowerConsumed", 0.0f)
    }
}
