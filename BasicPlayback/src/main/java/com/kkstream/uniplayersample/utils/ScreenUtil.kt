package com.kkstream.uniplayersample.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

object ScreenUtil {

    fun getScreenWidth(context: Context?): Int {
        val wm = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val matrix = DisplayMetrics()
        wm.defaultDisplay.getMetrics(matrix)
        return matrix.widthPixels
    }

    fun px2dp(context: Context, pxVal: Int): Int {
        val density = context.resources.displayMetrics.density
        return pxVal.div(density).toInt()
    }
}