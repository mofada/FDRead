package com.example.fada.fdread.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fada.fdread.R
import com.example.fada.fdread.adapter.BookChannelAdapter
import com.example.fada.fdread.adapter.BookHorizontalAdapter
import com.example.fada.fdread.bean.Book
import com.example.fada.fdread.bean.Channel
import kotlinx.android.synthetic.main.fragment_book_store.*

/**
 * A simple [Fragment] subclass.
 */
class BookStoreFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_book_store, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycleView_recommend.adapter = BookHorizontalAdapter(getBook())
        recycleView_recommend.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        recycleView_recommend.itemAnimator = DefaultItemAnimator()

        recycleView_channel.adapter = BookChannelAdapter(getChannel())
        recycleView_channel.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        recycleView_channel.itemAnimator = DefaultItemAnimator()
    }

    private fun  getChannel(): ArrayList<Channel> {
        val channels: ArrayList<Channel> = ArrayList<Channel>()
        channels.add(Channel("玄幻小说","http://www.biqukan.com/files/article/image/1/1094/1094s.jpg","玄幻小说是一种类型小说，思想内容往往幽深玄妙、奇伟瑰丽。不受科学与人文的限制，也不受时空的限制，励志，热血，任凭作者想像力自由发挥。与科幻、奇幻、武侠等幻想性质浓厚的类型小说关系密切。"))
        channels.add(Channel("玄幻小说","http://www.biqukan.com/files/article/image/1/1094/1094s.jpg","玄幻小说是一种类型小说，思想内容往往幽深玄妙、奇伟瑰丽。不受科学与人文的限制，也不受时空的限制，励志，热血，任凭作者想像力自由发挥。与科幻、奇幻、武侠等幻想性质浓厚的类型小说关系密切。"))
        channels.add(Channel("玄幻小说","http://www.biqukan.com/files/article/image/1/1094/1094s.jpg","玄幻小说是一种类型小说，思想内容往往幽深玄妙、奇伟瑰丽。不受科学与人文的限制，也不受时空的限制，励志，热血，任凭作者想像力自由发挥。与科幻、奇幻、武侠等幻想性质浓厚的类型小说关系密切。"))
        channels.add(Channel("玄幻小说","http://www.biqukan.com/files/article/image/1/1094/1094s.jpg","玄幻小说是一种类型小说，思想内容往往幽深玄妙、奇伟瑰丽。不受科学与人文的限制，也不受时空的限制，励志，热血，任凭作者想像力自由发挥。与科幻、奇幻、武侠等幻想性质浓厚的类型小说关系密切。"))
        channels.add(Channel("玄幻小说","http://www.biqukan.com/files/article/image/1/1094/1094s.jpg","玄幻小说是一种类型小说，思想内容往往幽深玄妙、奇伟瑰丽。不受科学与人文的限制，也不受时空的限制，励志，热血，任凭作者想像力自由发挥。与科幻、奇幻、武侠等幻想性质浓厚的类型小说关系密切。"))
        channels.add(Channel("玄幻小说","http://www.biqukan.com/files/article/image/1/1094/1094s.jpg","玄幻小说是一种类型小说，思想内容往往幽深玄妙、奇伟瑰丽。不受科学与人文的限制，也不受时空的限制，励志，热血，任凭作者想像力自由发挥。与科幻、奇幻、武侠等幻想性质浓厚的类型小说关系密切。"))
        return channels
    }

    fun getBook(): List<Book> {
        val books: ArrayList<Book> = ArrayList<Book>()
        books.add(Book("一念永恒", "10194", "耳根", "http://www.biqukan.com/files/article/image/1/1094/1094s.jpg", "一念永恒是耳根的最新小说", "第800章 xxxx"))
        books.add(Book("一念永恒", "10194", "耳根", "http://www.biqukan.com/files/article/image/1/1094/1094s.jpg", "一念永恒是耳根的最新小说", "第800章 xxxx"))
        books.add(Book("一念永恒", "10194", "耳根", "http://www.biqukan.com/files/article/image/1/1094/1094s.jpg", "一念永恒是耳根的最新小说", "第800章 xxxx"))
        books.add(Book("一念永恒", "10194", "耳根", "http://www.biqukan.com/files/article/image/1/1094/1094s.jpg", "一念永恒是耳根的最新小说", "第800章 xxxx"))
        books.add(Book("一念永恒", "10194", "耳根", "http://www.biqukan.com/files/article/image/1/1094/1094s.jpg", "一念永恒是耳根的最新小说", "第800章 xxxx"))
        return books
    }
}
