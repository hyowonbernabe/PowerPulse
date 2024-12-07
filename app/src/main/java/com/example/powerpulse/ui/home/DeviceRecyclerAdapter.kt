package com.example.powerpulse.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.powerpulse.R

class DeviceRecyclerAdapter(private var deviceName: List<String>, private var deviceDescription: List<String>, private var devicePicture: List<Int>) :
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

        holder.itemView.setOnClickListener { v: View ->
            Toast.makeText(v.context, "Clicked " + deviceName[position], Toast.LENGTH_SHORT).show()
        }
    }

}