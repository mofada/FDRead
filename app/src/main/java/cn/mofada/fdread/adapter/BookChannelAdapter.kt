package cn.mofada.fdread.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cn.mofada.fdread.R
import cn.mofada.fdread.bean.Channel
import com.bumptech.glide.Glide

/**
 * Created by fada on 2017/6/11.
 */
class BookChannelAdapter(var context: Context) : RecyclerView.Adapter<BookChannelAdapter.ViewHolder>() {
    var mContext: Context? = null
    var channels: ArrayList<Channel> = getChannel()
    var listener: OnItemClickListener? = null

    override fun getItemCount(): Int = channels.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        if (mContext == null) {
            mContext = parent?.context
        }
        var view: View = LayoutInflater.from(mContext).inflate(R.layout.item_list_channel, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val channel: Channel = channels.get(position)
        holder?.title?.text = channel.title
        Glide.with(mContext).load(channel.cover).into(holder?.image)
        holder?.description?.text = channel.descriptor
        if (holder != null) {
            setItemEvents(holder)
        }
    }


    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var title: TextView? = itemView?.findViewById(R.id.item_list_title)
        var image: ImageView? = itemView?.findViewById(R.id.item_list_image)
        var description: TextView? = itemView?.findViewById(R.id.item_list_intro)

    }

    private fun getChannel(): ArrayList<Channel> {
        val channels: ArrayList<Channel> = ArrayList<Channel>()
        channels.add(Channel("小说排行榜", "http://www.biqukan.com/files/article/image/20/20623/20623s.jpg", context.getString(R.string.intro_ranking), 0))
        channels.add(Channel("玄幻小说", "http://www.biqukan.com/files/article/image/1/1094/1094s.jpg", context.getString(R.string.intro_fantasy), 1))
        channels.add(Channel("修真小说", "http://www.biqukan.com/files/article/image/3/3313/3313s.jpg", context.getString(R.string.intro_xiuzhen), 2))
        channels.add(Channel("都市小说", "http://www.biqukan.com/files/article/image/24/24527/24527s.jpg", context.getString(R.string.intro_city), 3))
        channels.add(Channel("穿越小说", "http://www.biqukan.com/files/article/image/20/20882/20882s.jpg", context.getString(R.string.intro_through), 4))
        channels.add(Channel("网游小说", "http://www.biqukan.com/files/article/image/2/2675/2675s.jpg", context.getString(R.string.intro_game), 5))
        channels.add(Channel("科幻小说", "http://www.biqukan.com/files/article/image/16/16516/16516s.jpg", context.getString(R.string.intro_science), 6))
        channels.add(Channel("其他小说", "http://www.biqukan.com/files/article/image/17/17366/17366s.jpg", context.getString(R.string.intro_other), 7))
        channels.add(Channel("完本小说", "http://www.biqukan.com/files/article/image/3/3037/3037s.jpg", context.getString(R.string.intro_end), 8))
        return channels
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
}