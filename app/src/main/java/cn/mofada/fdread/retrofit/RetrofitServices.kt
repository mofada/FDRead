package cn.mofada.fdread.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by fada on 2017/6/16.
 */
class RetrofitServices {
    companion object {
        fun getInstance(): RetrofitServices {
            return Holder.instance
        }
    }

    private object Holder {
        val instance = RetrofitServices()
    }

    /**
     * 获取服务
     */
    fun getRetrofitAndGson(): FDReadServices {
        return Retrofit.Builder()
                .baseUrl("http://www.mofada.cn")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FDReadServices::class.java)
    }

    fun getRetrofit(): FDReadServices {
        return Retrofit.Builder()
                .baseUrl("http://www.mofada.cn")
                .build()
                .create(FDReadServices::class.java)
    }
}