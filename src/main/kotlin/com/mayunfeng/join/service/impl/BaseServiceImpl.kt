package com.mayunfeng.join.service.impl

import org.slf4j.Logger
import org.slf4j.LoggerFactory


 open class BaseServiceImpl {

    val log: Logger = LoggerFactory.getLogger(BaseServiceImpl::class.java)

     fun logi(msg: String){
         log.info("info: ------------------- $msg")
     }

     fun loge(msg: String){
         log.error("error: ------------------ $msg")
     }
}