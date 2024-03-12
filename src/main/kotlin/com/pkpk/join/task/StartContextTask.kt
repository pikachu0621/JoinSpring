package com.pkpk.join.task

import com.pkpk.join.base.BaseCls
import com.pkpk.join.config.AppConfig
import com.pkpk.join.service.impl.UserServiceImpl
import com.pkpk.join.utils.AESBCBUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import java.io.File


@Component
class StartContextTask : ApplicationContextAware, BaseCls() {

    @Value("\${server.port}")
    private val serverPort = 0

    @Value("\${custom.app.version}")
    private val appVersion = ""

    @Value("\${custom.root.name}")
    private val rootName = ""

    @Value("\${custom.root.password}")
    private val rootPassword = ""

    @Autowired
    private lateinit var appConfig: AppConfig

    @Autowired
    private lateinit var userServiceImpl: UserServiceImpl

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        println(
            "=====================================================\n" +
                    "      _           _         \n" +
                    "     | |         (_)        \n" +
                    "     | |   ___    _   _ __  \n" +
                    " _   | |  / _ \\  | | | '_ \\ \n" +
                    "| |__| | | (_) | | | | | | |\n" +
                    " \\____/   \\___/  |_| |_| |_|\n" +
                    "-----------------------------------------------------\n" +
                    "作者: \t\t\thttp://pkpk.run\n" +
                    "版本: \t\t\t$appVersion\n" +
                    "管理地址: \t\thttp://127.0.0.1:$serverPort/admin/index.html\n" +
                    "ROOT帐号和密码: \t[${rootName}, ${rootPassword}]\n" +
                    "====================================================="
        )
        // 加载配置
        appConfig.initDataAppConfigEdit()
        // 图片AES时效加密
        AESBCBUtils.init(appConfig.appConfigEdit.imagePassword)
        // 创建用户数据文件夹   user/img  user/...
        createUserDir()
        // 创建ROOT用户
        userServiceImpl.registeredRoot(rootName, rootPassword)
    }

    fun createUserDir(){
        val file = File(appConfig.configUserImageFilePath())
        if (!file.exists()) file.mkdirs()
    }

}