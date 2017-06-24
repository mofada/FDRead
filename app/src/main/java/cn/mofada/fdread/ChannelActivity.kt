package cn.mofada.fdread

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import cn.mofada.fdread.adapter.BookChannelListAdapter
import cn.mofada.fdread.adapter.BookHorizontalAdapter
import cn.mofada.fdread.adapter.OnItemClickListener
import cn.mofada.fdread.base.Constant
import cn.mofada.fdread.bean.Book
import cn.mofada.fdread.dialog.LoadingDialog
import cn.mofada.fdread.gson.Channel
import cn.mofada.fdread.retrofit.RetrofitServices
import cn.mofada.fdread.utils.PrefersUtils
import cn.mofada.fdread.utils.ToastUtils
import com.google.gson.Gson
import com.umeng.socialize.utils.DeviceConfig.context
import kotlinx.android.synthetic.main.fragment_book_store.*
import kotlinx.android.synthetic.main.layout_notice.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChannelActivity : AppCompatActivity() {
    private var bookAdapter: BookHorizontalAdapter = BookHorizontalAdapter(ArrayList<Book>())
    private var channelAdapter: BookChannelListAdapter = BookChannelListAdapter(ArrayList<Book>())
    private var dialog: LoadingDialog? = null
    private var channel: Channel? = null
    private var title: String = ""
    private var descriptor: String = ""
    private var type: Int = 0
    var call: Call<Channel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_book_store)

        title = intent.getStringExtra(TITLE)
        descriptor = intent.getStringExtra(DESCRIPTOR)
        type = intent.getIntExtra(TYPE, 1)

        dialog = LoadingDialog(this, R.style.loadingDialog)

        bookAdapter.listener(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val book = bookAdapter.data[position]
                BookDetailActivity.startActivity(this@ChannelActivity, book)
            }
        })

        channelAdapter.listener(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val book = channelAdapter.data[position]
                BookDetailActivity.startActivity(this@ChannelActivity, book)
            }
        })

        recycleView_recommend.adapter = bookAdapter
        recycleView_recommend.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycleView_recommend.itemAnimator = DefaultItemAnimator()

        recycleView_channel.adapter = channelAdapter
        recycleView_channel.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycleView_channel.itemAnimator = DefaultItemAnimator()
        recycleView_channel.addItemDecoration(DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL))


        swipeRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipeRefresh.setOnRefreshListener {
            refresh()
        }

        toolbar_user.visibility = View.GONE
        toolbar_search.visibility = View.GONE
        toolbar_title.text = title
        marqueeView.startWithText(descriptor)

        refresh()
    }

    private fun refresh() {
        dialog?.show()
        call = RetrofitServices.getInstance().getRetrofitAndGson().channel(type)
        call?.enqueue(object : Callback<Channel> {
            override fun onResponse(call: Call<Channel>?, response: Response<Channel>?) {
                channel = response?.body()
                bookAdapter.refresh(channel?.recommend!!)
                channelAdapter.refresh(channel?.list!!)
                PrefersUtils.putString(Constant.getChannel(type), Gson().toJson(channel))
                swipeRefresh.isRefreshing = false
                dialog?.dismiss()
            }

            override fun onFailure(call: Call<Channel>?, t: Throwable?) {
                val json = PrefersUtils.getString(Constant.getChannel(type))
                if (!TextUtils.isEmpty(json)) {
                    channel = Gson().fromJson(json, Channel::class.java)
                    bookAdapter.refresh(channel?.recommend!!)
                    channelAdapter.refresh(channel?.list!!)
                }
                ToastUtils.noIntent(this@ChannelActivity)
                swipeRefresh.isRefreshing = false
                dialog?.dismiss()
            }
        })
    }

    override fun onDestroy() {
        call?.cancel()
        super.onDestroy()
    }

    companion object {
        private var TITLE: String = "title"
        private var TYPE: String = "type"
        private var DESCRIPTOR: String = "descriptor"
        fun startActivity(activity: Activity, title: String, descriptor: String, type: Int) {
            val intent: Intent = Intent(activity, ChannelActivity::class.java)
            intent.putExtra(TITLE, title)
            intent.putExtra(DESCRIPTOR, descriptor)
            intent.putExtra(TYPE, type)
            activity.startActivity(intent)
        }
    }
}
