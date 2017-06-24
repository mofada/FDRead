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
 * 推荐适配器
 */
class BookHorizontalAdapter(var data: List<Book>) : RecyclerView.Adapter<BookHorizontalAdapter.ViewHolder>() {
    var mContext: Context? = null
    var listener: OnItemClickListener? = null

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        if (mContext == null) {
            mContext = parent?.context
        }
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.item_grid_book_recommend, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val book: Book = data[position]
        holder?.title?.text = book.title
        Glide.with(mContext).load(book.cover).into(holder?.image)
        holder?.detail?.text = book.intro
        if (holder != null) {
            setItemEvents(holder)
        }
    }

    fun listener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun setItemEvents(holder: ViewHolder) {
        if (listener != null) {
            holder.itemView.setOnClickListener {
                val layoutPosition = holder.getLayoutPosition()
                listener?.onItemClick(holder.itemView, layoutPosition)
            }
        }
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var title: TextView? = itemView?.findViewById(R.id.item_grid_title)
        var image: ImageView? = itemView?.findViewById(R.id.item_grid_image)
        var detail: TextView? = itemView?.findViewById(R.id.item_grid_update)
    }

    fun refresh( data: List<Book>){
        this.data = data
        notifyDataSetChanged()
    }
}