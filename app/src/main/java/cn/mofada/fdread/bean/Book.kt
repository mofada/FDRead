package cn.mofada.fdread.bean

import org.litepal.crud.DataSupport
import java.io.Serializable

/**
 * Created by fada on 2017/6/11.
 * "cover": "http://www.biqukan.com/files/article/image/1/1094/1094s.jpg",
"title": "一念永恒",
"author": "耳根",
"type": "玄幻小说",
"condition_": "连载",
"word": "2443225",
"time": "2017-06-15 11:50:00",
"update": "第846章 冥皇继位",
"updateChapter": "/1_1094/14700525.html",
"intro": "一念成沧海，一念化桑田。一念斩千魔，一念诛万仙。唯我念……永恒",
"lists":
 */
data class Book(
        var bookId: String,
        var cover: String = "",
        var title: String = "",
        var author: String = "",
        var type: String = "",
        var condition_: String = "",
        var word: String = "",
        var time: String = "",
        var update_: String = "",
        var currChapter: String?,
        var currChapterId: String? ,
        var updateChapter: String = "",
        var intro: String = "",
        var uid: String = ""
) : Serializable, DataSupport()