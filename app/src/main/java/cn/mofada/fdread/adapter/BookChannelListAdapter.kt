package cn.mofada.fdread.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.mofada.fdread.R
import cn.mofada.fdread.bean.Book

/**
 * Created by fada on 2017/6/11.
 */
class BookChannelListAdapter(var data: List<Book>) : RecyclerView.Adapter<BookChannelListAdapter.ViewHolder>() {
    var mContext: Context? = null
    var listener: OnItemClickListener? = null

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        if (mContext == null) {
            mContext = parent?.context
        }
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.item_list_channel_book, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val book: Book = data[position]
        holder?.time?.text = book.time
        holder?.title_author?.text = "${book.title}/${book.author}"
        holder?.update?.text = book.update_
        if (holder != null) {
            setItemEvents(holder)
        }
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var time: TextView? = itemView?.findViewById(R.id.item_list_time)
        var title_author: TextView? = itemView?.findViewById(R.id.item_list_title_author)
        var update: TextView? = itemView?.findViewById(R.id.item_list_update)
    }

    fun listener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun refresh(data: List<Book>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setItemEvents(holder: ViewHolder) {
        if (listener != null) {
            holder.itemView.setOnClickListener {
                val layoutPosition = holder.layoutPosition
                listener?.onItemClick(holder.itemView, layoutPosition)
            }
        }
    }
}