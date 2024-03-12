package com.pkpk.join.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component
import java.io.File
import java.io.Serializable

// 添加自定义配置文件
@Component
@PropertySource(value = ["classpath:config.properties"], encoding = "UTF-8")
class AppConfig(private val json: ObjectMapper = ObjectMapper()) {

    private val configFile = File("${System.getProperty("user.dir")}${File.separator}app-config.json")


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


    @Value("\${config-websocket-path}")
    var configWebsocketPath: Array<String> = arrayOf()

    var appConfigEdit: AppConfigEdit = upDataAppConfigEdit()


    val configUserDir = "${System.getProperty("user.dir")}${File.separator}"
    fun configUserStaticFilePath(): String = "$configUserDir$configUserStatic${File.separator}"
    fun configUserImageFilePath(): String = "${configUserStaticFilePath()}$configUserImg${File.separator}"


    // 更新 appConfigEdit 数据 这里为了性能考虑 不使用 get 自动更新， 请手动更新
    private fun upDataAppConfigEdit(): AppConfigEdit {
        // val resource = resourceLoader.getResource("classpath:app-config.json")
        val inputStream = javaClass.getClassLoader().getResourceAsStream("app-config.json")
        appConfigEdit = json.readValue(inputStream, AppConfigEdit::class.java)
        return appConfigEdit
    }

    fun setDataAppConfigEdit(appConfigEdit: AppConfigEdit) {
        json.writeValue(configFile, appConfigEdit)
        this.appConfigEdit = appConfigEdit
    }

    fun initDataAppConfigEdit() {
        if (!configFile.exists()) {
            upDataAppConfigEdit()
            json.writeValue(configFile, appConfigEdit)
            return
        }
        appConfigEdit = json.readValue(configFile, AppConfigEdit::class.java)
    }
}


/**
 * 在 classpath:app-config.json
 *
 * @param appIsRemove            是否关闭软件
 * @param appPackageName         包名
 * @param groupTypeArr           组类型
 * @param imagePassword          图片过期加密密码
 * @param imageTime              图片有效时长  -1永久  （单位 s）
 * @param imageSize              图片上传大小   （单位 mb）
 * @param tokenSalt              token 盐
 * @param tokenTime              token有效时长  -1永久 （单位 s）
 * @param userAccountLengthLimit 注册账号长度限制    [min 6 - max 12]
 */
data class AppConfigEdit(
    var appIsRemove: Boolean = false,
    var appPackageName: String = "",
    var groupTypeArr: ArrayList<String> = arrayListOf(),
    var imagePassword: String = "",
    var imageTime: Long = 0,
    var imageSize: Int = 0,
    var tokenSalt: String = "",
    var tokenTime: Long = 0,
    var userAccountLengthLimit: ArrayList<Int> = arrayListOf(),
) : Serializable