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
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.powerpulse.R
import com.example.powerpulse.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Shared ViewModel
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize RecyclerView
        val adapter = DeviceRecyclerAdapter(
            homeViewModel.deviceNameList,
            homeViewModel.deviceDescriptionList,
            homeViewModel.devicePictureList,
            requireContext()
        )
        binding.recyclerViewDevice.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewDevice.adapter = adapter

        // Observe LiveData from ViewModel
        homeViewModel.devices.observe(viewLifecycleOwner) { devices ->
            // Update the RecyclerView's data
            adapter.updateData(
                devices.map { it.name }.toMutableList(),
                devices.map { it.description }.toMutableList(),
                devices.map { it.picture }.toMutableList()
            )
        }

        // Pair Button logic
        binding.imageViewPairButton.setOnClickListener {
            showPairDeviceDialog()
        }

        return root
    }

    private fun showPairDeviceDialog() {
        val view = layoutInflater.inflate(R.layout.pair_dialogue, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()

        val editTextDeviceName = view.findViewById<EditText>(R.id.editTextDeviceName)
        val editTextDeviceDescription = view.findViewById<EditText>(R.id.editTextDeviceDescription)
        val buttonPairDevice = view.findViewById<Button>(R.id.buttonPairDevice)

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

            homeViewModel.addDevice(deviceName, deviceDescription, R.drawable.ic_plug)

            // Update the RecyclerView by re-initializing it with updated data
            initializeRecyclerView()

            dialog.dismiss()
        }
    }

    private fun initializeRecyclerView() {
        // Initialize or re-initialize the RecyclerView with the updated data
        val adapter = DeviceRecyclerAdapter(
            homeViewModel.deviceNameList,
            homeViewModel.deviceDescriptionList,
            homeViewModel.devicePictureList,
            requireContext()
        )
        binding.recyclerViewDevice.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewDevice.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}