package com.example.powerpulse.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.powerpulse.R
import com.example.powerpulse.activity.DeviceActivity

class DeviceRecyclerAdapter(
    private var deviceName: MutableList<String>,
    private var deviceDescription: MutableList<String>,
    private var devicePicture: MutableList<Int>,
    private val context: Context
) : RecyclerView.Adapter<DeviceRecyclerAdapter.ViewHolder>() {

    private val expandedStates = BooleanArray(deviceName.size)// Track expansion states
    private val switchStates = BooleanArray(deviceName.size) // Track switch states

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deviceName: TextView = itemView.findViewById(R.id.RecyclerDeviceName)
        val deviceDescription: TextView = itemView.findViewById(R.id.RecyclerDeviceDescription)
        val devicePicture: ImageView = itemView.findViewById(R.id.RecyclerDeviceLogo)
        val expandableContent: View = itemView.findViewById(R.id.expandableContent)
        val chevron: ImageView = itemView.findViewById(R.id.imageViewChevronDown)
        val disconnectDevice: TextView = itemView.findViewById(R.id.expandableDelete)
        val switchPower: Switch = itemView.findViewById(R.id.switchPower)
    }

    // Update the adapter's data
    fun updateData(
        newDeviceName: MutableList<String>,
        newDeviceDescription: MutableList<String>,
        newDevicePicture: MutableList<Int>
    ) {
        deviceName = newDeviceName
        deviceDescription = newDeviceDescription
        devicePicture = newDevicePicture
        notifyDataSetChanged()
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

        // Handle expansion state
        val isExpanded = expandedStates[position]
        holder.expandableContent.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.chevron.setImageResource(
            if (isExpanded) R.drawable.ic_chevron_up else R.drawable.ic_chevron_down
        )

        // Toggle expansion on chevron click
        holder.chevron.setOnClickListener {
            expandedStates[position] = !expandedStates[position]
            notifyItemChanged(position)
        }

        // Handle "Disconnect Device" click
        holder.disconnectDevice.setOnClickListener {
            removeItem(position)
        }

        // Navigate to DeviceActivity on item click
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DeviceActivity::class.java).apply {
                putExtra("deviceName", deviceName[position])
                putExtra("deviceDescription", deviceDescription[position])
                putExtra("switchState", switchStates[position]) // Pass switch state
            }
            ContextCompat.startActivity(context, intent, null)
        }

        // Update switch state and background color
        holder.switchPower.isChecked = switchStates[position]
        holder.switchPower.setOnCheckedChangeListener { _, isChecked ->
            switchStates[position] = isChecked
            // Notify change to trigger background update
            notifyDataSetChanged()
        }
    }

    // Remove item from lists and update RecyclerView
    private fun removeItem(position: Int) {
        deviceName.removeAt(position)
        deviceDescription.removeAt(position)
        devicePicture.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, deviceName.size) // Updates indices of remaining items
    }
}