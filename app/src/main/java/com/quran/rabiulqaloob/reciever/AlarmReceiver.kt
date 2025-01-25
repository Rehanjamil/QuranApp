package com.quran.rabiulqaloob.reciever

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import com.quran.rabiulqaloob.R
import com.quran.rabiulqaloob.utils.NotificationType

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        intent?.getBundleExtra(
            "data"
        )?.let {
            val soundUri: Uri? = when (NotificationType.valueOf(it.getString("notification_type") ?: "silent")) {
                NotificationType.SILENT -> Uri.parse(
                    ContentResolver.SCHEME_ANDROID_RESOURCE +
                            "://" + context.packageName + "/" + R.raw.silent
                )

                NotificationType.AZAN -> Uri.parse(
                    ContentResolver.SCHEME_ANDROID_RESOURCE +
                            "://" + context.packageName + "/" + R.raw.azan
                )

                NotificationType.FULLAZAN -> Uri.parse(
                    ContentResolver.SCHEME_ANDROID_RESOURCE +
                            "://" + context.packageName + "/" + R.raw.azan_full
                )

                NotificationType.BEEP -> Uri.parse(
                    ContentResolver.SCHEME_ANDROID_RESOURCE +
                            "://" + context.packageName + "/" + R.raw.pcblip
                )

                else -> null
            }

            val channelId = "Namaz"
            val title = it.getString("prayer_name")
            val channel = NotificationChannel(
                channelId,
                "Namaz",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
            val notificationBuilder = NotificationCompat.Builder(context, channelId)
                .setContentTitle(
                    "$title prayer"
                )
                .setContentText("Time for $title prayer")
                .setSmallIcon(R.drawable.ic_notify) // Replace with your notification icon
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            soundUri?.let {
                notificationBuilder.setSound(it)
            } ?: {
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                val vibrationEffect =
                    VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(vibrationEffect)
            }
            notificationManager.notify(0, notificationBuilder.build())
        }
    }
}
