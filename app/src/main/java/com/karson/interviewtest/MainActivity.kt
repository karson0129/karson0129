package com.karson.interviewtest

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.karson.interviewtest.Dialog.CustomDialog
import com.karson.interviewtest.Dialog.CustomDialog.Builder.OnPositiveButtonClick
import com.karson.interviewtest.adapter.BookAdapter
import com.karson.interviewtest.data.Book
import com.karson.interviewtest.databinding.ActivityMainBinding
import com.karson.interviewtest.inter.BookDao
import com.karson.interviewtest.room.BookDatabase
import com.karson.interviewtest.viewModel.MainViewModel
import com.karson.interviewtest.viewModel.MainViewModelFactory

class MainActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModelFactory: MainViewModelFactory


    private lateinit var viewModel: MainViewModel

    private lateinit var adapter:BookAdapter

    private lateinit var bookdao: BookDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookdao = BookDatabase.getDataBase(this).bookDao()

        viewModelFactory = MainViewModelFactory(bookdao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        binding.mainViewModel = viewModel

        adapter = BookAdapter()
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        viewModel.allBooks.observe(this, Observer {
            adapter.submitList(it)
        })

        binding.add.setOnClickListener(this@MainActivity)
        binding.delete.setOnClickListener(this@MainActivity)
        binding.enquire.setOnClickListener(this@MainActivity)

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.add -> {
                //添加
                val builder = CustomDialog.Builder(this)
                builder.type = 0
                builder.setPositiveButton(object : OnPositiveButtonClick {
                    override fun onClick(bookName: String, bookAuthor: String, dialogInterface: CustomDialog) {
                        dialogInterface.dismiss()
                        val bookNew:Book = Book(bookName,bookAuthor)
                        viewModel.onInsertBook(bookNew)
                    }
                })
                builder.setNegativeButton(DialogInterface.OnClickListener { dialogInterface, _ ->
                    dialogInterface.dismiss()
                })
                builder.create().show()

            }
            R.id.delete -> {
                //删除
                val builder = CustomDialog.Builder(this)
                builder.type = 1
                builder.setPositiveButton(object : OnPositiveButtonClick {
                    override fun onClick(bookName: String, bookAuthor: String, dialogInterface: CustomDialog) {
                        dialogInterface.dismiss()
                        viewModel.onClear(bookName)
                    }
                })
                builder.setNegativeButton(DialogInterface.OnClickListener { dialogInterface, _ ->
                    dialogInterface.dismiss()
                })
                builder.create().show()

            }
            R.id.enquire -> {
                //查询
                val builder = CustomDialog.Builder(this)
                builder.type = 1
                builder.setPositiveButton(object : OnPositiveButtonClick {
                    override fun onClick(bookName: String, bookAuthor: String, dialogInterface: CustomDialog) {
                        dialogInterface.dismiss()
                        viewModel.enquireBook(bookName)
                    }
                })
                builder.setNegativeButton(DialogInterface.OnClickListener { dialogInterface, _ ->
                    dialogInterface.dismiss()
                })
                builder.create().show()
            }
        }
    }


}
