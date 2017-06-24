package cn.mofada.fdread.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cn.mofada.fdread.R
import cn.mofada.fdread.bean.Book
import com.bumptech.glide.Glide

/**
 * Created by fada on 2017/6/11.
 */
class BookSearchAdapter(var data: List<Book>) : RecyclerView.Adapter<BookSearchAdapter.ViewHolder>() {
    var mContext: Context? = null
    var listener: OnItemClickListener? = null

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        if (mContext == null) {
            mContext = parent?.context
        }
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.item_list_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val book: Book = data.get(position)
        holder?.title?.text = book.title
        Glide.with(mContext).load(book.cover).into(holder?.image)
        holder?.author_type?.text = "${book.author}/${book.type}"
        holder?.intro?.text = book.intro
        if (holder != null) {
            setItemEvents(holder)
        }
    }


    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var title: TextView? = itemView?.findViewById(R.id.item_list_title)
        var image: ImageView? = itemView?.findViewById(R.id.item_list_image)
        var author_type: TextView? = itemView?.findViewById(R.id.item_list_author_type)
        var intro: TextView? = itemView?.findViewById(R.id.item_list_intro)
    }

    fun listener(listener: OnItemClickListener): BookSearchAdapter {
        this.listener = listener
        return this
    }

    fun setItemEvents(holder: ViewHolder) {
        if (listener != null) {
            holder.itemView.setOnClickListener {
                val layoutPosition = holder.layoutPosition
                listener?.onItemClick(holder.itemView, layoutPosition)
            }
        }
    }

    fun refresh(data: List<Book>) {
        this.data = data
        notifyDataSetChanged()
    }
}