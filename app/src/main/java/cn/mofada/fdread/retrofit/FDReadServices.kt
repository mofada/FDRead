package cn.mofada.fdread.retrofit

import android.widget.TextView
import cn.mofada.fdread.bean.Book
import cn.mofada.fdread.bean.Chapter
import cn.mofada.fdread.bean.Discuss
import cn.mofada.fdread.gson.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


/**
 * Created by fada on 2017/6/16.
 */
interface FDReadServices {
    /**
     * 获取小说首页
     */
    @GET("/fdread/index.php")
    fun index(): Call<Index>

    var view: TextView

    /**
     * 获取书籍信息
     * @bookId 书籍id
     */
    @GET("/fdread/book.php")
    fun book(@Query("bookId") bookId: String): Call<GsonBook>

    /**
     * 获取章节信息
     * @chapterId 章节id
     */
    @GET("/fdread/read.php")
    fun chapter(@Query("chapterId") chapterID: String): Call<Chapter>

    /**
     * 搜索小说
     * @search 搜索条件
     */
    @GET("/fdread/search.php")
    fun search(@Query("search") search: String): Call<Search>

    /**
     * 小说分类
     * 1 -> 玄幻
     * 2 -> 修真
     * 3 -> 都市
     * 4 -> 穿越
     * 5 -> 网游
     * 6 -> 科幻
     * 7 -> 其他
     * 8 -> 完本
     */
    @GET("/fdread/channel.php")
    fun channel(@Query("type") type: Int): Call<Channel>

    /**
     * 获取小说排行榜
     */
    @GET("/fdread/ranking.php")
    fun ranking(): Call<Ranking>

    // --------------------------------------------------------------------------------------------------------
    /*数据库操作*/

    /**
     * 删除书籍
     */
    @GET("/fdread/db/book_delete.php")
    fun book_delete(@Query("uid") uid: String, @Query("bookId") bookId: String): Call<Line>

    /**
     * 添加书籍
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("/fdread/db/book_insert.php")
    fun book_insert(@Query("book") book: String): Call<Line>

    /**
     * 获取书籍
     */
    @GET("/fdread/db/book_query.php")
    fun book_query(@Query("uid") uid: String): Call<List<Book>>

    /**
     * 更新书籍
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("/fdread/db/book_update.php")
    fun book_update(@Query("book") uid: String): Call<Line>

    /**
     * 添加讨论
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("/fdread/db/discuss_insert.php")
    fun discuss_insert(@Query("discuss") discuss: String): Call<Line>

    @GET("/fdread/db/discuss_query.php")
    fun discuss_query(): Call<List<Discuss>>

    @GET("/fdread/notification.php")
    fun  notification(): Call<Line>
}