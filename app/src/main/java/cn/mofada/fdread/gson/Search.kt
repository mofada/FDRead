package cn.mofada.fdread.gson

import cn.mofada.fdread.bean.Book

/**
 * Created by fada on 2017/6/16.
 */
data class Search(
        var state: String,
        var search: String,
        var lists: List<Book>
)