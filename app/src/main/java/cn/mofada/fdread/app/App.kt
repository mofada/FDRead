package cn.mofada.fdread.app

import android.app.Application
import cn.mofada.fdread.utils.PrefersUtils
import com.umeng.socialize.Config
import com.umeng.socialize.PlatformConfig
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.common.QueuedWork
import org.litepal.LitePal


class App : Application() {

    override fun onCreate() {

        super.onCreate()
        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        Config.DEBUG = false
        QueuedWork.isUseThreadPool = false
        UMShareAPI.get(this)
        LitePal.initialize(this)
        PrefersUtils.init(this)
    }

    //各个平台的配置，建议放在全局Application或者程序入口
    init {
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba")
    }
}
