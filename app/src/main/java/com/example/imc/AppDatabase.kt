package com.example.imc

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.imc.dao.UsuarioDao
import com.example.imc.model.Usuario

@Database(entities = [Usuario::class], version = 1)

abstract class AppDatabase : RoomDatabase() {

    abstract  fun usuarioDao(): UsuarioDao

    companion object{

        private const  val DATABASE_NAME = "DB_IMC"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}