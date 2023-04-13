package com.example.send_sms

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import com.example.send_sms.databinding.FragmentSendMessageBinding


class SendMessageFragment : Fragment() {

    lateinit var binding: FragmentSendMessageBinding
    private val PERMISSIONS_REQUEST_CODE = 101
    private val REQUEST_CODE_LOCATION_PERMISSION = 102
    val smsManager = SmsManager.getDefault()
    lateinit var settings: SharedPreferences
    lateinit var shared: SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSendMessageBinding.inflate(layoutInflater)

        try {
            settings = binding.root.context.applicationContext.getSharedPreferences(
                "NUMBERS",
                Context.MODE_PRIVATE
            );
            shared = SharedPref(settings)
            binding.sendsms.setOnClickListener {
                sendMessage()
            }
            binding.sendWhatsApp.setOnClickListener {
                Log.d("TAG", "WhatsApp")
                sendWhatsApp()
            }
            binding.sendWhatsApp.visibility = View.GONE
            binding.sendsms.visibility = View.GONE
            checkLocationPermission()
            binding.tvAddress.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    binding.sendWhatsApp.visibility = View.VISIBLE
                    binding.sendsms.visibility = View.VISIBLE

                }
            })
        } catch (e: Exception) {
            val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(binding.root.context)
            val dialog: AlertDialog = alertDialogBuilder.create()
            dialog.setMessage(e.message)
            dialog.show()
        }
        return binding.root
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun sendMessage() {
        if (checkPermissions()) {
            if (shared.getNumber().replace("+", "").isDigitsOnly()) {
                val manager = SmsManager.getDefault()
                manager.sendTextMessage(shared.getNumber(), null, "teste", null, null)

                Toast.makeText(
                    binding.root.context.applicationContext, "Message Sent successfully!",
                    Toast.LENGTH_LONG
                ).show();
            }
//            val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
//            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, "com.example.send_sms")
//            startActivity(intent)
//            val parts = smsManager.divideMessage(binding.tvAddress.text.toString())
//            val sentIntents = Array<PendingIntent?>(parts.size) { null }
//            val deliveredIntents = Array<PendingIntent?>(parts.size) { null }
//            for (i in 0 until parts.size) {
//                sentIntents[i] = PendingIntent.getBroadcast(
//                    binding.root.context.applicationContext,
//                    0,
//                    Intent("SMS_SENT"),
//                    PendingIntent.FLAG_MUTABLE
//                )
//                deliveredIntents[i] = PendingIntent.getBroadcast(
//                    binding.root.context.applicationContext,
//                    0,
//                    Intent("SMS_DELIVERED"),
//                    PendingIntent.FLAG_MUTABLE
//                )
//            }
//            for (elem in shared.getNumbers()!!) {
//                smsManager.sendMultipartTextMessage(
//                    elem, null, parts, ArrayList(sentIntents.toList()),
//                    ArrayList(deliveredIntents.toList())
//                )
//            }

        }

    }

    private fun sendWhatsApp() {
        if (shared.getNumber().replace("+", "").isDigitsOnly()) {
            val uri =
                Uri.parse("https://api.whatsapp.com/send?phone=" + shared.getNumber() + "&text=" + binding.tvAddress.text.toString());
            val sendIntent = Intent(Intent.ACTION_VIEW, uri);
            startActivity(sendIntent)
        }
    }

    private fun checkPermissions(): Boolean {
        return checkSmsPermission() && checkLocationPermission()
    }

    private fun checkSmsPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                binding.root.context,
                Manifest.permission.SEND_SMS
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        } else {
            // Permission is not granted, request for it
            ActivityCompat.requestPermissions(
                binding.root.context as Activity,
                arrayOf(Manifest.permission.SEND_SMS),
                PERMISSIONS_REQUEST_CODE
            )
            return false
        }
    }

    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                binding.root.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // permission already granted, proceed to get location
            getLocation();
            val locationManager = binding.root.context.getSystemService(
                Context.LOCATION_SERVICE
            ) as LocationManager
            val locationListener = MyLocationListener(binding)

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationListener
            )
            return true
        } else {
            // permission not granted, request permission
            ActivityCompat.requestPermissions(
                binding.root.context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION_PERMISSION
            );
            return false
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                // Permission is not granted
                Toast.makeText(binding.root.context, "SMS permission denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission granted, proceed to get location
                getLocation();
            } else {
                // permission not granted, show a message or disable the location feature
                Toast.makeText(
                    binding.root.context,
                    "Location permission denied",
                    Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}