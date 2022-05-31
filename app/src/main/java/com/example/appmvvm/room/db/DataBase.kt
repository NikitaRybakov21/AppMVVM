package com.example.appmvvm.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appmvvm.room.dao.WordsDao
import com.example.appmvvm.room.entites.Words

@Database(entities = [Words::class], version = 1, exportSchema = true)
abstract class DataBase : RoomDatabase() {
    abstract fun dataBase() : WordsDao
}