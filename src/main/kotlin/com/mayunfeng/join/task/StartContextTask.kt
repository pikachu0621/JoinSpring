package com.mayunfeng.join.task

import com.mayunfeng.join.BaseCls
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.*


@Component
class StartContextTask : ApplicationContextAware, BaseCls() {

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
    }
}