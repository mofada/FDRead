package cn.mofada.fdread.view

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration


/**
 * Created by fada on 2017/6/13.
 */
class VerticalSwipeRefreshLayout(context: Context?, attrs: AttributeSet?) : SwipeRefreshLayout(context, attrs) {

    private val mTouchSlop: Int
    // 上一次触摸时的X坐标
    private var mPrevX: Float = 0.toFloat()

    init {

        // 触发移动事件的最短距离，如果小于这个距离就不触发移动控件
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> mPrevX = event.x

            MotionEvent.ACTION_MOVE -> {
                val eventX = event.x
                val xDiff = Math.abs(eventX - mPrevX)
                // Log.d("refresh" ,"move----" + eventX + "   " + mPrevX + "   " + mTouchSlop);
                // 增加60的容差，让下拉刷新在竖直滑动时就可以触发
                if (xDiff > mTouchSlop + 60) {
                    return false
                }
            }
        }

        return super.onInterceptTouchEvent(event)
    }
}