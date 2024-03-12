package com.pkpk.join.config

import com.pkpk.join.handler.UserWebSocketHandler
import com.pkpk.join.interceptor.WebSocketInterceptor
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
    private lateinit var appConfig: AppConfig

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(userWebSocketController, *appConfig.configWebsocketPath)
            .addInterceptors(webSocketInterceptor)
            .setAllowedOrigins("*")
    }


}