package cn.mofada.fdread.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.mofada.fdread.BookDetailActivity
import cn.mofada.fdread.ChannelActivity
import cn.mofada.fdread.R
import cn.mofada.fdread.RankingActivity
import cn.mofada.fdread.adapter.BookChannelAdapter
import cn.mofada.fdread.adapter.BookHorizontalAdapter
import cn.mofada.fdread.adapter.BookSearchAdapter
import cn.mofada.fdread.adapter.OnItemClickListener
import cn.mofada.fdread.base.Constant
import cn.mofada.fdread.bean.Book
import cn.mofada.fdread.dialog.LoadingDialog
import cn.mofada.fdread.gson.Index
import cn.mofada.fdread.gson.Line
import cn.mofada.fdread.gson.Search
import cn.mofada.fdread.retrofit.RetrofitServices
import cn.mofada.fdread.utils.PrefersUtils
import cn.mofada.fdread.utils.ToastUtils
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_book_store.*
import kotlinx.android.synthetic.main.layout_notice.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class BookStoreFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_book_store, container, false)
    }

    private var bookAdapter: BookHorizontalAdapter = BookHorizontalAdapter(ArrayList<Book>())
    private var bookSearchAdapter: BookSearchAdapter = BookSearchAdapter(ArrayList<Book>())
    private var channelAdapter: BookChannelAdapter? = null
    private var dialog: LoadingDialog? = null

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        channelAdapter = BookChannelAdapter(context)
        dialog = LoadingDialog(activity, R.style.loadingDialog)

        bookAdapter.listener(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val book = bookAdapter.data[position]
                BookDetailActivity.startActivity(activity, book)
            }
        })

        channelAdapter!!.listener(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val channel = channelAdapter!!.channels[position]
                if (position == 0) {
                    RankingActivity.startActivity(activity)
                } else {
                    ChannelActivity.startActivity(activity, channel.title, channel.descriptor, channel.type)
                }
            }
        })

        bookSearchAdapter.listener(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val book = bookSearchAdapter.data[position]
                BookDetailActivity.startActivity(activity, book)
            }
        })

        recycleView_recommend.adapter = bookAdapter
        recycleView_recommend.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycleView_recommend.itemAnimator = DefaultItemAnimator() as RecyclerView.ItemAnimator?

        recycleView_channel.adapter = channelAdapter
        recycleView_channel.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recycleView_channel.itemAnimator = DefaultItemAnimator()


        swipeRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipeRefresh.setOnRefreshListener {
            refresh()
        }

        toolbar_user.setOnClickListener {
            activity.drawLayout.openDrawer(GravityCompat.START)
        }

        toolbar_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    search(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        toolbar_search.setOnCloseListener {
            recycleView_recommend.visibility = View.VISIBLE
            recycleView_channel.adapter = channelAdapter
            channelAdapter!!.notifyDataSetChanged()
            false
        }

        refresh()

        notification()
    }

    private fun notification() {
        RetrofitServices.getInstance().getRetrofitAndGson().notification().enqueue(object : Callback<Line> {
            override fun onFailure(call: Call<Line>?, t: Throwable?) {
                val json = PrefersUtils.getString(Constant.PREFERS_NOTIFICATION)
                if (!TextUtils.isEmpty(json)) {
                    val index: Line? = Gson().fromJson(json, Line::class.java)
                    marqueeView.startWithList(index?.line?.split("/"))
                }else{
                    val hello: String=getString(R.string.hello_fdread)
                    marqueeView.startWithList(hello.split("/"))
                }
            }

            override fun onResponse(call: Call<Line>?, response: Response<Line>?) {
                val index: Line? = response?.body()
                PrefersUtils.putString(Constant.PREFERS_NOTIFICATION, Gson().toJson(index))
                marqueeView.startWithList(index?.line?.split("/"))
            }
        })
    }

    fun search(query: String) {
        if (TextUtils.isEmpty(query)) {
            return
        }
        dialog?.show()
        RetrofitServices.getInstance().getRetrofitAndGson().search(query).enqueue(object : Callback<Search> {
            override fun onFailure(call: Call<Search>?, t: Throwable?) {
                dialog?.dismiss()
                toolbar_search.clearFocus()
            }

            override fun onResponse(call: Call<Search>?, response: Response<Search>?) {
                val search = response?.body()
                onSearchSuccess(search)
            }
        })
    }

    private fun onSearchSuccess(search: Search?) {
        recycleView_recommend.visibility = View.GONE
        recycleView_channel.adapter = bookSearchAdapter
        bookSearchAdapter.refresh(search?.lists!!)
        toolbar_search.clearFocus()
        dialog?.dismiss()
    }

    private fun refresh() {
        val call = RetrofitServices.getInstance().getRetrofitAndGson().index()
        call.enqueue(object : Callback<Index> {
            override fun onResponse(call: Call<Index>?, response: Response<Index>?) {
                val index: Index? = response?.body()
                bookAdapter.refresh(index?.recommend!!)
                PrefersUtils.putString(Constant.PREFERS_STORE, Gson().toJson(index))
                swipeRefresh.isRefreshing = false
            }

            override fun onFailure(call: Call<Index>?, t: Throwable?) {
                val json = PrefersUtils.getString(Constant.PREFERS_STORE)
                if (!TextUtils.isEmpty(json)) {
                    val index: Index? = Gson().fromJson(json, Index::class.java)
                    bookAdapter.refresh(index?.recommend!!)
                }
                ToastUtils.noIntent(activity)
                swipeRefresh.isRefreshing = false
            }
        })

    }

    fun loginIn(intent: Intent) {
        Glide.with(activity).load(intent.getStringExtra(Constant.USER_ICONURL)).into(toolbar_user)
    }

    fun loginOut() {
//        toolbar_user.setImageResource(R.mipmap.png_user)
        Glide.with(this).load(R.mipmap.png_user).into(toolbar_user)
    }
}
