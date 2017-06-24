package cn.mofada.fdread.gson

import cn.mofada.fdread.bean.Book

/**
 * Created by fada on 2017/6/16.
 */
data class Ranking(
        var ranking: List<Channel>,
        var state: String
) {
    data class Channel(
            var title: String,
            var lists: List<Book>
    )
}