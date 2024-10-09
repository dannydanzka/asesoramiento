package com.roberto.asesoramiento

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ResultadoActivity : AppCompatActivity() {

    private val CHANNEL_ID = "liquidacion_channel" // ID del canal de notificaciones

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        // Obtener los datos pasados desde MainActivity
        val salario = intent.getDoubleExtra("salario", 0.0)
        val antiguedad = intent.getIntExtra("antiguedad", 0)
        val liquidacion = intent.getDoubleExtra("liquidacion", 0.0)

        // Mostrar el resultado en el TextView
        val resultadoTextView = findViewById<TextView>(R.id.resultado)
        resultadoTextView.text = "Liquidación: $$liquidacion\nSalario: $$salario\nAntigüedad: $antiguedad años"

        // Botón para solicitar asesoramiento
        val btnAsesoramiento = findViewById<Button>(R.id.btnAsesoramiento)
        btnAsesoramiento.setOnClickListener {
            // Inicia la AsesoramientoActivity
            val intent = Intent(this, AsesoramientoActivity::class.java)
            startActivity(intent)
        }

        // Botón para regresar a la actividad anterior
        val btnRegresar = findViewById<Button>(R.id.btnRegresar)
        btnRegresar.setOnClickListener {
            finish() // Cierra esta actividad y regresa
        }

        // Crear el canal de notificación si es necesario (para Android 8.0 o superior)
        createNotificationChannel()
        mostrarNotificacionLiquidacion(salario, antiguedad, liquidacion)
    }

    private fun mostrarNotificacionLiquidacion(salario: Double, antiguedad: Int, liquidacion: Double) {
        val titulo = "Resumen de Liquidación"
        val mensaje = "Salario: $$salario, Antigüedad: $antiguedad años, Liquidación: $$liquidacion"
        mostrarNotificacion(this, titulo, mensaje)
    }

    private fun mostrarNotificacion(context: Context, titulo: String, mensaje: String) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true) // Cierra la notificación al hacer clic

        with(NotificationManagerCompat.from(context)) {
            notify(1001, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Liquidacion Channel"
            val descriptionText = "Canal para notificaciones de liquidación"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
