package com.example.powerpulse.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.powerpulse.R
import com.example.powerpulse.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var powerValueListener: ValueEventListener? = null

    object PowerConsumptionManager {
        var totalPowerConsumed: Float = 0.0f
    }

    // Shared ViewModel
    private val homeViewModel: HomeViewModel by activityViewModels()

    // Firebase Database reference
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance("https://powerpulse-56790-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("device1")

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

        // Device Count
        val deviceCount: Int = adapter.itemCount
        binding.textViewDeviceCount.text = "$deviceCount"

        // Pair Button logic
        binding.imageViewPairButton.setOnClickListener {
            showPairDeviceDialog(adapter)
        }

        // Listen for 'power' changes in Firebase
        observeRelayState()

        return root
    }

    private fun observeRelayState() {
        database.child("relayState").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val relayState = snapshot.getValue(Boolean::class.java) ?: false
                if (relayState) {
                    stopObservePowerValue()
                } else {
                    startObservePowerValue()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    private fun startObservePowerValue() {
        if (powerValueListener == null) {
            powerValueListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val power = snapshot.getValue(Int::class.java) ?: 0
                    val consumed = ((power * 0.0008333) / 1000).toFloat()

                    PowerConsumptionManager.totalPowerConsumed += consumed
                    val totalPowerConsumedCost = PowerConsumptionManager.totalPowerConsumed * 10.7327

                    binding.textViewPowerNumber.text = String.format("%.6f kWh", PowerConsumptionManager.totalPowerConsumed)
                    binding.textViewDeviceCost.text = String.format("%.3f ₱", totalPowerConsumedCost)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            }
            database.child("power").addValueEventListener(powerValueListener!!)
        }
    }

    private fun stopObservePowerValue() {
        if (powerValueListener != null) {
            database.child("power").removeEventListener(powerValueListener!!)
            powerValueListener = null
        }
    }

    private fun showPairDeviceDialog(adapter: DeviceRecyclerAdapter) {
        val view = layoutInflater.inflate(R.layout.pair_dialogue, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        val editTextDeviceName = view.findViewById<EditText>(R.id.editTextDeviceName)
        val editTextDeviceDescription = view.findViewById<EditText>(R.id.editTextDeviceDescription)
        val editTextAccessLink = view.findViewById<EditText>(R.id.editTextAccessLink)
        val editTextAccessKey = view.findViewById<EditText>(R.id.editTextAccessKey)
        val buttonPairDevice = view.findViewById<Button>(R.id.buttonPairDevice)

        buttonPairDevice.setOnClickListener {
            val deviceName = editTextDeviceName.text.toString().trim()
            val deviceDescription = editTextDeviceDescription.text.toString().trim()
            val accessLink = editTextAccessLink.text.toString().trim()
            val accessKey = editTextAccessKey.text.toString().trim()

            if (deviceName.isEmpty()) {
                editTextDeviceName.error = "Device Name cannot be empty"
                return@setOnClickListener
            }

            if (deviceDescription.isEmpty()) {
                editTextDeviceDescription.error = "Device Description cannot be empty"
                return@setOnClickListener
            }

            if (accessLink.isEmpty()) {
                editTextAccessLink.error = "Access Link cannot be empty"
                return@setOnClickListener
            }

            if (accessKey.isEmpty()) {
                editTextAccessKey.error = "Access Key cannot be empty"
                return@setOnClickListener
            }

            homeViewModel.addDevice(deviceName, deviceDescription, R.drawable.ic_plug)

            val deviceCount: Int = adapter.itemCount
            binding.textViewDeviceCount.text = "$deviceCount"

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