// NotificationActionReceiver.kt
package com.roberto.asesoramiento

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat

class NotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "com.roberto.asesoramiento.ACCEPT" -> {
                // Usuario aceptó la asesoría
                // Iniciar la notificación de progreso
                NotificationHelper.mostrarNotificacionConProgreso(context)

                // Cerrar la notificación de opciones (ID 1007)
                val notificationManager = NotificationManagerCompat.from(context)
                notificationManager.cancel(1007)
            }
            "com.roberto.asesoramiento.CANCEL" -> {
                // Usuario canceló la asesoría
                Toast.makeText(context, "Asesoría cancelada", Toast.LENGTH_SHORT).show()

                // Navegar a MainActivity
                val mainIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(mainIntent)

                // Cerrar la notificación de opciones (ID 1007)
                val notificationManager = NotificationManagerCompat.from(context)
                notificationManager.cancel(1007)
            }
        }
    }
}
