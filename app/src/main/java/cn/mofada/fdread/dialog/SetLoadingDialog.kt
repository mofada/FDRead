package cn.mofada.fdread.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import cn.mofada.fdread.R
import kotlinx.android.synthetic.main.dialog_loading.*


/**
 * Created by fada on 2017/6/16.
 */
class LoadingDialog(context: Context?, themeResId: Int) : Dialog(context, themeResId) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window
        window!!.setGravity(Gravity.CENTER)
        setContentView(R.layout.dialog_loading)
        setCanceledOnTouchOutside(false)

        loadingView.show()
    }
}