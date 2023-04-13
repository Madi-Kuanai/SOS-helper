package com.example.send_sms

import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.widget.TextView
import com.example.send_sms.databinding.FragmentSendMessageBinding
import java.util.*


class MyLocationListener(private val binding: FragmentSendMessageBinding) : LocationListener {
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    override fun onLocationChanged(location: Location) {
        // This method is called when the location changes
        latitude = location.latitude
        longitude = location.longitude

        // Use latitude and longitude to get the address
        getAddress(binding.tvAddress)
    }

    @SuppressLint("SetTextI18n")
    internal fun getAddress(textView: TextView): String {
        val geocoder = Geocoder(binding.root.context.applicationContext, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (textView.text != "Address: " + addresses?.get(0)?.getAddressLine(0).toString()) {
            textView.text = "Address: " + addresses?.get(0)?.getAddressLine(0).toString()
        }
        return addresses?.get(0)?.getAddressLine(0) ?: throw Error("Address is null")
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        // This method is called when the status of the provider changes
    }

    override fun onProviderEnabled(provider: String) {
        // This method is called when the provider is enabled
    }

    override fun onProviderDisabled(provider: String) {
        // This method is called when the provider is disabled
    }
}
