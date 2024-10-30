package com.example.imc.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabela_usuario")
data class Usuario (

    @ColumnInfo(name = "nome") var nome: String?,
    @ColumnInfo(name = "peso") var peso: Float?,
    @ColumnInfo(name = "altura") var altura: Float?,
    @ColumnInfo(name = "imc") val imc: Float?
){
    @PrimaryKey(autoGenerate = true)
    var uid:Int = 0
}