package com.rmgstudios.bpafoodie

import android.content.Context
import android.content.SharedPreferences


internal class SharedPref(context: Context) {
    private val mySharedPref: SharedPreferences

    init {
        mySharedPref = context.getSharedPreferences("filename", Context.MODE_PRIVATE)
    }

    // this method will save the nightMode State : True or False
    fun setWaterState(state: Boolean?) {
        val editor = mySharedPref.edit()
        editor.putBoolean("WaterMode", state!!)
        editor.apply()
    }

    // this method will load the nightMode State
    fun loadWaterState(): Boolean {
        return mySharedPref.getBoolean("WaterMode", false)
    }
}