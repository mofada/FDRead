package com.example.fada.fdread.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fada.fdread.R
import com.example.fada.fdread.adapter.BookAdapter
import com.example.fada.fdread.adapter.OnItemClickListener
import com.example.fada.fdread.bean.Book
import kotlinx.android.synthetic.main.fragment_book_shelf.*


/**
 * A simple [Fragment] subclass.
 */
class BookShelfFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_book_shelf, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycleView_main.adapter = BookAdapter(getBook()).listener(OnBookClick())

        recycleView_main.layoutManager = GridLayoutManager(activity, 3)
        recycleView_main.itemAnimator = DefaultItemAnimator()
    }

    fun getBook(): List<Book> {
        val books: ArrayList<Book> = ArrayList<Book>()
        books.add(Book("一念永恒", "10194", "耳根", "http://www.biqukan.com/files/article/image/1/1094/1094s.jpg", "一念永恒是耳根的最新小说", "第800章 xxxx"))
        books.add(Book("一念永恒", "10194", "耳根", "http://www.biqukan.com/files/article/image/1/1094/1094s.jpg", "一念永恒是耳根的最新小说", "第800章 xxxx"))
        books.add(Book("一念永恒", "10194", "耳根", "http://www.biqukan.com/files/article/image/1/1094/1094s.jpg", "一念永恒是耳根的最新小说", "第800章 xxxx"))
        books.add(Book("一念永恒", "10194", "耳根", "http://www.biqukan.com/files/article/image/1/1094/1094s.jpg", "一念永恒是耳根的最新小说", "第800章 xxxx"))
        books.add(Book("一念永恒", "10194", "耳根", "http://www.biqukan.com/files/article/image/1/1094/1094s.jpg", "一念永恒是耳根的最新小说", "第800章 xxxx"))
        books.add(Book("一念永恒", "10194", "耳根", "http://www.biqukan.com/files/article/image/1/1094/1094s.jpg", "一念永恒是耳根的最新小说", "第800章 xxxx"))
        books.add(Book("一念永恒", "10194", "耳根", "http://www.biqukan.com/files/article/image/1/1094/1094s.jpg", "一念永恒是耳根的最新小说", "第800章 xxxx"))
        books.add(Book("一念永恒", "10194", "耳根", "http://www.biqukan.com/files/article/image/1/1094/1094s.jpg", "一念永恒是耳根的最新小说", "第800章 xxxx"))
        return books
    }

    class OnBookClick : OnItemClickListener{
        override fun onItemClick(view: View, position: Int) {
            Log.d("TAG"," - " + position)
        }

        override fun onItemLongClick(view: View, position: Int) {
            Log.d("TAG"," - " + position)
        }

    }
}
