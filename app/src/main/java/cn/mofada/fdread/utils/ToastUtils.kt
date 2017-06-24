package cn.mofada.fdread.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast

/**
 * Created by fada on 2017/6/16.
 */
object ToastUtils {
    fun showToast(activity: Activity, content: String) {
        Toast.makeText(activity, content, Toast.LENGTH_SHORT).show()
    }

    fun showToast(context: Context, content: String) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
    }

    fun noIntent(activity: Activity) {
        Toast.makeText(activity, "似乎没有网络哦", Toast.LENGTH_SHORT).show()
    }
}