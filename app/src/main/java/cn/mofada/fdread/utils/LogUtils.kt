package cn.mofada.fdread.utils

import android.util.Log

/**
 * Created by fada on 2017/6/15.
 */
class LogUtils {
    companion object {
        private var TAG: String = "FDRead"
        private val ISDEBUG: Boolean = false

        fun d(content: String) {
            if (ISDEBUG)
                Log.d(TAG, content)
        }
    }
}