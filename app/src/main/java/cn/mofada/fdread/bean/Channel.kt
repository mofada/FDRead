package cn.mofada.fdread.bean

import java.io.Serializable

/**
 * Created by fada on 2017/6/13.
 * 分类
 */
data class Channel(var title: String, var cover: String, var descriptor: String, var type: Int) : Serializable