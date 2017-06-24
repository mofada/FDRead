package cn.mofada.fdread

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import cn.mofada.fdread.bean.Chapter
import cn.mofada.fdread.retrofit.RetrofitServices
import cn.mofada.fdread.utils.LogUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DownLoadService : Service() {
    inner class FDBinder : Binder() {
        fun Download(chapter: Chapter?, order: Boolean = true) {
            isOrder = order
            if (chapter != null) {
                getBookByChapterId(chapter.chapterId)
            }
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
                LogUtils.d("onFailure" + t?.message)
            }

            override fun onResponse(call: Call<Chapter>?, response: Response<Chapter>?) {
                val chapter: Chapter = response?.body()!!
                LogUtils.d("onResponse:" + chapter.toString())
                chapter.saveOrUpdateAsync("chapterId = ${chapter.chapterId}")
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
