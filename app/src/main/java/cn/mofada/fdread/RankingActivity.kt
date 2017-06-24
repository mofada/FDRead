package cn.mofada.fdread

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import cn.mofada.fdread.adapter.BookRankingAdapter
import cn.mofada.fdread.adapter.OnItemClickListener
import cn.mofada.fdread.base.Constant
import cn.mofada.fdread.bean.Book
import cn.mofada.fdread.dialog.LoadingDialog
import cn.mofada.fdread.gson.Ranking
import cn.mofada.fdread.retrofit.RetrofitServices
import cn.mofada.fdread.utils.PrefersUtils
import cn.mofada.fdread.utils.ToastUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_ranking.*
import kotlinx.android.synthetic.main.layout_notice.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RankingActivity : AppCompatActivity() {
    var dialog: LoadingDialog? = null
    var adapter: BookRankingAdapter = BookRankingAdapter(ArrayList<Book>())
    var ranking: Ranking? = null
    var call: Call<Ranking>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)
        dialog = LoadingDialog(this, R.style.loadingDialog)

        tablayout.addTab(tablayout.newTab().setText("小说总榜"))
        tablayout.addTab(tablayout.newTab().setText("玄幻小说"))
        tablayout.addTab(tablayout.newTab().setText("修真小说"))
        tablayout.addTab(tablayout.newTab().setText("都市小说"))
        tablayout.addTab(tablayout.newTab().setText("穿越小说"))
        tablayout.addTab(tablayout.newTab().setText("网游小说"))
        tablayout.addTab(tablayout.newTab().setText("科幻小说"))
        tablayout.addTab(tablayout.newTab().setText("其他小说"))
        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                setContent(tab?.position!!)
            }

        })

        recycleView_ranking.adapter = adapter
        recycleView_ranking.layoutManager = LinearLayoutManager(this)
        recycleView_ranking.itemAnimator = DefaultItemAnimator()
        recycleView_ranking.addItemDecoration(DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL))
        adapter.listener(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val book = adapter.data[position]
                BookDetailActivity.startActivity(this@RankingActivity, book)
            }
        })

        marqueeView.startWithText(getString(R.string.intro_ranking))

        refresh()
    }

    fun setContent(position: Int) {
        adapter.refresh(ranking?.ranking?.get(position)?.lists!!)
    }

    private fun refresh() {
        dialog?.show()
        call = RetrofitServices.getInstance().getRetrofitAndGson().ranking()
        call?.enqueue(object : Callback<Ranking> {
            override fun onFailure(call: Call<Ranking>?, t: Throwable?) {
                val json = PrefersUtils.getString(Constant.PREFERS_RANKING)
                if (!TextUtils.isEmpty(json)) {
                    ranking = Gson().fromJson(json, Ranking::class.java)
                    setContent(0)
                }
                ToastUtils.noIntent(this@RankingActivity)
                dialog?.dismiss()
            }

            override fun onResponse(call: Call<Ranking>?, response: Response<Ranking>?) {
                ranking = response?.body()
                PrefersUtils.putString(Constant.PREFERS_RANKING, Gson().toJson(ranking))
                setContent(0)
                dialog?.dismiss()
            }

        })
    }

    override fun onDestroy() {
        call?.cancel()
        super.onDestroy()
    }

    companion object {
        fun startActivity(activity: Activity) {
            val intent: Intent = Intent(activity, RankingActivity::class.java)
            activity.startActivity(intent)
        }
    }
}
