// ResultadoActivity.kt
package com.roberto.asesoramiento

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ResultadoActivity : AppCompatActivity() {

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
        val btnSolicitarAsesoria = findViewById<Button>(R.id.btnAsesoramiento)
        btnSolicitarAsesoria.text = "Solicitar Asesoría"
        btnSolicitarAsesoria.setOnClickListener {
            // Mostrar Toast indicando que la notificación ha sido enviada
            Toast.makeText(this, "Notificación enviada", Toast.LENGTH_SHORT).show()

            // Mostrar notificación con opciones (Aceptar/Cancelar)
            NotificationHelper.mostrarNotificacionConBotones(this)
        }

        // Botón para regresar a la actividad anterior
        val btnRegresar = findViewById<Button>(R.id.btnRegresar)
        btnRegresar.setOnClickListener {
            finish() // Cierra esta actividad y regresa a la pantalla inicial
        }
    }
}
