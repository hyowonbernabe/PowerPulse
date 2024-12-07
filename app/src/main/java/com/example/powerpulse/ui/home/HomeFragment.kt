package com.example.powerpulse.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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

        listDevices()

        // Initialize views
        val textViewPairButton : ImageView = binding.textViewPairButton

        // Initialize RecyclerView
        binding.recyclerViewDevice.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewDevice.adapter = context?.let {
            DeviceRecyclerAdapter(deviceNameList, deviceDescriptionList, devicePictureList,
                it
            )
        }

        // Logic for Pair Button
        textViewPairButton.setOnClickListener {
            
        }


        return root
    }

    private fun addDevice(deviceName: String, deviceDescription: String, devicePicture: Int) {
        deviceNameList.add(deviceName)
        deviceDescriptionList.add(deviceDescription)
        devicePictureList.add(devicePicture)
    }

    private fun listDevices() {
        for (i in 1..9) {
            addDevice("Prototype Device $i", "Lorem Ipsum", R.drawable.ic_plug)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
