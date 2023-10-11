package com.pkpk.join.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component
import java.io.File

const val TOKEN_PARAMETER = "token"
// 添加自定义配置文件
@Component
@PropertySource(value = ["classpath:config.properties"])
class AppConfig {


    @Value("\${config.static.image-path}")
    var configUserImg: String = ""

    @Value("\${config.static.path}")
    var configUserStatic: String = ""

    @Value("\${config.default.pic-name}")
    var configDefaultPicName: String = ""

    @Value("\${config.default.pic-boy-path}")
    var configDefaultPicBoy: String = ""

    @Value("\${config.default.pic-girl-path}")
    var configDefaultPicGirl: String = ""

    @Value("\${config.token.salt}")
    var configSalt: String = ""

    @Value("\${config.token.time}")
    var configTokenTime: Long = -1

    @Value("\${config.chars.max-length}")
    var configMaxLength: Int = 12

    @Value("\${config.chars.min-length}")
    var configMinLength: Int = 6

    @Value("\${config.image.size}")
    var configImageSize: Int = 10

    @Value("\${config.image.time}")
    var configImageTime: Long = 60

    @Value("\${config.image.password}")
    var configImagePassword: String = ""

    @Value("\${client.config.group-type}")
    var clientConfigGroupType: Array<String> = arrayOf()

    @Value("\${config-websocket-path}")
    var configWebsocketPath: Array<String> = arrayOf()

    val configUserDir = "${System.getProperty("user.dir")}${File.separator}"


    fun configUserStaticFilePath(): String = "$configUserDir$configUserStatic${File.separator}"
    fun configUserImageFilePath(): String = "${configUserStaticFilePath()}$configUserImg${File.separator}"

}