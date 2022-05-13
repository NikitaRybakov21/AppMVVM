package com.example.appmvvm.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appmvvm.room.entites.Words

@Dao
interface WordsDao {
    @Query("SELECT * FROM word_table")
    fun getAllWords() : List<Words>

    @Query("SELECT * FROM word_table WHERE word LIKE :name")
    fun getWordName(name : String) : List<Words>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWords(list : List<Words>)
}