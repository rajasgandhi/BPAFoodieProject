package com.rmgstudios.hapori.fragments

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.rmgstudios.hapori.R

class AboutFragment : Fragment() {
    fun SettingsFragment() {

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_about, container, false)
        changeTitleTextSize(view.findViewById(R.id.aboutText))
        return view
    }
    private fun changeTitleTextSize(title: TextView) {
        val displayMetrics = DisplayMetrics()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            requireActivity().display!!.getRealMetrics(displayMetrics)
        } else requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

        val width = displayMetrics.widthPixels

        title.textSize = (.037037 * width).toFloat()
    }
}