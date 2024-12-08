package com.example.powerpulse.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
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
    private val dayStates = mutableMapOf(
        "Sunday" to false,
        "Monday" to false,
        "Tuesday" to false,
        "Wednesday" to false,
        "Thursday" to false,
        "Friday" to false,
        "Saturday" to false
    )


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deviceName: TextView = itemView.findViewById(R.id.RecyclerDeviceName)
        val deviceDescription: TextView = itemView.findViewById(R.id.RecyclerDeviceDescription)
        val devicePicture: ImageView = itemView.findViewById(R.id.RecyclerDeviceLogo)
        val expandableContent: View = itemView.findViewById(R.id.expandableContent)
        val chevron: ImageView = itemView.findViewById(R.id.imageViewChevronDown)
        val disconnectDevice: TextView = itemView.findViewById(R.id.expandableDelete)
        val switchPower: Switch = itemView.findViewById(R.id.switchPower)
        val deviceTime: TextView = itemView.findViewById(R.id.DeviceTime)
        val deviceTimeAMPM: TextView = itemView.findViewById(R.id.DeviceTimeAMPM)
        val deviceTimeButton: LinearLayout = itemView.findViewById(R.id.DeviceTimeButton)
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

        // Configure each day's click listener
        val days = listOf(
            holder.itemView.findViewById<ImageView>(R.id.DeviceSunday) to "Sunday",
            holder.itemView.findViewById<ImageView>(R.id.DeviceMonday) to "Monday",
            holder.itemView.findViewById<ImageView>(R.id.DeviceTuesday) to "Tuesday",
            holder.itemView.findViewById<ImageView>(R.id.DeviceWednesday) to "Wednesday",
            holder.itemView.findViewById<ImageView>(R.id.DeviceThursday) to "Thursday",
            holder.itemView.findViewById<ImageView>(R.id.DeviceFriday) to "Friday",
            holder.itemView.findViewById<ImageView>(R.id.DeviceSaturday) to "Saturday"
        )

        for ((imageView, day) in days) {
            imageView.setOnClickListener {
                val newState = !(dayStates[day] ?: false)
                dayStates[day] = newState

                // Update the drawable
                val focusedResource = when (day) {
                    "Sunday", "Saturday" -> R.drawable.ic_letter_s_focused
                    "Monday" -> R.drawable.ic_letter_m_focused
                    "Tuesday", "Thursday" -> R.drawable.ic_letter_t_focused
                    "Wednesday" -> R.drawable.ic_letter_w_focused
                    "Friday" -> R.drawable.ic_letter_f_focused
                    else -> R.drawable.ic_letter_empty_focused // Fallback
                }
                val defaultResource = when (day) {
                    "Sunday", "Saturday" -> R.drawable.ic_letter_s
                    "Monday" -> R.drawable.ic_letter_m
                    "Tuesday", "Thursday" -> R.drawable.ic_letter_t
                    "Wednesday" -> R.drawable.ic_letter_w
                    "Friday" -> R.drawable.ic_letter_f
                    else -> R.drawable.ic_letter_empty // Fallback
                }
                imageView.setImageResource(if (newState) focusedResource else defaultResource)
            }
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