package com.example.powerpulse.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.powerpulse.R
import com.example.powerpulse.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var deviceNameList = mutableListOf<String>()
    private var deviceDescriptionList = mutableListOf<String>()
    private var devicePictureList = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize View Binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize views
        val textViewPairButton : ImageView = binding.textViewPairButton

        // Initialize device list
        addDevice("Prototype Device", "Prototype Description", R.drawable.ic_plug)

        // Initialize RecyclerView
        binding.recyclerViewDevice.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewDevice.adapter = context?.let {
            DeviceRecyclerAdapter(deviceNameList, deviceDescriptionList, devicePictureList,
                it
            )
        }

        // Logic for Pair Button
        textViewPairButton.setOnClickListener {
            // Inflate the custom layout
            val view = layoutInflater.inflate(R.layout.pair_dialogue, null)

            // Create the dialog
            val builder = AlertDialog.Builder(requireContext())
            builder.setView(view)

            // Initialize views
            val editTextDeviceName = view.findViewById<EditText>(R.id.editTextDeviceName)
            val editTextDeviceDescription = view.findViewById<EditText>(R.id.editTextDeviceDescription)
            val buttonPairDevice = view.findViewById<Button>(R.id.buttonPairDevice)

            // Show the dialog
            val dialog = builder.create()
            dialog.show()

            buttonPairDevice.setOnClickListener {
                val deviceName = editTextDeviceName.text.toString().trim()
                val deviceDescription = editTextDeviceDescription.text.toString().trim()

                if (deviceName.isEmpty()) {
                    editTextDeviceName.error = "Device Name cannot be empty"
                    return@setOnClickListener
                }

                if (deviceDescription.isEmpty()) {
                    editTextDeviceDescription.error = "Device Description cannot be empty"
                    return@setOnClickListener
                }

                // Add the device to the list
                addDevice(deviceName, deviceDescription, R.drawable.ic_plug)

                dialog.dismiss()
            }
        }


        return root
    }

    private fun addDevice(deviceName: String, deviceDescription: String, devicePicture: Int) {
        deviceNameList.add(deviceName)
        deviceDescriptionList.add(deviceDescription)
        devicePictureList.add(devicePicture)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
