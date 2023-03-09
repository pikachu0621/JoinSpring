package com.mayunfeng.join

import com.mayunfeng.join.controller.BaseController
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class BaseCls {
    val log: Logger = LoggerFactory.getLogger(BaseCls::class.java)

    fun logi(msg: String) {
        log.info("info: ------------------- $msg")
    }

    fun loge(msg: String) {
        log.error("error: ------------------ $msg")
    }
}