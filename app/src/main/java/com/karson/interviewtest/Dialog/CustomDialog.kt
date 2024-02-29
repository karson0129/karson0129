package com.karson.interviewtest.Dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.karson.interviewtest.R

class CustomDialog :Dialog {
    constructor(context: Context?):super (context!!)
    constructor(context: Context?,theme:Int):super (context!!,theme)

    class Builder (private val context: Context) {
        var content :String? = null
        var type:Int = 0
        private var positiveButtonClickListener:Builder.OnPositiveButtonClick? = null
        private var negativeButtonClickListener:DialogInterface.OnClickListener? = null

        public interface OnPositiveButtonClick{
            fun onClick(bookName:String,bookAuthor:String,dialog: CustomDialog)
        }

        fun setContent(content:String):Builder{
            this.content = content
            return this
        }

        /**
         * 如果传入0就是 添加书 ； 1就是 删除和查询书
         */
        fun setType(type:Int):Builder{
            this.type = type
            return this
        }

        fun setPositiveButton(listener: Builder.OnPositiveButtonClick): Builder {
            this.positiveButtonClickListener =listener
            return this
        }

        fun setNegativeButton(listener: DialogInterface.OnClickListener): Builder {
            this.negativeButtonClickListener =listener
            return this
        }

        fun create(): CustomDialog {
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            //为自定义弹窗设置主题
            val customDialog = CustomDialog(context, R.style.CustomDialog)
            val view = layoutInflater.inflate(R.layout.dialog_confirm,null)
            val editBookName:EditText = view.findViewById(R.id.edit_bookName)
            val editBookAuthor:EditText = view.findViewById(R.id.edit_bookAuthor)
            val linearBookName:LinearLayout = view.findViewById(R.id.linear_bookName)
            val linearBookAuthor:LinearLayout = view.findViewById(R.id.linear_author)
            customDialog.addContentView(view,LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ))

            if (type == 1){
                linearBookAuthor.visibility = View.GONE
            }else{
                linearBookName.visibility = View.VISIBLE
                linearBookAuthor.visibility = View.VISIBLE
            }

            //设置弹框内容
            content?.let {
                (view.findViewById(R.id.dialog_content) as TextView).text = it
            }
            //设置弹框按钮
            positiveButtonClickListener?.let {

                (view.findViewById(R.id.dialog_sure) as Button).setOnClickListener {
                    if (TextUtils.isEmpty(editBookName.text.toString())){
                        Toast.makeText(context, "请输入书名", Toast.LENGTH_LONG).show()
                    }
                    if (type == 0){
                        if (TextUtils.isEmpty(editBookAuthor.text.toString())){
                            Toast.makeText(context, "请输入作者", Toast.LENGTH_LONG).show()
                        }
                    }
                    positiveButtonClickListener!!.onClick(editBookName.text.toString()
                        ,editBookAuthor.text.toString(),customDialog)
                }
            } ?: run {
                (view.findViewById(R.id.dialog_sure) as Button).visibility = View.GONE
            }
            negativeButtonClickListener?.let {
                (view.findViewById(R.id.dialog_cancel) as Button).setOnClickListener {
                    negativeButtonClickListener!!.onClick(customDialog, DialogInterface.BUTTON_NEGATIVE)
                }
            } ?: run {
                (view.findViewById(R.id.dialog_cancel) as Button).visibility = View.GONE
            }

            customDialog.setContentView(view)
            return customDialog
        }
    }

}