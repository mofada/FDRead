package cn.mofada.fdread.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import cn.mofada.fdread.BookDetailActivity
import cn.mofada.fdread.BookReadActivity
import cn.mofada.fdread.R
import cn.mofada.fdread.adapter.BookAdapter
import cn.mofada.fdread.base.Constant
import cn.mofada.fdread.bean.Book
import cn.mofada.fdread.bean.Chapter
import cn.mofada.fdread.utils.PrefersUtils
import com.bumptech.glide.Glide
import com.mcxtzhang.swipemenulib.SwipeMenuLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_book_shelf.*
import org.litepal.crud.DataSupport
import org.litepal.crud.callback.FindMultiCallback


/**
 * A simple [Fragment] subclass.
 */
class BookShelfFragment : Fragment() {
    var adapter: BookAdapter = BookAdapter(ArrayList<Book>())

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_book_shelf, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.listener(object : BookAdapter.OnClickListener {
            override fun onRemoveClick(view: View, position: Int) {
                adapter.remove(position)
            }

            override fun onItemClick(view: View, position: Int) {
                val book = adapter.data[position]
                if (!TextUtils.isEmpty(book.currChapterId)) {
                    BookReadActivity.startActivity(activity, Chapter("", book.currChapterId!!, "", "", "", ""))
                } else {
                    BookDetailActivity.startActivity(activity, book)
                }
            }
        })

        recycleView_main.adapter = adapter
        recycleView_main.layoutManager = GridLayoutManager(activity, 3)
        recycleView_main.itemAnimator = DefaultItemAnimator()
        recycleView_main.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val viewCache = SwipeMenuLayout.getViewCache()
                viewCache?.smoothClose()
            }
            false
        }

        toolbar_user.setOnClickListener {
            activity.drawLayout.openDrawer(GravityCompat.START)
        }

        getBook()

        showUser()
    }

    fun showUser() {
        if (PrefersUtils.getBoolean(Constant.PREFERS_ISLOGIN)) {
            val intent: Intent = Intent(Constant.ACTION_LOGININ)
            intent.putExtra(Constant.USER_NAME, PrefersUtils.getString(Constant.PREFERS_NAME))
            intent.putExtra(Constant.USER_ICONURL, PrefersUtils.getString(Constant.PREFERS_ICONURL))
            activity.sendBroadcast(intent)
        }
    }

    fun loginIn(intent: Intent) {
        Glide.with(activity).load(intent.getStringExtra(Constant.USER_ICONURL)).into(toolbar_user)
    }

    fun loginOut() {
        Glide.with(activity).load(R.mipmap.png_user).into(toolbar_user)
    }

    fun getBook() {
        DataSupport.findAllAsync(Book::class.java).listen(object : FindMultiCallback {
            override fun <T> onFinish(t: List<T>?) {
                val data: List<Book>? = t as ArrayList<Book>
                adapter.refresh(ArrayList(data))
            }
        })
    }
}
