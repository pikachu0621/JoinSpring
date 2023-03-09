package com.mayunfeng.join.controller

import com.mayunfeng.join.BaseCls
import com.mayunfeng.join.service.ParameterException
import com.mayunfeng.join.service.ParameterIllegalException
import com.mayunfeng.join.service.ServiceException
import com.mayunfeng.join.utils.ERROR
import com.mayunfeng.join.utils.JsonResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler
import java.io.Serializable


open class BaseController: BaseCls() {

    // 异常这里统一处理
    @ExceptionHandler(ServiceException::class)
    fun handleException(e: Throwable): JsonResult<Serializable>{
        if (e is ServiceException){
            return JsonResult.err(e.errorMsg, e.errorCode)
        }
        return JsonResult.err("未知错误", ERROR)
    }
}