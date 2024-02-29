package com.karson.interviewtest.inter

import androidx.lifecycle.LiveData
import androidx.room.*
import com.karson.interviewtest.data.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Insert
    suspend fun insertBook(book: Book):Long

    @Update
    suspend fun updateBook(newBook: Book)

    @Query("select * from Book")
    fun loadAllUsers():LiveData<List<Book>>

    @Query("SELECT * FROM Book WHERE bookName = :name")
    fun loadBookName(name:String):LiveData<List<Book>>

    @Query("delete from Book WHERE bookName = :name")
    suspend fun deleteBook(name:String)


}