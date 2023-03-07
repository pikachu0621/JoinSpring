package com.mayunfeng.join.utils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


object TimeUtils {

    fun getCurrentTime(
        format: String = "yyyy-MM-dd HH:mm:ss",
        date: Date = Date()
    ): String = SimpleDateFormat(format, Locale.CHINA).format(date)


    /**
     * 获得两个日期间差
     *
     * @param beginDate
     * @param endDate
     * @return long 单位 秒
     */
    fun getTimeDistance(beginDate: Date, endDate: Date): Long {
        val time = (Calendar.getInstance().apply {
            time = endDate
        }.timeInMillis - Calendar.getInstance().apply {
            time = beginDate
        }.timeInMillis) / 1000
        return abs(time)
    }


    fun getTimeDistance(beginDate: String, endDate: String, format: String = "yyyy-MM-dd HH:mm:ss"): Long {
        val format = SimpleDateFormat(format, Locale.CHINA)
        return getTimeDistance(format.parse(beginDate), format.parse(endDate))

    }


}