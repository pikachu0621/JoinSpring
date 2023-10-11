package com.pkpk.join.task

import com.pkpk.join.base.BaseCls
import com.pkpk.join.service.ISingOutTimeTask
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.stereotype.Component

/**
 * 处理当前签到任务是否过过期
 *
 * 在线Cron表达式生成器 https://www.matools.com/cron/
 */
@Configuration
@EnableScheduling
@Component
class SignOutTimeTask: BaseCls(), ISingOutTimeTask {


    // @Scheduled(cron = "* * * * * ? *")
    override fun verifySignOutTime(){

    }



}