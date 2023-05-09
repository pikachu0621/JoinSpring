package com.mayunfeng.join.task

import com.baomidou.mybatisplus.extension.toolkit.SqlRunner
import com.mayunfeng.join.base.BaseCls
import com.mayunfeng.join.config.AppConfig
import com.mayunfeng.join.utils.AESBCBUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import java.io.File


@Component
class StartContextTask : ApplicationContextAware, BaseCls() {

    @Autowired
    private lateinit var APPConfig: AppConfig

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        println("                                                                                   ,-,------,   \n" +
                " ____    ____                ___      _____             _                        _ \\(\\(_,--'  \n" +
                "|_   \\  /   _|             .' ..]    |_   _|           (_)                 <`--'\\>/(/(__      \n" +
                "  |   \\/   |     _   __   _| |_        | |     .--.    __    _ .--.        /. .  `'` '  \\     \n" +
                "  | |\\  /| |    [ \\ [  ] '-| |-'   _   | |   / .'`\\ \\ [  |  [ `.-. |     (`')  ,        @     \n" +
                " _| |_\\/_| |_    \\ '/ /    | |    | |__' |   | \\__. |  | |   | | | |      `-._,        /      \n" +
                "|_____||_____| [\\_:  /    [___]   `.____.'    '.__.'  [___] [___||__]         )-)_/--( (        \n" +
                "                \\__.'                                                         ''''  ''''        \n"
        )
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