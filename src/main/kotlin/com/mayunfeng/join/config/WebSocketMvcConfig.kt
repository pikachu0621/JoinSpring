package com.mayunfeng.join.config

import com.mayunfeng.join.handler.UserWebSocketHandler
import com.mayunfeng.join.interceptor.WebSocketInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry


@Configuration
@EnableWebSocket
class WebSocketMvcConfig : WebSocketConfigurer {


    @Autowired
    private lateinit var webSocketInterceptor: WebSocketInterceptor

    @Autowired
    private lateinit var userWebSocketController: UserWebSocketHandler

    @Autowired
    private lateinit var APPConfig: AppConfig

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {

        registry.addHandler(userWebSocketController, *APPConfig.configWebsocketPath)
            .addInterceptors(webSocketInterceptor)
            .setAllowedOrigins("*")
    }


}