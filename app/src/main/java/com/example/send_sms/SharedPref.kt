package com.example.send_sms

import android.content.SharedPreferences
import android.util.Log


class SharedPref(var preferenceService: SharedPreferences) {
    val numbersKey = "numbers"
    var editor: SharedPreferences.Editor = preferenceService.edit()
    fun changeNumber(number: String): Boolean {
        editor.putString(numbersKey, number).commit()
        return true

    }


    fun getNumber(): String {
        return preferenceService.getString(numbersKey, "Вы не сохранили SOS номер").toString()
    }
}