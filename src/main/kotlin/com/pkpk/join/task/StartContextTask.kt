package com.pkpk.join.task

import com.pkpk.join.base.BaseCls
import com.pkpk.join.config.AppConfig
import com.pkpk.join.utils.AESBCBUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringBootVersion
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import java.io.File


@Component
class StartContextTask : ApplicationContextAware, BaseCls() {

    @Autowired
    private lateinit var APPConfig: AppConfig

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        println("      _           _         \n" +
                "     | |         (_)        \n" +
                "     | |   ___    _   _ __  \n" +
                " _   | |  / _ \\  | | | '_ \\ \n" +
                "| |__| | | (_) | | | | | | |\n" +
                " \\____/   \\___/  |_| |_| |_|\n" +
                "form: pkpk-run, versions: ${ this.javaClass::class.java.getPackage().implementationVersion}")
        // 图片时效加密算法
        AESBCBUtils.init(APPConfig.configImagePassword)
        // 创建用户数据文件夹   user/img  user/...
        createUserDir()
        /*alter TABLE users AUTO_INCREMENT=1000;*/
    }

    fun createUserDir(){
        val file = File(APPConfig.configUserImageFilePath())
        if (!file.exists()) file.mkdirs()
    }

}