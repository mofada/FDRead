package cn.mofada.fdread.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.mofada.fdread.R
import cn.mofada.fdread.bean.Chapter

/**
 * Created by fada on 2017/6/11.
 */
class BookChapterAdapter(var data: List<Chapter>) : RecyclerView.Adapter<BookChapterAdapter.ViewHolder>() {
    var mContext: Context? = null
    var listener: OnItemClickListener? = null

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        if (mContext == null) {
            mContext = parent?.context
        }
        var view: View = LayoutInflater.from(mContext).inflate(R.layout.item_list_chapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val chapter: Chapter = data.get(position)
        holder?.title?.text = chapter.title
        if (holder != null) {
            setItemEvents(holder)
        }
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var title: TextView? = itemView?.findViewById(R.id.item_list_title)
    }

    fun listener(listener: OnItemClickListener): BookChapterAdapter {
        this.listener = listener
        return this
    }

    fun refresh(data: List<Chapter>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun reversed() {
        data = data.reversed()
        notifyDataSetChanged()
    }

    fun setItemEvents(holder: ViewHolder) {
        if (listener != null) {
            holder.itemView.setOnClickListener(View.OnClickListener {
                val layoutPosition = holder.getLayoutPosition()
                listener?.onItemClick(holder.itemView, layoutPosition)
            })
        }
    }
}