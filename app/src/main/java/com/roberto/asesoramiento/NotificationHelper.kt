// NotificationHelper.kt
package com.roberto.asesoramiento

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.*

object NotificationHelper {

    private const val CHANNEL_ID = "asesoramiento_channel"
    private const val PROGRESS_NOTIFICATION_ID = 1008
    private const val FINAL_NOTIFICATION_ID = 1009

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

    // Función para mostrar una notificación con los resultados del cálculo
    fun mostrarNotificacionResultados(context: Context, salario: Double, antiguedad: Int, liquidacion: Double) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Resultados del Cálculo")
            .setContentText("Salario: $$salario, Antigüedad: $antiguedad años, Liquidación: $$liquidacion")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(1006, builder.build())
        }
    }

    // Función para mostrar una notificación con botones de acción (Aceptar / Cancelar)
    fun mostrarNotificacionConBotones(context: Context) {
        // Intent para Aceptar
        val intentAceptar = Intent(context, NotificationActionReceiver::class.java).apply {
            action = "com.roberto.asesoramiento.ACCEPT"
        }
        val pendingIntentAceptar = PendingIntent.getBroadcast(
            context, 0, intentAceptar, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Intent para Cancelar
        val intentCancelar = Intent(context, NotificationActionReceiver::class.java).apply {
            action = "com.roberto.asesoramiento.CANCEL"
        }
        val pendingIntentCancelar = PendingIntent.getBroadcast(
            context, 1, intentCancelar, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Opciones de Asesoría")
            .setContentText("¿Necesitas ayuda legal ahora?")
            .addAction(R.drawable.notification_icon, "Aceptar", pendingIntentAceptar) // Aceptar
            .addAction(R.drawable.notification_icon, "Cancelar", pendingIntentCancelar) // Cancelar
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(1007, builder.build())
        }
    }

    // Función para mostrar una notificación con barra de progreso usando Coroutines
    fun mostrarNotificacionConProgreso(context: Context) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Procesando tu Solicitud")
            .setContentText("Estamos preparando tu asesoría.")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOnlyAlertOnce(true) // Evita que la notificación haga sonido en cada actualización

        with(NotificationManagerCompat.from(context)) {
            builder.setProgress(100, 0, false)
            notify(PROGRESS_NOTIFICATION_ID, builder.build())

            // Usamos Coroutines para simular el progreso
            CoroutineScope(Dispatchers.IO).launch {
                for (progress in 0..100 step 10) {
                    delay(500) // Simula una tarea que lleva tiempo
                    builder.setProgress(100, progress, false)
                    withContext(Dispatchers.Main) {
                        notify(PROGRESS_NOTIFICATION_ID, builder.build())
                    }
                }
                // Cuando el progreso termina
                builder.setContentText("Información enviada.")
                    .setProgress(0, 0, false)
                withContext(Dispatchers.Main) {
                    notify(PROGRESS_NOTIFICATION_ID, builder.build())
                }

                // Cerrar la notificación de progreso después de 3 segundos
                withContext(Dispatchers.Main) {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(3000) // 3 segundos
                        NotificationManagerCompat.from(context).cancel(PROGRESS_NOTIFICATION_ID)
                    }
                }

                // Mostrar la notificación final
                mostrarNotificacionFija(context)
            }
        }
    }

    // Función para mostrar una notificación fija después del progreso
    private fun mostrarNotificacionFija(context: Context) {
        val intent = Intent(context, AsesoramientoActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Asesoría lista")
            .setContentText("La información ha sido enviada. Toca para más detalles.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(FINAL_NOTIFICATION_ID, builder.build())
        }

        // No cerramos la notificación final automáticamente
    }
}
