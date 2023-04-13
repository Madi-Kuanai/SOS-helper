package com.example.send_sms

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.send_sms.databinding.NumberCardBinding

class NumberAdapter : RecyclerView.Adapter<NumberAdapter.NumbersHolder>() {

    private var numbersList = arrayListOf<String>()

    class NumbersHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var settings: SharedPreferences
        lateinit var shared: SharedPref
        var binding: NumberCardBinding = NumberCardBinding.bind(itemView)
        fun bind(number: String) {
            settings = binding.root.context.applicationContext.getSharedPreferences(
                "NUMBERS",
                Context.MODE_PRIVATE
            );
            shared = SharedPref(settings)
            binding.tvNumber.text = number

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumbersHolder {
        return NumbersHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.number_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NumbersHolder, position: Int) {
        holder.bind(numbersList[position])
    }

    override fun getItemCount(): Int {
        return numbersList.size
    }

    fun addNumber(number: String) {
        numbersList.add(number)
    }
}

