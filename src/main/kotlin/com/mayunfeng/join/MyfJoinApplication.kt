package com.mayunfeng.join


import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.ComponentScan


@SpringBootApplication
@MapperScan("com.gitee.sunchenbin.mybatis.actable.dao.*", "com.mayunfeng.join.mapper")
@ComponentScan("com.gitee.sunchenbin.mybatis.actable.manager.*", "com.mayunfeng.join.*")
class MyfJoinApplication

lateinit var runApplication: ConfigurableApplicationContext

fun main(args: Array<String>) {
    runApplication = runApplication<MyfJoinApplication>(*args)
}