package com.mayunfeng.join.interceptor

import com.mayunfeng.join.base.BaseCls
import com.mayunfeng.join.service.TokenFailureException
import com.mayunfeng.join.service.impl.TokenServiceImpl
import com.mayunfeng.join.utils.JsonResult
import com.mayunfeng.join.utils.OtherUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


const val TOKEN_PARAMETER = "token"

@Component
class HttpWebInterceptor: HandlerInterceptor,  BaseCls() {

    @Autowired
    private lateinit var tokenServiceImpl: TokenServiceImpl


    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

        val token = request.getParameter(TOKEN_PARAMETER)
        log.info("http---访问控制器前 $token --- ${request.servletPath}")

        if (OtherUtils.isFieldEmpty(token)  || !tokenServiceImpl.verify(token) ) {
            val tokenFailureException = TokenFailureException()
            OtherUtils.returnJson(response, JsonResult.err(tokenFailureException.errorMsg, tokenFailureException.errorCode))
            return false
        }
        return true
    }



    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        super.afterCompletion(request, response, handler, ex)

        log.info("http---访问控制器后 ${ex?.message}")
    }




}