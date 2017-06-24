package com.example.fada.fdread.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.fada.fdread.R
import com.example.fada.fdread.bean.Book

/**
 * Created by fada on 2017/6/11.
 */
class BookHorizontalAdapter(var data: List<Book>) : RecyclerView.Adapter<BookHorizontalAdapter.ViewHolder>() {
    var mContext: Context? = null

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        if (mContext == null) {
            mContext = parent?.context
        }
        var view: View = LayoutInflater.from(mContext).inflate(R.layout.item_grid_book_recommend, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val book: Book = data.get(position)
        holder?.title?.text = book.title
        Glide.with(mContext).load(book.image).into(holder?.image)
        holder?.update?.text =book.update
    }


    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var title: TextView? = itemView?.findViewById(R.id.item_grid_title)
        var image: ImageView? = itemView?.findViewById(R.id.item_grid_image)
        var update: TextView? = itemView?.findViewById(R.id.item_grid_update)
    }
}