package com.mayunfeng.join.base



import com.mayunfeng.join.utils.ERROR
import com.mayunfeng.join.utils.JsonResult
import org.springframework.web.bind.annotation.ExceptionHandler
import java.io.Serializable
import javax.servlet.http.HttpServletResponse


open class BaseController: BaseCls() {

    // 异常这里统一处理
    @ExceptionHandler(BaseServiceException::class)
    fun handleException(e: Throwable, response: HttpServletResponse): JsonResult<Serializable>{
        if (e is BaseServiceException){
            return JsonResult.err(e.errorMsg, e.errorCode)
        }
        return JsonResult.err("未知错误", ERROR)
    }
}