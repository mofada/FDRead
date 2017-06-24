package cn.mofada.fdread.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.view.Gravity
import android.view.View
import cn.mofada.fdread.R
import cn.mofada.fdread.adapter.LoadingDialogAdapter
import cn.mofada.fdread.adapter.OnItemClickListener
import cn.mofada.fdread.base.Constant
import cn.mofada.fdread.utils.PrefersUtils
import kotlinx.android.synthetic.main.dialog_set_loading.*


/**
 * Created by fada on 2017/6/16.
 */
class SetLoadingDialog(context: Context?, themeResId: Int) : Dialog(context, themeResId) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window
        window!!.setGravity(Gravity.CENTER)
        setContentView(R.layout.dialog_set_loading)
        setCanceledOnTouchOutside(true)

        val dialogAdapter: LoadingDialogAdapter = LoadingDialogAdapter(INDICATORS)
        dialogAdapter.listener(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                PrefersUtils.putString(Constant.INDICATOR, dialogAdapter.data[position])
                dismiss()
            }
        })
        recycle_dialog.adapter = dialogAdapter
        recycle_dialog.layoutManager = GridLayoutManager(context, 4)
        recycle_dialog.itemAnimator = DefaultItemAnimator()
        recycle_dialog.addItemDecoration(DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL))
        recycle_dialog.addItemDecoration(DividerItemDecoration(
                context, DividerItemDecoration.HORIZONTAL))
    }

    private val INDICATORS: List<String> = arrayListOf(
            "BallPulseIndicator", "BallGridPulseIndicator", "BallClipRotateIndicator", "BallClipRotatePulseIndicator",
            "SquareSpinIndicator", "BallClipRotateMultipleIndicator", "BallPulseRiseIndicator", "BallRotateIndicator",
            "CubeTransitionIndicator", "BallZigZagIndicator", "BallZigZagDeflectIndicator", "BallTrianglePathIndicator",
            "BallScaleIndicator", "LineScaleIndicator", "LineScalePartyIndicator", "BallScaleMultipleIndicator",
            "BallPulseSyncIndicator", "BallBeatIndicator", "LineScalePulseOutIndicator", "LineScalePulseOutRapidIndicator",
            "BallScaleRippleIndicator", "BallScaleRippleMultipleIndicator", "BallSpinFadeLoaderIndicator", "LineSpinFadeLoaderIndicator",
            "TriangleSkewSpinIndicator", "PacmanIndicator", "BallGridBeatIndicator", "SemiCircleSpinIndicator"
    )
}