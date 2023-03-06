package com.mayunfeng.join.utils

import java.text.SimpleDateFormat
import java.util.Date

object TimeUtils {

    fun getCurrentTime(format: String = "yyyy-MM-dd HH:mm:ss",
                       date: Date = Date()): String =
        SimpleDateFormat(format).format(date)





}