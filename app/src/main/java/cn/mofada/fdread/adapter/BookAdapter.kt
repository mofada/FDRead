package cn.mofada.fdread.adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import cn.mofada.fdread.R
import cn.mofada.fdread.base.Constant
import cn.mofada.fdread.bean.Book
import cn.mofada.fdread.bean.Chapter
import cn.mofada.fdread.gson.Line
import cn.mofada.fdread.retrofit.RetrofitServices
import cn.mofada.fdread.utils.PrefersUtils
import com.bumptech.glide.Glide
import org.litepal.crud.DataSupport
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by fada on 2017/6/11.
 * 书架书籍适配器
 */
class BookAdapter(var data: ArrayList<Book>) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {
    var mContext: Context? = null
    var listener: OnClickListener? = null

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        if (mContext == null) {
            mContext = parent?.context
        }
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.item_grid_book, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val book: Book = data[position]
        holder?.title?.text = book.title
        Glide.with(mContext).load(book.cover).into(holder?.image)
        if (TextUtils.isEmpty(book.currChapter)) {
            book.currChapter = "未读"
        }
        holder?.currChapter?.text = "已读:${book.currChapter}"
        if (holder != null) {
            setItemEvents(holder)
        }
    }


    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var title: TextView? = itemView?.findViewById(R.id.item_grid_title)
        var image: ImageView? = itemView?.findViewById(R.id.item_grid_image)
        var currChapter: TextView? = itemView?.findViewById(R.id.item_grid_update)
        var card: CardView? = itemView?.findViewById(R.id.item_grid_card)
        var delete: Button? = itemView?.findViewById(R.id.item_grid_button)
    }

    fun listener(listener: OnClickListener) {
        this.listener = listener
    }

    fun refresh(data: ArrayList<Book>) {
        this.data = data
        notifyDataSetChanged()
    }

    /**
     * 设置事件
     */
    fun setItemEvents(holder: ViewHolder) {
        if (listener != null) {
            holder.card?.setOnClickListener {
                val layoutPosition = holder.layoutPosition
                listener?.onItemClick(holder.card!!, layoutPosition)
            }

            holder.delete?.setOnClickListener {
                val layoutPosition = holder.layoutPosition
                listener?.onRemoveClick(holder.delete!!, layoutPosition)
            }
        }
    }

    /**
     * 事件接口
     */
    interface OnClickListener : OnItemClickListener {
        fun onRemoveClick(view: View, position: Int)

        override fun onItemClick(view: View, position: Int)
    }

    /**
     * 移除指定位置
     * @position 位置
     */
    fun remove(position: Int) {
//        DataSupport.delete(Book::class.java, data[position].id)
        val book: Book = data[position]
        book.deleteAsync().listen {
            data.removeAt(position)
            notifyItemRemoved(position)
            DataSupport.deleteAll(Chapter::class.java, "list = '${book.bookId}'")
            if (PrefersUtils.getBoolean(Constant.PREFERS_ISLOGIN)) {
                book.uid = PrefersUtils.getString(Constant.PREFERS_UID)
                RetrofitServices.getInstance().getRetrofitAndGson().book_delete(book.uid, book.bookId).enqueue(object : Callback<Line> {
                    override fun onResponse(call: Call<Line>?, response: Response<Line>?) {}

                    override fun onFailure(call: Call<Line>?, t: Throwable?) {}
                })
            }
        }
    }
}