package com.example.imc

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.imc.dao.UsuarioDao
import com.example.imc.databinding.ActivityCadastroUsuarioBinding
import com.example.imc.databinding.ActivityMainBinding
import com.example.imc.model.Usuario
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var usuarioDao: UsuarioDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val database = AppDatabase.getInstance(this)
        usuarioDao = database.usuarioDao()

        val nome = findViewById<EditText>(R.id.nome)
        val peso = findViewById<EditText>(R.id.peso)
        val altura = findViewById<EditText>(R.id.altura)
        val btnLimpar = findViewById<Button>(R.id.btn_Limpar)
        val btnEnviar = findViewById<Button>(R.id.btn_Enviar)
        val btnMostrar = findViewById<Button>(R.id.btn_Mostrar)
        val textViewResultado = findViewById<TextView>(R.id.res_IMC)
        val textViewUsuarios = findViewById<TextView>(R.id.res_Usuarios)


        btnEnviar.setOnClickListener{
            val resPeso = peso.text.toString()
            val resAltura = altura.text.toString()
            val resNome = nome.text.toString()

            if(resAltura.isNotEmpty() and resPeso.isNotEmpty() and resNome.isNotEmpty()){
                val pesoDecimal = resPeso.toFloat()
                val alturaDecimal = resAltura.toFloat()

                val resultado = calcularIMC(pesoDecimal,alturaDecimal)
                textViewResultado.text = "IMC: $resultado"


                val user = Usuario(
                    nome = resNome,
                    peso = resPeso.toFloat(),
                    altura = resAltura.toFloat(),
                    imc = resultado
                )

                GlobalScope.launch {
                    usuarioDao.inserirUsuario(user)
                    runOnUiThread{
                        Toast.makeText(this@MainActivity, "Usuário inserido com sucesso", Toast.LENGTH_SHORT).show()
                    }
                }
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

        btnMostrar.setOnClickListener {
            lifecycleScope.launch {
                val usuarios = usuarioDao.getUsuarios()

                if (usuarios.isNotEmpty()) {
                    val usuariosInfo = usuarios.joinToString("<br><br>") { usuario ->
                        "<b>Nome:</b> ${usuario.nome}<br><b>Peso:</b> ${usuario.peso} kg<br>" +
                                "<b>Altura:</b> ${usuario.altura} m<br><b>IMC:</b> ${usuario.imc}"
                    }
                    textViewUsuarios.text = Html.fromHtml(usuariosInfo)  // Exibe com HTML estilizado
                } else {
                    textViewUsuarios.text = "Nenhum usuário encontrado"
                }
            }
        }

    }

    fun calcularIMC(peso:Float,altura:Float,):Float{
        val imc = peso / (altura*altura)
        return imc
    }

}