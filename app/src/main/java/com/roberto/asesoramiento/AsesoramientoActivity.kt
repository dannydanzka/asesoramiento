// AsesoramientoActivity.kt
package com.roberto.asesoramiento

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AsesoramientoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asesoramiento)

        // Mostrar información introductoria sobre leyes laborales
        val textoIntroductorio = findViewById<TextView>(R.id.textoIntroductorio)
        textoIntroductorio.text = """
            Bienvenido a la asesoría legal.
            Aquí podrás conocer tus derechos laborales y obtener más información 
            sobre las leyes que te protegen en el ámbito laboral.
        """.trimIndent()

        // Botón para ir al inicio de la aplicación
        val btnIrAlInicio = findViewById<Button>(R.id.btnRegresar)
        btnIrAlInicio.setOnClickListener {
            // Crear un Intent para iniciar MainActivity
            val intent = Intent(this, MainActivity::class.java).apply {
                // Limpiar la pila de actividades para evitar regresar
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }
}
