package com.karson.interviewtest.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Book(var bookName:String, var bookAuthor:String) {
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0

}