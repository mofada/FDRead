package cn.mofada.fdread

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.AccelerateInterpolator
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * 闪屏
 */
class SplashActivity : AppCompatActivity () {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        /**
         * 采用动画进行计时
         */
        val valueAnimator: ValueAnimator = ValueAnimator.ofFloat(1F, 0.5F)
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?) {
                MainActivity.startActivity(this@SplashActivity)
                finish()
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
            }
        })
        valueAnimator.addUpdateListener {
            relative.alpha = it.animatedValue as Float
        }
        valueAnimator.interpolator = AccelerateInterpolator()
        valueAnimator.duration = 3000
        valueAnimator.start()

        linear_go.setOnClickListener {
            valueAnimator.cancel()
        }
    }
}
