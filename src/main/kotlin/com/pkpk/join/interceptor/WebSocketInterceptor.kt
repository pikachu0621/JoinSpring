package com.pkpk.join.interceptor

import com.pkpk.join.base.BaseCls
import com.pkpk.join.config.AppConfig
import com.pkpk.join.config.TOKEN_PARAMETER
import com.pkpk.join.service.AppOffException
import com.pkpk.join.service.impl.TokenServiceImpl
import com.pkpk.join.utils.JsonResult
import com.pkpk.join.utils.OtherUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor



@Component
class WebSocketInterceptor: HandshakeInterceptor, BaseCls() {


    @Autowired
    private lateinit var tokenServiceImpl: TokenServiceImpl

    @Autowired
    private lateinit var appConfig: AppConfig

    override fun beforeHandshake(
        request:  ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        // api 接口是否关闭
        if (appConfig.appConfigEdit.appIsRemove) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED)
            logi("------ 握手失败：[IP:${OtherUtils.getRemoteIP(request)}]-[原因:后台已关闭API]-[请求Uri:${request.uri.path}]")
            return false
        }
        // websocket 握手鉴权
        val token = OtherUtils.getMustParameter(request.uri, request.headers, TOKEN_PARAMETER)
        logi("------ 开始握手：[IP:${OtherUtils.getRemoteIP(request)}]-[token:$token]-[请求Uri:${request.uri.path}]")
        if (OtherUtils.isFieldEmpty(token) || !tokenServiceImpl.verify(token!!) ) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED)
            logi("------ 握手失败：[IP:${OtherUtils.getRemoteIP(request)}]-[原因:token无效]-[请求Uri:${request.uri.path}]-[token-${token}]")
            return false
        }
        return true
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?
    ) {
        logi("------ 握手完成：[IP:${OtherUtils.getRemoteIP(request)}]-[请求Uri:${request.uri.path}]")
    }
}
