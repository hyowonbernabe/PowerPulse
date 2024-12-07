package com.example.powerpulse.ui.home

import android.bluetooth.BluetoothClass.Device
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.powerpulse.R
import com.example.powerpulse.activity.AuthActivity
import com.example.powerpulse.activity.DeviceActivity

class DeviceRecyclerAdapter(
    private var deviceName: List<String>,
    private var deviceDescription: List<String>,
    private var devicePicture: List<Int>,
    private val context: Context) :
    RecyclerView.Adapter<DeviceRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deviceName: TextView = itemView.findViewById(R.id.RecyclerDeviceName)
        val deviceDescription: TextView = itemView.findViewById(R.id.RecyclerDeviceDescription)
        val devicePicture: ImageView = itemView.findViewById(R.id.RecyclerDeviceLogo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.device_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return deviceName.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.deviceName.text = deviceName[position]
        holder.deviceDescription.text = deviceDescription[position]
        holder.devicePicture.setImageResource(devicePicture[position])

        holder.itemView.setOnClickListener {
            // Create an Intent to start DeviceActivity
            val intent = Intent(context, DeviceActivity::class.java).apply {
                putExtra("deviceId", position)
            }
            ContextCompat.startActivity(context, intent, null)
        }
    }

}