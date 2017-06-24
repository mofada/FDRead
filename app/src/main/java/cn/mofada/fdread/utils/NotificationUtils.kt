package cn.mofada.fdread.utils

import android.app.NotificationManager
import android.content.Context
import android.support.v7.app.NotificationCompat
import cn.mofada.fdread.R
import cn.mofada.fdread.base.Constant

/**
 * Created by fada on 2017/6/21.
 */
object NotificationUtils {
    fun showNotification(context: Context, message: String) {
        val builder = NotificationCompat.Builder(context)
        builder.setContentText(message)
        builder.setContentTitle("发达阅读")
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setAutoCancel(true)
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(Constant.NOTIFICATION_ID, builder.build())
    }
}