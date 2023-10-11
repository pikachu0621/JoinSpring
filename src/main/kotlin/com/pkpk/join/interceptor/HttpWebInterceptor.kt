package com.pkpk.join.interceptor

import com.pkpk.join.base.BaseCls
import com.pkpk.join.config.TOKEN_PARAMETER
import com.pkpk.join.service.TokenFailureException
import com.pkpk.join.service.impl.TokenServiceImpl
import com.pkpk.join.utils.JsonResult
import com.pkpk.join.utils.OtherUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class HttpWebInterceptor: HandlerInterceptor,  BaseCls() {

    @Autowired
    private lateinit var tokenServiceImpl: TokenServiceImpl


    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        // 这里 header 和 get 参数都可以 拦截未授权用户
        val token: String? = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)
        logi("------ 开始请求：[IP:${OtherUtils.getRemoteIP(request)}]-[token:$token]-[路径:${request.servletPath}]")

        if (OtherUtils.isFieldEmpty(token)  || !tokenServiceImpl.verify(token!!) ) {
            val tokenFailureException = TokenFailureException()
            OtherUtils.returnJson(response, JsonResult.err(tokenFailureException.errorMsg, tokenFailureException.errorCode))
            logi("------ 结束请求-已拦截：[IP:${OtherUtils.getRemoteIP(request)}]-[原因:token无效]-[路径:${request.servletPath}]")
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
        logi("------ 结束请求：[IP:${OtherUtils.getRemoteIP(request)}]-[路径:${request.servletPath}]")
    }




}