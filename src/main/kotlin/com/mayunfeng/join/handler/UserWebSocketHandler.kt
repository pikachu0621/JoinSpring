package com.mayunfeng.join.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.mayunfeng.join.config.TOKEN_PARAMETER
import com.mayunfeng.join.service.impl.TokenServiceImpl
import com.mayunfeng.join.service.impl.UserServiceImpl
import com.mayunfeng.join.utils.OtherUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger







@Component
@CrossOrigin // 跨域
class UserWebSocketHandler: TextWebSocketHandler()  {


    private val log: Logger = LoggerFactory.getLogger(UserWebSocketHandler::class.java)

    @Autowired
    private lateinit var userServiceImpl: UserServiceImpl

    @Autowired
    private lateinit var tokenServiceImpl: TokenServiceImpl


    private val onlineNum = AtomicInteger()
    private val sessionPools = ConcurrentHashMap<String, WebSocketSession>()

    @Throws(IOException::class)
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val token = OtherUtils.getMustParameter(session.uri, session.handshakeHeaders, TOKEN_PARAMETER)!!
        val userInfo = userServiceImpl.userInfoById(tokenServiceImpl.queryByToken(token)!!.userId)
        log.info("    >>用户 ${userInfo.userNickname} 说：${message.payload}")
        // session.sendMessage(TextMessage(ObjectMapper().writeValueAsString(userInfo)))
    }

    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {
        val token = OtherUtils.getMustParameter(session.uri, session.handshakeHeaders, TOKEN_PARAMETER)!!
        val userInfo = userServiceImpl.userInfoById(tokenServiceImpl.queryByToken(token)!!.userId)
        disLinkUnboundToken()

        sessionPools[token] = session
        addOnlineCount()

        log.info("[*]用户 ${userInfo.userNickname} 已上线, 当前在线人数$onlineNum")
        // session.sendMessage(TextMessage("[*]用户 ${userInfo.userNickname} 已上线, 当前在线人数$onlineNum"))
    }


    @Throws(Exception::class)
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val token = OtherUtils.getMustParameter(session.uri, session.handshakeHeaders, TOKEN_PARAMETER)!!
        sessionPools.remove(token)
        subOnlineCount()

        val queryByToken = tokenServiceImpl.queryByToken(token)
        if (queryByToken == null){
            // 其他地方登录
            log.info("[*] 其他设备登录/密码被修改, 当前在线人数$onlineNum")
        } else {
            // 正常下线
            val userInfo = userServiceImpl.userInfoById(queryByToken.userId)
            log.info("[*]用户 ${userInfo.userNickname} 已下线, 当前在线人数$onlineNum")
        }


    }


    /**
     * 断开指定用户
     */
    fun disLinkByUserId(userId: Long){
        val webSocketSession = isUserIdOnline(userId) ?: return
        webSocketSession.close(CloseStatus.GOING_AWAY)
    }


    /**
     * 断开 失效的 token
     *
     * GOING_AWAY = new CloseStatus(1001);  其他设备登录
     * PROTOCOL_ERROR = new CloseStatus(1002);  密码被修改
     *
     */
    fun disLinkUnboundToken(status: CloseStatus = CloseStatus.GOING_AWAY){
        sessionPools.forEach { (t,  w) ->
            if (tokenServiceImpl.queryByToken(t) == null) {
                w.close(status)
            }
        }
    }





    /**
     * 添加链接人数
     */
    fun addOnlineCount() {
        onlineNum.incrementAndGet()
    }

    /**
     * 移除链接人数
     */
    fun subOnlineCount() {
        onlineNum.decrementAndGet()
    }


    /**
     * 判断用户是否在线
     */
    fun isUserIdOnline(userId: Long): WebSocketSession?{
        sessionPools.forEach { (t,  w) ->
            if (tokenServiceImpl.queryByToken(t)?.userId == userId) {
                return w
            }
        }
        return null
    }






    /**
     * 给指定 token 发送消息
     *
     *
     *
     */
    fun sendMessageToToken(msg: Any, token: String){
        sessionPools.forEach { (t, ws) ->
            if (t == token) {
                val jsonMsg = ObjectMapper().writeValueAsString(msg)
                ws.sendMessage(TextMessage(jsonMsg))
            }
        }
    }

    /**
     * 给指定 userId 发送消息
     */
    fun sendMessageToUserId(msg: Any, userId: Long){
        sessionPools.forEach { (t, ws) ->
            if (tokenServiceImpl.queryByToken(t)!!.userId == userId) {
                val jsonMsg = ObjectMapper().writeValueAsString(msg)
                ws.sendMessage(TextMessage(jsonMsg))
            }
        }
    }

}