package com.example.fada.fdread.adapter

import android.view.View

/**
 * Created by fada on 2017/6/13.
 */
interface OnItemClickListener {
    fun onItemClick(view: View, position: Int)

    fun onItemLongClick(view: View, position: Int)
}