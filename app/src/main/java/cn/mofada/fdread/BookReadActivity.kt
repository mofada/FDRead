package cn.mofada.fdread

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import cn.mofada.fdread.base.Constant
import cn.mofada.fdread.bean.Book
import cn.mofada.fdread.bean.Chapter
import cn.mofada.fdread.dialog.LoadingDialog
import cn.mofada.fdread.gson.Line
import cn.mofada.fdread.retrofit.RetrofitServices
import cn.mofada.fdread.utils.DisplayUtil
import cn.mofada.fdread.utils.PrefersUtils
import cn.mofada.fdread.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_book_read.*
import org.litepal.crud.DataSupport
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BookReadActivity : AppCompatActivity() {
    var isShowActionBar: Boolean = false
    var chapter: Chapter? = null
    var dialog: LoadingDialog? = null
    var call: Call<Chapter>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_read)

        dialog = LoadingDialog(this, R.style.loadingDialog)

        chapter = intent.getSerializableExtra(CHAPTER) as Chapter?

        nestedScroll.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_MOVE -> isShowActionBar = true
                MotionEvent.ACTION_UP -> onDown(motionEvent)
            }
            super.onTouchEvent(motionEvent)
        }

        setActionVisibility()

        refresh(chapter?.chapterId!!)

        /**
         * 设置字体 -
         */
        chapter_text_size_reduce.setOnClickListener {
            textSize(-1F)
        }

        /**
         * 设置字体 +
         */
        chapter_text_size_plus.setOnClickListener {
            textSize(1F)
        }

        /**
         * 设置背景
         */
        chapter_color_bg_1.setOnClickListener {
            setBackground(R.color.read_theme_white)
        }

        chapter_color_bg_2.setOnClickListener {
            setBackground(R.color.read_theme_yellow)
        }

        chapter_color_bg_3.setOnClickListener {
            setBackground(R.color.read_theme_green)
        }

        chapter_color_bg_4.setOnClickListener {
            setBackground(R.color.read_theme_gray)
        }

        chapter_color_bg_5.setOnClickListener {
            setBackground(R.color.read_theme_five)
        }

        chapter_color_bg_night.setOnClickListener {
            setBackground(R.color.read_theme_night)
        }

        chapter_previous.setOnClickListener {
            changeChapter(chapter?.previous!!, false)
        }

        chapter_bottom_previous.setOnClickListener {
            changeChapter(chapter?.previous!!, false)
        }

        chapter_next.setOnClickListener {
            changeChapter(chapter?.next!!, true)
        }

        chapter_bottom_next.setOnClickListener {
            changeChapter(chapter?.next!!, true)
        }

        chapter_list.setOnClickListener {
            BookDetailActivity.startActivity(this, Book(
                    chapter?.list!!, "", "", "", "", "", "", "", "", chapter?.title, chapter?.chapterId!!, "", ""
            ))
            finish()
        }

        chapter_bottom_list.setOnClickListener {
            BookDetailActivity.startActivity(this, Book(
                    chapter?.list!!, "", "", "", "", "", "", "", "", chapter?.title, chapter?.chapterId!!, "", ""
            ))
            finish()
        }

        setBackground(PrefersUtils.getInt(Constant.READ_BACKGROUND))
        chapter_content.textSize = PrefersUtils.getFloat(Constant.READ_TEXT_SIZE)
    }

    fun changeChapter(chapterId: String, isNext: Boolean) {
        if (chapterId.endsWith("/")) {
            if (isNext) {
                chapter_next.isEnabled = false
                chapter_bottom_next.isEnabled = false
            } else {
                chapter_previous.isEnabled = false
                chapter_bottom_previous.isEnabled = false
            }
        } else {
            chapter_previous.isEnabled = true
            chapter_next.isEnabled = true
            chapter_bottom_next.isEnabled = true
            chapter_bottom_previous.isEnabled = true
            refresh(chapterId)
        }
    }

    fun setBackground(colorId: Int) {
        PrefersUtils.putInt(Constant.READ_BACKGROUND, colorId)
        layout.setBackgroundResource(colorId)
    }

    /**
     * @size 12-25
     */
    fun textSize(size: Float) {
        val spVal: Float = DisplayUtil.px2sp(this, chapter_content.textSize) + size
        if (spVal < 12) {
            chapter_text_size_reduce.isEnabled = false
            return
        } else if (spVal > 26) {
            chapter_text_size_plus.isEnabled = false
            return
        } else {
            chapter_text_size_reduce.isEnabled = true
            chapter_text_size_plus.isEnabled = true
        }
        PrefersUtils.putFloat(Constant.READ_TEXT_SIZE, spVal)
        chapter_content.textSize = spVal
    }

    private fun refresh(chapterId: String) {
        dialog?.show()
        val chapters: List<Chapter> = DataSupport.where("chapterId = '$chapterId'").find(Chapter::class.java)
        if (chapters.isNotEmpty()) {
            chapter = chapters[0]
            if (!chapter?.next?.endsWith("/")!! && !chapter?.previous?.endsWith("/")!!) {
                onFinish()
                return
            }
        }
        call = RetrofitServices.getInstance().getRetrofitAndGson().chapter(chapterId)
        call?.enqueue(object : Callback<Chapter> {
            override fun onResponse(call: Call<Chapter>?, response: Response<Chapter>?) {
                chapter = response?.body()
                val books: List<Book> = DataSupport.where("bookId = '${chapter?.list}'").find(Book::class.java)
                if (books.isNotEmpty()) {
                    chapter?.saveOrUpdate("chapterId = '$chapterId'")
                }
                onFinish()
            }

            override fun onFailure(call: Call<Chapter>?, t: Throwable?) {
                val chapters: List<Chapter> = DataSupport.where("chapterId = '$chapterId'").find(Chapter::class.java)
                if (chapters.isNotEmpty()) {
                    chapter = chapters[0]
                    onFinish()
                }
                ToastUtils.noIntent(this@BookReadActivity)

                dialog?.dismiss()
            }
        })
    }

    private fun onFinish() {
        chapter_content.text = chapter?.content
        chapter_title.text = chapter?.title
        toolbar_title.text = chapter?.title
        bottom_actionBar.visibility = View.VISIBLE
        nestedScroll.scrollTo(0, 0)
        dialog?.dismiss()
        updateCurr()
    }

    private fun updateCurr() {
        val books: List<Book> = DataSupport.where("bookId = '${chapter?.list}'").find(Book::class.java)
        if (books.isNotEmpty()) {
            books[0].currChapter = chapter?.title
            books[0].currChapterId = chapter?.chapterId!!
            books[0].saveOrUpdateAsync("bookId = '${chapter?.list}'").listen {
                updateBook(books[0])
                val intent = Intent(Constant.ACTION_BOOKADD)
                this.sendBroadcast(intent)
            }
        }
    }

    private fun updateBook(book: Book) {
        if (PrefersUtils.getBoolean(Constant.PREFERS_ISLOGIN)) {
            book.uid = PrefersUtils.getString(Constant.PREFERS_UID)
            val json: String = BookDetailActivity.getJsonByBook(book)

            RetrofitServices.getInstance().getRetrofitAndGson().book_update(json).enqueue(object : Callback<Line> {
                override fun onResponse(call: Call<Line>?, response: Response<Line>?) {}

                override fun onFailure(call: Call<Line>?, t: Throwable?) {}
            })
        }
    }

    fun setActionVisibility() {
        if (isShowActionBar) {
            //显示状态栏， Activity不全屏显示(恢复到有状态的正常情况)。
            layout.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            toolbar.visibility = View.VISIBLE
            bottombar.visibility = View.VISIBLE
        } else {
            //Activity全屏显示，且状态栏被隐藏覆盖掉
            layout.systemUiVisibility = View.INVISIBLE
            toolbar.visibility = View.GONE
            bottombar.visibility = View.GONE
        }
    }

    fun onDown(motionEvent: MotionEvent) {
        val proportion: Float = motionEvent.y / windowManager.defaultDisplay.height
        if (proportion > .3 && proportion < .7) {
            isShowActionBar = !isShowActionBar
            setActionVisibility()
        }
    }

    override fun onDestroy() {
        call?.cancel()
        super.onDestroy()
    }

    companion object {
        val CHAPTER: String = "chapter"
        fun startActivity(activity: Activity, chapter: Chapter) {
            val intent: Intent = Intent(activity, BookReadActivity::class.java)
            intent.putExtra(CHAPTER, chapter)
            activity.startActivity(intent)
        }
    }
}
