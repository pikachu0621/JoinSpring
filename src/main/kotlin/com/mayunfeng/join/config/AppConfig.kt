package com.mayunfeng.join.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
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

    @Value("\${config.default.pic-path}")
    var configDefaultPic: String = ""

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

    @Value("\${config.image.type}")
    var configImageType: String = ""

    @Value("\${config.image.time}")
    var configImageTime: Long = 60

    @Value("\${config.image.password}")
    var configImagePassword: String = ""


    val configUserDir = "${System.getProperty("user.dir")}${File.separator}"

    fun configUserStaticFilePath(): String = "$configUserDir$configUserStatic${File.separator}"
    fun configUserImageFilePath(): String = "${configUserStaticFilePath()}$configUserImg${File.separator}"

}