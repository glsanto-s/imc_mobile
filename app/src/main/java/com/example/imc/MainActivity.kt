package com.example.imc

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val peso = findViewById<EditText>(R.id.peso)
        val altura = findViewById<EditText>(R.id.altura)
        val btnLimpar = findViewById<Button>(R.id.btn_Limpar)
        val btnEnviar = findViewById<Button>(R.id.btn_Enviar)
        val textViewResultado = findViewById<TextView>(R.id.res_IMC)

        btnEnviar.setOnClickListener{
            val resPeso = peso.text.toString()
            val resAltura = altura.text.toString()

            if(resAltura.isNotEmpty() and resPeso.isNotEmpty()){
                val pesoDecimal = resPeso.toFloat()
                val alturaDecimal = resAltura.toFloat()

                val resultado = calcularIMC(pesoDecimal,alturaDecimal)
                textViewResultado.text = "IMC: $resultado"
            }
            else
            {
                textViewResultado.text = "Por favor, preencher todos os campos!"
            }
        }

        btnLimpar.setOnClickListener{
            peso.text.clear()
            altura.text.clear()
            textViewResultado.text = "IMC:"

            Toast.makeText(this, "Seleção limpa", Toast.LENGTH_SHORT).show()
        }
    }

    fun calcularIMC(peso:Float,altura:Float,):Float{
        val imc = peso / (altura*altura)
        return imc
    }

}