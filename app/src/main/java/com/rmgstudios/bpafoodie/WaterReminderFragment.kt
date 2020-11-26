package com.rmgstudios.bpafoodie

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment


class WaterReminderFragment : Fragment() {
    fun WaterReminderFragment() {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_water_reminder, container, false)

        val sendReminderSwitch: SwitchCompat = view.findViewById(R.id.switch1)

        val sharedPref = SharedPref(activity!!)

        sendReminderSwitch.isChecked = sharedPref.loadWaterState()

        sendReminderSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            // update your model (or other business logic) based on isChecked
            val intentAlarm = Intent(activity, NotificationSender::class.java)
            val alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val pi = PendingIntent.getBroadcast(
                activity,
                1,
                intentAlarm,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            if (isChecked) {
                sharedPref.setWaterState(true)
                Toast.makeText(activity, getString(R.string.reminders_enabled), Toast.LENGTH_SHORT).show()
                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    SystemClock.elapsedRealtime(),
                    AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                    pi
                )
            } else {
                Toast.makeText(activity, getString(R.string.reminders_disabled), Toast.LENGTH_SHORT).show()
                sharedPref.setWaterState(false)
                if (alarmManager != null) {
                    alarmManager.cancel(pi)
                }
            }
        }
        changeTitleTextSize(view.findViewById(R.id.waterReminderText))
        return view
    }

    private fun changeTitleTextSize(title: TextView) {
        val displayMetrics = DisplayMetrics()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            activity!!.display!!.getRealMetrics(displayMetrics)
        } else activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)

        val width = displayMetrics.widthPixels

        title.textSize = (.037037 * width).toFloat()
    }
}