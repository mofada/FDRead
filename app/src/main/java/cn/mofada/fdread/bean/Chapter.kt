package cn.mofada.fdread.bean

import org.litepal.crud.DataSupport
import java.io.Serializable

/**
 * Created by fada on 2017/6/11.
章节
 * {"title": "第846章 冥皇继位",
"previous": "/1_1094/14692242.html",
"list": "/1_1094/",
"next": "/1_1094/",
"content":"……"}
 */
data class Chapter(
        var title: String,
        var chapterId: String,
        var previous: String,
        var list: String,
        var next: String,
        var content: String
) : Serializable, DataSupport()