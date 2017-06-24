package cn.mofada.fdread.gson

import cn.mofada.fdread.bean.Chapter

/**
 * Created by fada on 2017/6/19.
 */
data class GsonBook(
        var bookId: String,
        var cover: String,
        var title: String,
        var author: String,
        var type: String,
        var condition_: String,
        var word: String,
        var time: String,
        var update_: String,
        var updateChapter: String,
        var intro: String,
        var uid: String,
        var lists: List<Chapter>
)