package com.mayunfeng.join.interceptor

import com.mayunfeng.join.base.BaseCls
import com.mayunfeng.join.config.AppConfig
import com.mayunfeng.join.config.TOKEN_PARAMETER
import com.mayunfeng.join.service.TokenFailureException
import com.mayunfeng.join.service.impl.TokenServiceImpl
import com.mayunfeng.join.utils.JsonResult
import com.mayunfeng.join.utils.OtherUtils
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

    override fun beforeHandshake(
        request:  ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        // WebSocket 握手鉴权
        val token = OtherUtils.getMustParameter(request.uri, request.headers, TOKEN_PARAMETER)

        logi("------ 开始握手：[IP:${OtherUtils.getRemoteIP(request)}]-[token:$token]-[请求Uri:${request.uri.path}]")
        if (OtherUtils.isFieldEmpty(token) || !tokenServiceImpl.verify(token!!) ) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED)
            logi("------ 握手失败：[IP:${OtherUtils.getRemoteIP(request)}]-[原因:token无效]-[请求Uri:${request.uri.path}")
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

        logi("------ 握手完成：[IP:${OtherUtils.getRemoteIP(request)}]-[请求Uri:${request.uri.path}")
    }
}
