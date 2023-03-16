package com.mayunfeng.join.base

import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class BaseCls {
     val log: Logger = LoggerFactory.getLogger(BaseCls::class.java)

    fun logi(msg: String) {
        log.info(msg)
    }

    fun loge(msg: String) {
        log.error(msg)
    }

}