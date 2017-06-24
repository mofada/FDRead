package cn.mofada.fdread.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.mofada.fdread.R
import cn.mofada.fdread.UserActivity
import cn.mofada.fdread.adapter.BookDiscussAdapter
import cn.mofada.fdread.base.Constant
import cn.mofada.fdread.bean.Discuss
import cn.mofada.fdread.dialog.DiscussDialog
import cn.mofada.fdread.dialog.LoadingDialog
import cn.mofada.fdread.gson.Line
import cn.mofada.fdread.retrofit.RetrofitServices
import cn.mofada.fdread.utils.PrefersUtils
import cn.mofada.fdread.utils.ToastUtils
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_input.*
import kotlinx.android.synthetic.main.fragment_book_discuss.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class BookDiscussFragment : Fragment() {
    var dialog: DiscussDialog? = null
    var loading: LoadingDialog? = null
    var adapter: BookDiscussAdapter = BookDiscussAdapter(ArrayList<Discuss>())

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_book_discuss, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_title.text = "讨论"
        toolbar_search.visibility = View.INVISIBLE

        dialog = DiscussDialog(activity, R.style.loadingDialog)
        loading = LoadingDialog(activity, R.style.loadingDialog)

        swipeRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipeRefresh.setOnRefreshListener {
            getDiscuss()
        }

        recycleView_discuss.adapter = adapter
        recycleView_discuss.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recycleView_discuss.itemAnimator = DefaultItemAnimator()

        toolbar_user.setOnClickListener {
            activity.drawLayout.openDrawer(GravityCompat.START)
        }

        floatButton.setOnClickListener {
            if (PrefersUtils.getBoolean(Constant.PREFERS_ISLOGIN)) {
                dialog!!.show()
            } else {
                ToastUtils.showToast(activity, "只有登录才能讨论哦")
                UserActivity.startActivity(activity)
            }
        }

        dialog!!.listener = object : DiscussDialog.onClick {
            override fun onClick(v: View) {
                val text = dialog!!.dialog_input.text.toString()
                if (TextUtils.isEmpty(text)) {
                    ToastUtils.showToast(activity, "不能发表空的消息")
                } else {
                    submit(text)
                }
            }
        }

        getDiscuss()
    }

    fun submit(text: String) {
        if (!PrefersUtils.getBoolean(Constant.PREFERS_ISLOGIN)) return
        val name: String = PrefersUtils.getString(Constant.PREFERS_NAME)
        val uid: String = PrefersUtils.getString(Constant.PREFERS_UID)
        val iconurl: String = PrefersUtils.getString(Constant.PREFERS_ICONURL)
        val discuss: Discuss = Discuss(uid, iconurl, name, text)
        val message = Gson().toJson(discuss)

        RetrofitServices.getInstance().getRetrofitAndGson().discuss_insert(message).enqueue(object : Callback<Line> {
            override fun onFailure(call: Call<Line>?, t: Throwable?) {
                ToastUtils.noIntent(activity)
            }

            override fun onResponse(call: Call<Line>?, response: Response<Line>?) {
                getDiscuss()
            }
        })
    }

    fun getDiscuss() {
        RetrofitServices.getInstance().getRetrofitAndGson().discuss_query().enqueue(object : Callback<List<Discuss>> {
            override fun onResponse(call: Call<List<Discuss>>?, response: Response<List<Discuss>>?) {
                val discuss: List<Discuss> = response?.body()!!
                adapter.refresh(discuss)
                PrefersUtils.putString(Constant.PREFERS_DISCUSS, Gson().toJson(discuss))
                swipeRefresh.isRefreshing = false
            }

            override fun onFailure(call: Call<List<Discuss>>?, t: Throwable?) {
                val json = PrefersUtils.getString(Constant.PREFERS_DISCUSS)
                if (!TextUtils.isEmpty(json)) {
                    val discuss: List<Discuss> = Gson().fromJson(json, object : TypeToken<List<Discuss>>() {
                    }.type)
                    adapter.refresh(discuss)
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
        Glide.with(this).load(R.mipmap.png_user).into(toolbar_user)
//        toolbar_user.setImageResource(R.mipmap.png_user)
    }
}
