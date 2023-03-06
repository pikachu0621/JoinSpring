package com.mayunfeng.join.controller

import com.mayunfeng.join.service.ParameterException
import com.mayunfeng.join.service.ServiceException
import com.mayunfeng.join.service.UsernameDuplicateException
import com.mayunfeng.join.utils.ERROR
import com.mayunfeng.join.utils.ERROR_PARAMETER
import com.mayunfeng.join.utils.JsonResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler


open class BaseController {

    val log: Logger = LoggerFactory.getLogger(BaseController::class.java)

    // 异常这里统一处理
    @ExceptionHandler(ServiceException::class)
    fun handleException(e: Throwable): JsonResult<Void>{
        when(e){

            is ParameterException ->{
                return JsonResult.err("参数错误", ERROR_PARAMETER)
            }

            is UsernameDuplicateException ->{
                return JsonResult.err("用户名已被占用", -400)
            }

        }
        return JsonResult.err("其他错误", ERROR)
    }




}