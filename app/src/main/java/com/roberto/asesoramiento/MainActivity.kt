// MainActivity.kt
package com.roberto.asesoramiento

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.Intent
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {

    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 100

    lateinit var salarioEditText: EditText
    lateinit var antiguedadEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        salarioEditText = findViewById(R.id.salario)
        antiguedadEditText = findViewById(R.id.antiguedad)

        // Crear canal de notificación
        NotificationHelper.createNotificationChannel(this)

        // Verificar permisos de notificaciones para Android 13+
        checkNotificationPermission()

        // Botón para calcular la liquidación y abrir la pantalla de resultado
        val calcularButton = findViewById<Button>(R.id.btnCalcular)
        calcularButton.setOnClickListener {
            if (validarCampos()) {
                val salario = salarioEditText.text.toString().toDouble()
                val antiguedad = antiguedadEditText.text.toString().toInt()
                val liquidacion = calcularLiquidacion(salario, antiguedad)

                // Mostrar notificación básica con los resultados
                NotificationHelper.mostrarNotificacionResultados(this, salario, antiguedad, liquidacion)

                // Enviar a la pantalla de resultados
                val intent = Intent(this, ResultadoActivity::class.java)
                intent.putExtra("salario", salario)
                intent.putExtra("antiguedad", antiguedad)
                intent.putExtra("liquidacion", liquidacion)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para limpiar el formulario
        val limpiarButton = findViewById<Button>(R.id.btnLimpiar)
        limpiarButton.setOnClickListener {
            limpiarFormulario()
        }
    }

    // Función para limpiar el formulario
    private fun limpiarFormulario() {
        salarioEditText.text.clear()
        antiguedadEditText.text.clear()
    }

    private fun validarCampos(): Boolean {
        return salarioEditText.text.isNotEmpty() && antiguedadEditText.text.isNotEmpty()
    }

    private fun calcularLiquidacion(salario: Double, antiguedad: Int): Double {
        return salario * antiguedad * 0.5 // Fórmula simplificada para el cálculo de liquidación
    }

    // Función para verificar si el permiso para notificaciones está concedido (Android 13+)
    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED) {

                // Si el permiso no está concedido, solicitarlo
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        } else if (!areNotificationsEnabled()) {
            Toast.makeText(this, "Por favor, habilita las notificaciones en la configuración", Toast.LENGTH_LONG).show()
            val intent = Intent().apply {
                action = "android.settings.APP_NOTIFICATION_SETTINGS"
                putExtra("android.provider.extra.APP_PACKAGE", packageName)
            }
            startActivity(intent)
        }
    }

    // Verificar si las notificaciones están habilitadas en el sistema
    private fun areNotificationsEnabled(): Boolean {
        return NotificationManagerCompat.from(this).areNotificationsEnabled()
    }

    // Opcional: Manejar el resultado de la solicitud de permisos
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de notificaciones concedido", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permiso de notificaciones denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
