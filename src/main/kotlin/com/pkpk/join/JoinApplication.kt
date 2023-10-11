package com.pkpk.join


import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.ComponentScan


@SpringBootApplication
@MapperScan("com.gitee.sunchenbin.mybatis.actable.dao.*", "com.pkpk.join.mapper")
@ComponentScan("com.gitee.sunchenbin.mybatis.actable.manager.*", "com.pkpk.join.*")
class JoinApplication

lateinit var runApplication: ConfigurableApplicationContext

fun main(args: Array<String>) {
    runApplication = runApplication<JoinApplication>(*args)
}