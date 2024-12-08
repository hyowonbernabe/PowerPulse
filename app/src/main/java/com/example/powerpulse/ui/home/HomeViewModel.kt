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
        addDevice("Prototype Device", "Device to be demonstrated", R.drawable.ic_plug)
        addDevice("Air Conditioner", "Lorem Ipsum", R.drawable.ic_plug)
        addDevice("Hyowon's Computer", "WAG I-OFF", R.drawable.ic_plug)
        addDevice("Switch ni Kyle", "Tara laro", R.drawable.ic_plug)
        addDevice("SLU TV", "Baka magalit si ma'am", R.drawable.ic_plug)
        addDevice("Fridge ni Aaron", "200g protein", R.drawable.ic_plug)
        addDevice("Joeffrey's Fan", "HNGGGGGGGGGGGGG", R.drawable.ic_plug)
    }

    fun addDevice(name: String, description: String, picture: Int) {
        deviceNameList.add(name)
        deviceDescriptionList.add(description)
        devicePictureList.add(picture)
    }
}

data class Device(val name: String, val description: String, val picture: Int)
