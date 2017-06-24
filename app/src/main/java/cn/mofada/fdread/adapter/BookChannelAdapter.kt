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
import com.example.fada.fdread.bean.Channel

/**
 * Created by fada on 2017/6/11.
 */
class BookChannelAdapter(var data: List<Channel>) : RecyclerView.Adapter<BookChannelAdapter.ViewHolder>() {
    var mContext: Context? = null

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        if (mContext == null) {
            mContext = parent?.context
        }
        var view: View = LayoutInflater.from(mContext).inflate(R.layout.item_list_channel, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val channel: Channel = data.get(position)
        holder?.title?.text = channel.title
        Glide.with(mContext).load(channel.image).into(holder?.image)
        holder?.description?.text =channel.descriptor
    }


    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var title: TextView? = itemView?.findViewById(R.id.item_list_title)
        var image: ImageView? = itemView?.findViewById(R.id.item_list_image)
        var description: TextView? = itemView?.findViewById(R.id.item_list_description)

    }
}