package com.rmgstudios.bpafoodie

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat


class NotificationSender : BroadcastReceiver() {
    var mNotificationId = 1

    override fun onReceive(context: Context, intent: Intent?) {
        // Gets an instance of the NotificationManager service
        val mgr = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder = NotificationCompat.Builder(context, "1")
        mBuilder.setSmallIcon(R.mipmap.ic_launcher) // notification icon
            .setContentTitle(context.resources.getText(R.string.app_name))
            .setContentText("This is your reminder to get up and drink some water!")
            .setAutoCancel(true) // clear notification after click
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(
                mNotificationId.toString(),
                context.resources.getString(R.string.app_name),
                importance
            )
            mBuilder.setChannelId(mNotificationId.toString())
            mgr.createNotificationChannel(notificationChannel)
        }
        val resultIntent = Intent(context, MainActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(
            context,
            mNotificationId,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        mBuilder.setContentIntent(resultPendingIntent)
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(mNotificationId, mBuilder.build())
    }
}