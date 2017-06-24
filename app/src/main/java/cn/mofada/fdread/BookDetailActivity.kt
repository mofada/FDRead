package cn.mofada.fdread

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import cn.mofada.fdread.adapter.BookChapterAdapter
import cn.mofada.fdread.adapter.OnItemClickListener
import cn.mofada.fdread.base.Constant
import cn.mofada.fdread.bean.Book
import cn.mofada.fdread.bean.Chapter
import cn.mofada.fdread.dialog.LoadingDialog
import cn.mofada.fdread.gson.GsonBook
import cn.mofada.fdread.gson.Line
import cn.mofada.fdread.retrofit.RetrofitServices
import cn.mofada.fdread.service.DownLoadService
import cn.mofada.fdread.utils.PrefersUtils
import cn.mofada.fdread.utils.ToastUtils
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_book.*
import org.litepal.crud.DataSupport
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BookDetailActivity : AppCompatActivity() {

    var book: Book? = null
    var gsonBook: GsonBook? = null
    var adapter: BookChapterAdapter? = BookChapterAdapter(ArrayList<Chapter>())
    var dialog: LoadingDialog? = null
    var call: Call<GsonBook>? = null
    var isSave: Boolean = false
    var binder: DownLoadService.FDBinder? = null

    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {}

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            binder = service as DownLoadService.FDBinder
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        dialog = LoadingDialog(this, R.style.loadingDialog)

        book = intent.extras.getSerializable(BOOK) as Book

        appbar.addOnOffsetChangedListener { appBarLayout, offset ->
            val alpha: Float = offset * 1F / appBarLayout.totalScrollRange
            textView_title.alpha = Math.abs(alpha)
        }

        adapter?.listener(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                BookReadActivity.startActivity(this@BookDetailActivity, adapter?.data!![position])
            }
        })

        recycleView_book.adapter = adapter
        recycleView_book.layoutManager = LinearLayoutManager(this)
        recycleView_book.itemAnimator = DefaultItemAnimator()
        recycleView_book.addItemDecoration(DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL))

        book_add.setOnClickListener {
            if (isSave) {
                val books: List<Book> = DataSupport.where("bookId = '${book?.bookId}'").find(Book::class.java)
                if (TextUtils.isEmpty(books[0].currChapterId)) {
                    books[0].currChapterId = adapter?.data?.get(0)?.chapterId
                }
                BookReadActivity.startActivity(this, Chapter("", books[0].currChapterId!!, "", "", "", ""))
            } else {
                book?.currChapterId = adapter?.data?.get(0)?.chapterId
                book!!.saveOrUpdateAsync("bookId = '${book!!.bookId}'").listen {
                    ToastUtils.showToast(this, "添加成功")
                    isSave = isSaved()
                    book_add.text = "继续阅读"
                    val intent = Intent(Constant.ACTION_BOOKADD)
                    this.sendBroadcast(intent)
                    addBookTo()
                }
            }
        }

        book_download.setOnClickListener {
            ToastUtils.showToast(this, "开始缓存，请保持网络畅通")
            binder?.Download(adapter?.data, adapter?.isOrder!!)
            book_download.isEnabled = false
        }

        book_order.setOnClickListener {
            adapter!!.reversed()
        }

        isSave = isSaved()

        if (isSave) {
            book_add.text = "继续阅读"
        }

        refresh()

        val intent: Intent = Intent(this, DownLoadService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onResume() {
        super.onResume()
        adapter?.notifyDataSetChanged()
    }

    private fun checkSave() {
        val chapters: List<Chapter> = DataSupport.where("list = '${book?.bookId}'").find(Chapter::class.java)

        book_download.isEnabled = true

        if (chapters.size + 5 >= adapter?.data?.size!!) {
            book_download.isEnabled = false
            book_download.text = "已缓存"
        }
    }

    private fun addBookTo() {
        if (PrefersUtils.getBoolean(Constant.PREFERS_ISLOGIN)) {
            book?.uid = PrefersUtils.getString(Constant.PREFERS_UID)
            val json: String = getJsonByBook(book!!)

            RetrofitServices.getInstance().getRetrofitAndGson().book_insert(json).enqueue(object : Callback<Line> {
                override fun onResponse(call: Call<Line>?, response: Response<Line>?) {}

                override fun onFailure(call: Call<Line>?, t: Throwable?) {}
            })
        }
    }

    private fun isSaved(): Boolean {
        val books: List<Book> = DataSupport.where("bookId = '${book?.bookId}'").find(Book::class.java)
        if (books.isNotEmpty()) {
            return true
        }
        return false
    }

    private fun refresh() {
        dialog?.show()
        call = RetrofitServices.getInstance().getRetrofitAndGson().book(book?.bookId!!)
        call?.enqueue(object : Callback<GsonBook> {
            override fun onResponse(call: Call<GsonBook>?, response: Response<GsonBook>?) {
                gsonBook = response?.body()
                gsonBookToBook()
            }

            override fun onFailure(call: Call<GsonBook>?, t: Throwable?) {
                val books: List<Book> = DataSupport.where("bookId = '${book?.bookId}'").find(Book::class.java)
                val chapters: List<Chapter> = DataSupport.where("list = '${book?.bookId}'").find(Chapter::class.java)
                if (books.isNotEmpty()) {
                    book = books[0]
                    updateUI()
                }
                if (chapters.isNotEmpty()) {
                    adapter?.refresh(chapters)
                }
                ToastUtils.noIntent(this@BookDetailActivity)
                dialog?.dismiss()
            }
        })
    }

    private fun gsonBookToBook() {
        book?.bookId = gsonBook?.bookId!!
        book?.cover = gsonBook?.cover!!
        book?.title = gsonBook?.title!!
        book?.author = gsonBook?.author!!
        book?.type = gsonBook?.type!!
        book?.condition_ = gsonBook?.condition_!!
        book?.word = gsonBook?.word!!
        book?.time = gsonBook?.time!!
        book?.update_ = gsonBook?.update_!!
        book?.updateChapter = gsonBook?.updateChapter!!
        book?.intro = gsonBook?.intro!!

        updateChapter()
        updateUI()
    }

    private fun updateUI() {
        book_title.text = book?.title
        textView_title.text = book?.title
        Glide.with(this).load(book?.cover).into(book_cover)
        book_author_type.text = "${book?.author}/${book?.type}"
        book_condition_word.text = "${book?.condition_}/${book?.word}"
        book_update.text = book?.update_
        book_time.text = book?.time
        book_intro.text = book?.intro

        if (isSaved()) {
            book?.saveOrUpdateAsync("bookId = '${book!!.bookId}'")?.listen {
                updateBook()
            }
        }

        checkSave()

        book_add.isEnabled = true
        book_order.isEnabled = true

        dialog?.dismiss()
    }

    private fun updateChapter() {
        if (gsonBook?.lists?.size!! > 12) {
            gsonBook?.lists = gsonBook?.lists?.subList(12, gsonBook?.lists?.size!!)!!
        }
        adapter?.refresh(gsonBook?.lists!!)
    }

    private fun updateBook() {
        if (PrefersUtils.getBoolean(Constant.PREFERS_ISLOGIN)) {
            book?.uid = PrefersUtils.getString(Constant.PREFERS_UID)
            val json: String = getJsonByBook(book!!)

            RetrofitServices.getInstance().getRetrofitAndGson().book_update(json).enqueue(object : Callback<Line> {
                override fun onResponse(call: Call<Line>?, response: Response<Line>?) {}

                override fun onFailure(call: Call<Line>?, t: Throwable?) {}

            })
        }
    }

    companion object {
        private val BOOK: String = "book"
        fun startActivity(activity: Activity, book: Book) {
            val intent: Intent = Intent(activity, BookDetailActivity::class.java)
            intent.putExtra(BOOK, book)
            activity.startActivity(intent)
        }

        fun getJsonByBook(book: Book): String {
            val json: JsonObject = JsonObject()
            json.addProperty("bookId", book.bookId)
            json.addProperty("cover", book.cover)
            json.addProperty("title", book.title)
            json.addProperty("author", book.author)
            json.addProperty("type", book.type)
            json.addProperty("condition_", book.condition_)
            json.addProperty("word", book.word)
            json.addProperty("time", book.time)
            json.addProperty("update_", book.update_)
            json.addProperty("currChapter", book.currChapter)
            json.addProperty("currChapterId", book.currChapterId)
            json.addProperty("updateChapter", book.updateChapter)
            json.addProperty("intro", book.intro)
            json.addProperty("uid", book.uid)
            return json.toString()
        }
    }

    override fun onDestroy() {
        call?.cancel()
        unbindService(connection)
        super.onDestroy()
    }
}
