package com.example.send_sms

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.send_sms.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        setNavView()
    }

    private fun setNavView() {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, SendMessageFragment()).commit()
        binding.bottomNavigationView.setOnItemSelectedListener {
            if (it.itemId == R.id.sos)
                replace(SendMessageFragment())
            else if (it.itemId == R.id.add_number)
                replace(AddNumberFragment())
            return@setOnItemSelectedListener true
        }
    }

    private fun replace(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit()
    }
};
