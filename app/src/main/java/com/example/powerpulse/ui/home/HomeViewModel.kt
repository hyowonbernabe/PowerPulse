package com.example.powerpulse.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.powerpulse.R

class HomeViewModel : ViewModel() {

    private val _devices = MutableLiveData<List<Device>>()
    val devices: LiveData<List<Device>> get() = _devices

    val deviceNameList = mutableListOf<String>()
    val deviceDescriptionList = mutableListOf<String>()
    val devicePictureList = mutableListOf<Int>()

    init {
        // Add a prototype device
        addDevice("Prototype Name 1", "Prototype Description", R.drawable.ic_plug)
        addDevice("Prototype Name 2", "Prototype Description", R.drawable.ic_plug)
        addDevice("Prototype Name 3", "Prototype Description", R.drawable.ic_plug)
    }

    fun addDevice(name: String, description: String, picture: Int) {
        deviceNameList.add(name)
        deviceDescriptionList.add(description)
        devicePictureList.add(picture)
    }
}

data class Device(val name: String, val description: String, val picture: Int)
