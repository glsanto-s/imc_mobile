package com.example.imc.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.imc.model.Usuario

@Dao
interface UsuarioDao {
    @Insert
    suspend fun inserirUsuario(usuario: Usuario)

    @Query("SELECT * FROM tabela_usuario")
    suspend fun getUsuarios(): List<Usuario>

    @Update
    suspend fun updateUsuario(usuario: Usuario)

    @Delete
    suspend fun deleteUsuario(usuario: Usuario)
}