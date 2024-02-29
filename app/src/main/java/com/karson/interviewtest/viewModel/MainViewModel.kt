package com.karson.interviewtest.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.karson.interviewtest.data.Book
import com.karson.interviewtest.inter.BookDao
import kotlinx.coroutines.launch

class MainViewModel(private val database: BookDao):ViewModel() {

    private val TAG:String = "MainViewModel"

    var allBooks:LiveData<List<Book>> = database.loadAllUsers()


    /***
     * 添加
     */
    fun onInsertBook(book: Book) {
        viewModelScope.launch {
            insert(book)
            allBooks = getTonightFromDatabase()
        }
    }

    /**
     * 删除
     */
    fun onClear(bookName:String) {
        viewModelScope.launch {
            clear(bookName)
            allBooks = getTonightFromDatabase()

        }
    }

    /**
     * 查询
     */
    fun enquireBook(bookName:String){
        viewModelScope.launch {
            if (enquire(bookName).value == null){
                allBooks = MutableLiveData<List<Book>>()
            }else{
                allBooks = enquire(bookName)
            }
        }
    }

    private suspend fun getTonightFromDatabase(): LiveData<List<Book>> {
        return database.loadAllUsers()
    }

    private suspend fun insert(book: Book) {
        database.insertBook(book)
    }

    suspend fun clear(bookName:String) {
        database.deleteBook(bookName)
    }

    private suspend fun enquire(bookName: String): LiveData<List<Book>>{
       return database.loadBookName(bookName)
    }


    override fun onCleared() {
        super.onCleared()
        Log.d(TAG,"Activity被杀死后也会被销毁！")
    }
}