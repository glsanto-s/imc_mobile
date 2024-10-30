package com.example.imc

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
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
        val containerUsuarios = findViewById<LinearLayout>(R.id.containerUsuarios)


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
            if (containerUsuarios.visibility == View.GONE) {
                containerUsuarios.visibility = View.VISIBLE
            } else {
                containerUsuarios.visibility = View.GONE
            }
            containerUsuarios.removeAllViews()
            lifecycleScope.launch {
                val usuarios = usuarioDao.getUsuarios()
                if (usuarios.isEmpty()) {
                    // Exibe uma mensagem "Nenhum usuário encontrado" se a lista estiver vazia
                    val textViewInfo = TextView(this@MainActivity).apply {
                        text = "Nenhum usuário encontrado"
                        textSize = 16f
                        setPadding(8, 8, 8, 8)
                    }
                }
                else {
                    for (usuario in usuarios) {
                        val nomeEditText = EditText(this@MainActivity).apply {
                            setText(usuario.nome)
                            textSize = 16f
                            setPadding(8, 8, 8, 8)
                        }

                        val pesoEditText = EditText(this@MainActivity).apply {
                            setText(usuario.peso.toString())
                            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                            textSize = 16f
                            setPadding(8, 8, 8, 8)
                        }

                        val alturaEditText = EditText(this@MainActivity).apply {
                            setText(usuario.altura.toString())
                            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                            textSize = 16f
                            setPadding(8, 8, 8, 8)
                        }

                        val peso = usuario.peso?.toFloat() ?: 0f
                        val altura = usuario.altura?.toFloat() ?: 0f
                        val retorno = calcularIMC(peso,altura)

                        val imcTextView = TextView(this@MainActivity).apply {
                            setText(retorno.toString())
                            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                            textSize = 16f
                            setPadding(8, 8, 8, 8)
                        }

                        // Botão "Editar" para confirmar a edição
                        val btnEditar = Button(this@MainActivity).apply {
                            text = "Editar"
                            setOnClickListener {
                                // Atualize os dados do usuário com os valores dos EditTexts
                                usuario.nome = nomeEditText.text.toString()
                                usuario.peso = pesoEditText.text.toString().toFloatOrNull() ?: usuario.peso
                                usuario.altura = alturaEditText.text.toString().toFloatOrNull() ?: usuario.altura

                                // Salve os dados no banco de dados
                                lifecycleScope.launch {
                                    usuarioDao.updateUsuario(usuario)
                                    Toast.makeText(this@MainActivity, "Usuário atualizado!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        val btnDeletar = Button(this@MainActivity).apply {
                            text = "Deletar"
                            setOnClickListener {
                                lifecycleScope.launch {
                                    usuarioDao.deleteUsuario(usuario)
                                    Toast.makeText(this@MainActivity, "Usuário deletado!", Toast.LENGTH_SHORT).show()

                                    containerUsuarios.removeAllViews()
                                    btnMostrar.performClick()
                                }
                            }
                        }


                        containerUsuarios.addView(nomeEditText)
                        containerUsuarios.addView(pesoEditText)
                        containerUsuarios.addView(alturaEditText)
                        containerUsuarios.addView(imcTextView)
                        containerUsuarios.addView(btnEditar)
                        containerUsuarios.addView(btnDeletar)
                    }
                }

            }
        }

    }

    fun calcularIMC(peso:Float,altura:Float):Float{
        val imc = peso / (altura*altura)
        return imc
    }

}