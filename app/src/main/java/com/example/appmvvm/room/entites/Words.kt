package com.example.appmvvm.room.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_table")
data class Words(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    @ColumnInfo(name = "word") val word : String,
    @ColumnInfo(name = "wordURL") val wordURI : String
)
