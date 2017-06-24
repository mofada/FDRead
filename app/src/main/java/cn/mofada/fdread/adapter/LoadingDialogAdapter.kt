package cn.mofada.fdread.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.mofada.fdread.R
import com.wang.avi.AVLoadingIndicatorView

/**
 * Created by fada on 2017/6/11.
 * 加载动画
 */
class LoadingDialogAdapter(var data: List<String>) : RecyclerView.Adapter<LoadingDialogAdapter.ViewHolder>() {
    var mContext: Context? = null
    var listener: OnItemClickListener? = null

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        if (mContext == null) {
            mContext = parent?.context
        }
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.dialog_loading, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val indicator: String = data[position]
        holder?.indicator?.setIndicator(indicator)
        if (holder != null) {
            setItemEvents(holder)
        }
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var indicator: AVLoadingIndicatorView? = itemView?.findViewById(R.id.loadingView)
    }

    fun listener(listener: OnItemClickListener) {
        this.listener = listener
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