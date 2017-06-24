package cn.mofada.fdread.gson

import cn.mofada.fdread.bean.Book

/**
 * Created by fada on 2017/6/15.
 */
/**
 * {
"recommend": [],
"fantasy": {},
"xiuzhen": {},
"city": {},
"through": {},
"game ": {},
"science ": {},
"state": "ok"
}
 */
data class Index(
        var recommend: ArrayList<Book>,
        var fantasy: Channel,
        var xiuzhen: Channel,
        var city: Channel,
        var through: Channel,
        var game: Channel,
        var science: Channel,
        var state: String
) {
    data class Channel(
            var title: String,
            var top: Book,
            var book: ArrayList<Book>
    )
}