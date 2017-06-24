package cn.mofada.fdread.retrofit

/**
 * Created by fada on 2017/6/16.
 */
class Retrofit{
    companion object {
        fun getInstance(): Retrofit {
            return Holder.instance
        }
    }

    private object Holder {
        val instance = Retrofit()
    }

    fun getRetrofit():FDReadServices{
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .build()

        val service = retrofit.create(GitHubService::class.java)
    }
}