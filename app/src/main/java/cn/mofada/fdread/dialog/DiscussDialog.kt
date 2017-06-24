package cn.mofada.fdread.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import cn.mofada.fdread.R
import kotlinx.android.synthetic.main.dialog_input.*


/**
 * Created by fada on 2017/6/16.
 * 讨论框
 */
class DiscussDialog(context: Context?, themeResId: Int) : Dialog(context, themeResId) {
    var listener: onClick? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window
        window!!.setGravity(Gravity.CENTER)
        setContentView(R.layout.dialog_input)
//        setCanceledOnTouchOutside(true)

        dialog_cancel.setOnClickListener {
            dialog_input.clearFocus()
            dismiss()
        }

        dialog_clear.setOnClickListener {
            dialog_input.text.clear()
        }

        dialog_submit.setOnClickListener {
            if (listener != null) {
                listener!!.onClick(it)
            }
            dialog_input.clearFocus()
            dialog_input.text.clear()
            dismiss()
        }
    }

    fun listener(listener: onClick) {
        this.listener = listener
    }

    interface onClick {
        fun onClick(v: View)
    }
}