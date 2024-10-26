package com.example.imc.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.imc.model.Usuario

@Dao
interface UsuarioDao {
    @Insert
    suspend fun inserirUsuario(usuario: Usuario)

    @Query("SELECT * FROM tabela_usuario")
    suspend fun getUsuarios(): List<Usuario>
}