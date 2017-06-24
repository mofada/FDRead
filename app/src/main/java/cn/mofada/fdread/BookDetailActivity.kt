package cn.mofada.fdread

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import cn.mofada.fdread.adapter.BookChapterAdapter
import cn.mofada.fdread.bean.Chapter
import kotlinx.android.synthetic.main.activity_book.*


class BookActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        appbar.addOnOffsetChangedListener { appBarLayout, offset ->
            val alpha: Float = offset * 1F / appBarLayout.totalScrollRange
            textView_title.alpha = Math.abs(alpha)
        }

        recycleView_book.adapter = BookChapterAdapter(getChapter())
        recycleView_book.layoutManager = LinearLayoutManager(this)
        recycleView_book.itemAnimator = DefaultItemAnimator()
        recycleView_book.addItemDecoration(DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL))
    }

    fun getChapter(): List<Chapter> {
        val chapters: ArrayList<Chapter> = ArrayList<Chapter>()
        chapters.add(Chapter("第845章 震撼蛮荒", ""))
        chapters.add(Chapter("第844章 不死骨爆发", ""))
        chapters.add(Chapter("第843章 天尊血发", ""))
        chapters.add(Chapter("第842章 我也不是那么优秀……", ""))
        chapters.add(Chapter("第841章 师尊，救命", ""))
        chapters.add(Chapter("第840章 那根血发！", ""))
        chapters.add(Chapter("第839章 绝世之战", ""))
        chapters.add(Chapter("第838章 你凭什么不认可！", ""))
        chapters.add(Chapter("第837章 天尊降临", ""))
        chapters.add(Chapter("第836章 天地道法！", ""))
        chapters.add(Chapter("第835章 因果", ""))
        chapters.add(Chapter("第834章 冥皇降临！", ""))
        return chapters
    }

    companion object {
        fun startActivity(activity: Activity) {
            val intent: Intent = Intent(activity, BookActivity::class.java)
            activity.startActivity(intent)
        }
    }
}
