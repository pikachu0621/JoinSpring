package com.mayunfeng.join.service


/**
 * 定时器
 */
interface ISingOutTimeTask {


    /**
     * 定时验证创建的签到任务是否过期
     *
     * 1s 轮询一次
     *
     */
    fun verifySignOutTime()


}