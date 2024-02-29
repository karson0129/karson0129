package com.karson.interviewtest.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.karson.interviewtest.data.Book
import com.karson.interviewtest.inter.BookDao

@Database(version = 1,entities = [Book::class])
abstract class BookDatabase: RoomDatabase() {
    abstract fun bookDao():BookDao

    companion object{
        private var instance:BookDatabase?=null

        fun getDataBase(context: Context):BookDatabase {
            //instance 不为空直接执行let函数返回instance, 为空则不执行let函数
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext
                , BookDatabase::class.java, "book_database"
            ).build().apply { instance = this }
        }
    }
}