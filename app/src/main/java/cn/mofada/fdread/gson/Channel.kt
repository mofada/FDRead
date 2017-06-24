package cn.mofada.fdread.gson

import cn.mofada.fdread.bean.Book

/**
 * Created by fada on 2017/6/15.
 */
/**
 * "fantasy": {
"title": "玄幻小说",
"top": {
"bookId": "/1_1583/",
"title": "圣墟",
"author": "",
"image": "http://www.biqukan.com/images/nocover.jpg",
"detail": "辰东新书，三部曲圣墟带你走进修仙世界！......"
},
"book":
 */
data class Channel(
        var recommend:ArrayList<Book>,
        var list:ArrayList<Book>,
        var state:String
)