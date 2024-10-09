package com.roberto.asesoramiento

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificationHelper {

    private const val CHANNEL_ID = "asesoramiento_channel"

    // Función para crear el canal de notificación
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Asesoramiento Legal"
            val descriptionText = "Canal para notificaciones de asesoramiento legal"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Función para mostrar una notificación básica
    fun mostrarNotificacionBasica(context: Context) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Asesoría Legal Básica")
            .setContentText("Recibe asesoría sobre tus derechos laborales.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(1002, builder.build())
        }
    }

    // Función para mostrar una notificación con acción de toque
    fun mostrarNotificacionConToque(context: Context) {
        val intent = Intent(context, AsesoramientoActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Consulta Directa")
            .setContentText("Toca para obtener asesoría personalizada.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(1003, builder.build())
        }
    }

    // Función para mostrar una notificación con botones de acción
    fun mostrarNotificacionConBotones(context: Context) {
        val intentSi = Intent(context, AsesoramientoActivity::class.java)
        val pendingIntentSi = PendingIntent.getActivity(
            context, 0, intentSi, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Elige tu opción")
            .setContentText("¿Necesitas ayuda legal ahora?")
            .addAction(R.drawable.notification_icon, "Aceptar", pendingIntentSi)
            .addAction(R.drawable.notification_icon, "Cancelar", pendingIntentSi)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(1004, builder.build())
        }
    }

    // Función para mostrar una notificación con barra de progreso
    fun mostrarNotificacionConProgreso(context: Context) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Procesando tu Solicitud")
            .setContentText("Estamos preparando tu asesoría.")
            .setPriority(NotificationCompat.PRIORITY_LOW)

        NotificationManagerCompat.from(context).apply {
            builder.setProgress(100, 0, true)
            notify(1005, builder.build())

            Thread {
                try {
                    for (progress in 0..100 step 10) {
                        Thread.sleep(500)
                        builder.setProgress(100, progress, false)
                        notify(1005, builder.build())
                    }
                    builder.setContentText("Asesoría lista para ti.")
                        .setProgress(0, 0, false)
                    notify(1005, builder.build())
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }
}
