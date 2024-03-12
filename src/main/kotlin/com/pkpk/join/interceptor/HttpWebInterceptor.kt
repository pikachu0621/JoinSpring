package com.pkpk.join.interceptor

import com.pkpk.join.base.BaseCls
import com.pkpk.join.config.*
import com.pkpk.join.service.AppOffException
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
class HttpWebInterceptor : HandlerInterceptor, BaseCls() {

    @Autowired
    private lateinit var tokenServiceImpl: TokenServiceImpl


    @Autowired
    private lateinit var appConfig: AppConfig


    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        // api 接口是否关闭
        if (isIncludePath(request.servletPath) && appConfig.appConfigEdit.appIsRemove) {
            val appOffException = AppOffException()
            OtherUtils.returnJson(response, JsonResult.err(appOffException.errorMsg, appOffException.errorCode))
            logi("------ 结束请求-已拦截：[IP:${OtherUtils.getRemoteIP(request)}]-[原因:后台已关闭API]-[路径:${request.servletPath}]")
            return false
        }
        // api 鉴权
        val token: String? = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)
        logi("------ 开始请求：[IP:${OtherUtils.getRemoteIP(request)}]-[token:$token]-[路径:${request.servletPath}]")
        if (OtherUtils.isFieldEmpty(token) || !tokenServiceImpl.verify(token!!)) {
            val tokenFailureException = TokenFailureException()
            OtherUtils.returnJson(
                response,
                JsonResult.err(tokenFailureException.errorMsg, tokenFailureException.errorCode)
            )
            logi("------ 结束请求-已拦截：[IP:${OtherUtils.getRemoteIP(request)}]-[原因:token无效]-[路径:${request.servletPath}]")
            return false
        }
        return true
    }


    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?,
    ) {
        super.afterCompletion(request, response, handler, ex)
        logi("------ 结束请求：[IP:${OtherUtils.getRemoteIP(request)}]-[路径:${request.servletPath}]")
    }


    private fun isIncludePath(path: String?): Boolean {
        path ?: return false
        if (path.length <= 2) return false
        val offApis = arrayOf(
            API_GROUP,
            API_JOIN_GROUP,
            API_PICTURE,
            API_PUBLIC,
            API_START_SIGN,
            API_USER,
            API_USER_SIGN)
        for (api in offApis) if (path.contains(api)) return true
        return false
    }


}