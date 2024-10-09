package com.roberto.asesoramiento

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AsesoramientoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asesoramiento)

        // Botón para regresar a la actividad anterior
        val btnRegresar = findViewById<Button>(R.id.btnRegresar)
        btnRegresar.setOnClickListener {
            finish() // Cierra esta actividad y regresa
        }
    }
}
