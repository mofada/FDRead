package cn.mofada.fdread

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import cn.mofada.fdread.adapter.BookAdapter
import cn.mofada.fdread.base.Constant
import cn.mofada.fdread.bean.Book
import cn.mofada.fdread.dialog.LoadingDialog
import cn.mofada.fdread.gson.Line
import cn.mofada.fdread.retrofit.RetrofitServices
import cn.mofada.fdread.utils.LogUtils
import cn.mofada.fdread.utils.PrefersUtils
import com.bumptech.glide.Glide
import com.mcxtzhang.swipemenulib.SwipeMenuLayout
import com.umeng.socialize.UMAuthListener
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA
import kotlinx.android.synthetic.main.activity_user.*
import org.litepal.crud.DataSupport
import org.litepal.crud.callback.FindMultiCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserActivity : AppCompatActivity() {
    var isLogin: Boolean = false
    var dialog: LoadingDialog? = null
    var adapter: BookAdapter = BookAdapter(ArrayList<Book>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        dialog = LoadingDialog(this, R.style.loadingDialog)
        adapter.listener(object : BookAdapter.OnClickListener {
            override fun onRemoveClick(view: View, position: Int) {
                adapter.remove(position)
            }

            override fun onItemClick(view: View, position: Int) {
                val book = adapter.data[position]
                BookDetailActivity.startActivity(this@UserActivity, book)
            }
        })

        appbar.addOnOffsetChangedListener { appBarLayout, offset ->
            val alpha: Float = offset * 1F / appBarLayout.totalScrollRange
            toolbar_title.alpha = Math.abs(alpha)
        }

        recycleView_user.adapter = adapter
        recycleView_user.layoutManager = GridLayoutManager(this, 3)
        recycleView_user.itemAnimator = DefaultItemAnimator()
        recycleView_user.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val viewCache = SwipeMenuLayout.getViewCache()
                viewCache?.smoothClose()
            }
            false
        }

        toolbar_login.setOnClickListener {
            if (isLogin) {
                loginOut()
            } else {
                loginIn()
            }
        }

        toolbar_user.setOnClickListener {
            finish()
        }

        isLogin = UMShareAPI.get(this).isAuthorize(this, SHARE_MEDIA.QQ)

        checkLogin()

        showUser()

        getBook()
    }

    fun getBook() {
        DataSupport.findAllAsync(Book::class.java).listen(object : FindMultiCallback {
            override fun <T> onFinish(t: List<T>?) {
                val data: List<Book>? = t as ArrayList<Book>
                adapter.refresh(ArrayList(data))
            }
        })
    }

    fun showUser() {
        if (PrefersUtils.getBoolean(Constant.PREFERS_ISLOGIN)) {
            user_name.text = PrefersUtils.getString(Constant.PREFERS_NAME)
            Glide.with(this).load(PrefersUtils.getString(Constant.PREFERS_ICONURL)).into(user_iconurl)
            Glide.with(this).load(PrefersUtils.getString(Constant.PREFERS_ICONURL)).into(toolbar_user)
        }
    }

    private fun checkLogin() {
        if (isLogin) {
            toolbar_login.text = "注销"
        } else {
            toolbar_login.text = "登录"
        }
        PrefersUtils.putBoolean(Constant.PREFERS_ISLOGIN, isLogin)
    }

    fun loginIn() {
        UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, authListener)
    }

    fun loginOut() {
        isLogin = false
        checkLogin()
        UMShareAPI.get(this).deleteOauth(this, SHARE_MEDIA.QQ, authListener)
        val intent: Intent = Intent(Constant.ACTION_LOGINOUT)
        this.sendBroadcast(intent)
        user_name.text = "your name"
        user_iconurl.setImageResource(R.mipmap.png_user)
        toolbar_user.setImageResource(R.mipmap.png_user)

        PrefersUtils.remove(Constant.PREFERS_NAME)
        PrefersUtils.remove(Constant.PREFERS_ICONURL)
        PrefersUtils.remove(Constant.PREFERS_UID)
    }

    var authListener: UMAuthListener = object : UMAuthListener {
        override fun onStart(platform: SHARE_MEDIA) {
            dialog?.show()
        }

        override fun onError(platform: SHARE_MEDIA, action: Int, t: Throwable) {
            dialog?.dismiss()
            LogUtils.d("onError" + t.message)
            Toast.makeText(this@UserActivity, "目前只支持QQ登录，请安装QQ客户端后重试", Toast.LENGTH_LONG).show()
        }

        override fun onCancel(platform: SHARE_MEDIA, action: Int) {
            dialog?.dismiss()
            Toast.makeText(this@UserActivity, "您取消了登录", Toast.LENGTH_LONG).show()
        }

        override fun onComplete(platform: SHARE_MEDIA, action: Int, data: Map<String, String>?) {
            dialog?.dismiss()
            if (data == null) return
            onFinish(data)
        }
    }

    private fun onFinish(data: Map<String, String>) {
        isLogin = true
        checkLogin()

        user_name.text = data["name"]
        Glide.with(this).load(data["iconurl"]).into(user_iconurl)
        Glide.with(this).load(data["iconurl"]).into(toolbar_user)

        val intent: Intent = Intent(Constant.ACTION_LOGININ)
        intent.putExtra(Constant.USER_NAME, data["name"])
        intent.putExtra(Constant.USER_ICONURL, data["iconurl"])
        this.sendBroadcast(intent)
        PrefersUtils.putString(Constant.PREFERS_NAME, data["name"])
        PrefersUtils.putString(Constant.PREFERS_ICONURL, data["iconurl"])
        PrefersUtils.putString(Constant.PREFERS_UID, data["uid"])

        getBookByUid(data["uid"]!!)

        DataSupport.findAllAsync(Book::class.java).listen(object : FindMultiCallback {
            override fun <T> onFinish(t: List<T>?) {
                val books: List<Book> = t as List<Book>
                for (book in books) {
                    uploadBooks(book)
                }
            }
        })

    }

    private fun uploadBooks(book: Book) {
        val uid = PrefersUtils.getString(Constant.PREFERS_UID)
        book.uid = uid
        val json = BookDetailActivity.getJsonByBook(book)
        RetrofitServices.getInstance().getRetrofitAndGson().book_insert(json).enqueue(object : Callback<Line> {
            override fun onFailure(call: Call<Line>?, t: Throwable?) {
                LogUtils.d("User book_insert onFailure" + t?.message)
            }

            override fun onResponse(call: Call<Line>?, response: Response<Line>?) {
                LogUtils.d("user book_insert onResponse" + response?.message())

            }
        })
    }

    private fun getBookByUid(uid: String) {
        LogUtils.d("getBookByUid:" + uid)

        RetrofitServices.getInstance().getRetrofitAndGson().book_query(uid).enqueue(object : Callback<List<Book>> {
            override fun onFailure(call: Call<List<Book>>?, t: Throwable?) {
                LogUtils.d("user book_query onFailure" + t?.message)

            }

            override fun onResponse(call: Call<List<Book>>?, response: Response<List<Book>>?) {
                LogUtils.d("user book_query onResponse" + response?.message())
                val books: List<Book> = response?.body()!!
                for (book in books) {
                    book.saveOrUpdate("bookId = '${book.bookId}'")
                }
                getBook()
                val intent: Intent = Intent(Constant.ACTION_BOOKADD)
                this@UserActivity.sendBroadcast(intent)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        UMShareAPI.get(this).release()
    }

    companion object {
        fun startActivity(activity: Activity) {
            val intent: Intent = Intent(activity, UserActivity::class.java)
            activity.startActivity(intent)
        }
    }
}
