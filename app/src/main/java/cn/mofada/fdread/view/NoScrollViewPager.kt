package cn.mofada.fdread.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by fada on 2017/6/12.
 */
class NoScrollViewPager(context: Context?, attrs: AttributeSet?) : ViewPager(context, attrs) {
    override fun onTouchEvent(ev: MotionEvent?): Boolean = false

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean = false
}