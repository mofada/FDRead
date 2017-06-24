package cn.mofada.fdread.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import cn.mofada.fdread.bean.Chapter
import cn.mofada.fdread.retrofit.RetrofitServices
import cn.mofada.fdread.utils.NotificationUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DownLoadService : Service() {
    var count: Int = 0
    var curr: Int = 0
    var isDwon: Boolean = false

    inner class FDBinder : Binder() {
        @Synchronized fun Download(chapters: List<Chapter>?, order: Boolean = true) {
            isOrder = order
            count = chapters?.size!!
            getBookByChapterId(chapters[0].chapterId)
        }
    }

    var binder: FDBinder = FDBinder()
    var isOrder: Boolean = false

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    @Synchronized private fun getBookByChapterId(chapterId: String) {
        RetrofitServices.getInstance().getRetrofitAndGson().chapter(chapterId).enqueue(object : Callback<Chapter> {
            override fun onFailure(call: Call<Chapter>?, t: Throwable?) {
                NotificationUtils.showNotification(this@DownLoadService, "缓存失败")
            }

            override fun onResponse(call: Call<Chapter>?, response: Response<Chapter>?) {
                curr++
                val chapter: Chapter = response?.body()!!
                chapter.saveOrUpdate("chapterId = '${chapter.chapterId}'")
                NotificationUtils.showNotification(this@DownLoadService, "正在缓存：$curr/$count ${chapter.title}")
                if (isOrder) {
                    if (!chapter.next.endsWith("/"))
                        getBookByChapterId(chapter.next)
                } else {
                    if (!chapter.previous.endsWith("/"))
                        getBookByChapterId(chapter.previous)
                }
            }
        })
    }
}
