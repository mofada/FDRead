package cn.mofada.fdread.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cn.mofada.fdread.R
import cn.mofada.fdread.bean.Discuss
import com.bumptech.glide.Glide

/**
 * Created by fada on 2017/6/11.
 * 讨论适配器
 */
class BookDiscussAdapter(var data: List<Discuss>) : RecyclerView.Adapter<BookDiscussAdapter.ViewHolder>() {
    var mContext: Context? = null
    var listener: OnItemClickListener? = null

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        if (mContext == null) {
            mContext = parent?.context
        }
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.item_list_discuss, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val discuss: Discuss = data[position]
        holder?.title?.text = discuss.name
        Glide.with(mContext).load(discuss.iconurl).into(holder?.image)
        holder?.content?.text = discuss.content
        if (holder != null) {
            setItemEvents(holder)
        }
    }


    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var title: TextView? = itemView?.findViewById(R.id.item_list_title)
        var image: ImageView? = itemView?.findViewById(R.id.item_list_image)
        var content: TextView? = itemView?.findViewById(R.id.item_list_intro)
    }

    fun listener(listener: OnItemClickListener): BookDiscussAdapter {
        this.listener = listener
        return this
    }

    fun setItemEvents(holder: ViewHolder) {
        if (listener != null) {
            holder.itemView.setOnClickListener{
                val layoutPosition = holder.getLayoutPosition()
                listener?.onItemClick(holder.itemView, layoutPosition)
            }
        }
    }

    fun  refresh(data: List<Discuss>) {
        this.data = data
        notifyDataSetChanged()
    }
}