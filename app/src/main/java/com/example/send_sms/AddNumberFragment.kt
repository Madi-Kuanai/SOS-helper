package com.example.send_sms

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.send_sms.databinding.FragmentAddNumberBinding


class AddNumberFragment : Fragment() {
    lateinit var binding: FragmentAddNumberBinding
    private var adapter: NumberAdapter = NumberAdapter()
    lateinit var settings: SharedPreferences
    lateinit var shared: SharedPref
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNumberBinding.inflate(layoutInflater)
        settings = binding.root.context.applicationContext.getSharedPreferences(
            "NUMBERS",
            Context.MODE_PRIVATE
        );
        shared = SharedPref(settings)
        binding.yourNumber.text = "Your sos number: " + shared.getNumber()
        binding.addNumberBtn.setOnClickListener {
            if (binding.phoneInput.text.toString().isNotEmpty()) {
                if (shared.changeNumber(binding.phoneInput.text.toString())) {
                    binding.yourNumber.text = "Ваш SOS номер: " + binding.phoneInput.text.toString()
                    binding.phoneInput.text.clear()
                }
            }
        }
        return binding.root
    }

//    private fun initRecView() {
//        val linear = LinearLayoutManager(context)
//        linear.orientation = LinearLayoutManager.VERTICAL
//        binding.numberRec.layoutManager = linear
//        binding.numberRec.adapter = adapter
//        for (elem in shared.getNumbers() ?: throw NullPointerException()) {
//            adapter.addNumber(elem)
//        }
//    }
}
