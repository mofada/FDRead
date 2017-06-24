package cn.mofada.fdread.utils

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import cn.mofada.fdread.R

/**
 * Created by fada on 2017/6/15.
 */
object PrefersUtils {
    var sharedPreferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("fdread", Context.MODE_PRIVATE)
        editor = sharedPreferences?.edit()
    }

    fun putBoolean(key: String, value: Boolean) {
        editor?.putBoolean(key, value)?.apply()
    }

    fun getBoolean(key: String, defValue: Boolean = false): Boolean = sharedPreferences?.getBoolean(key, defValue)!!

    fun putString(key: String, value: String?) {
        if (!TextUtils.isEmpty(value))
            editor?.putString(key, value)?.apply()
    }

    fun getString(key: String, defValue: String = ""): String = sharedPreferences?.getString(key, defValue)!!

    fun putFloat(key: String, value: Float) {
            editor?.putFloat(key, value)?.apply()
    }

    fun getFloat(key: String, defValue: Float = 14F): Float = sharedPreferences?.getFloat(key, defValue)!!

    fun putInt(key: String, value: Int) {
        editor?.putInt(key, value)?.apply()
    }

    fun getInt(key: String, defValue: Int = R.color.read_theme_white): Int = sharedPreferences?.getInt(key, defValue)!!

    fun remove(key: String) {
        editor?.remove(key)?.apply()
    }
}