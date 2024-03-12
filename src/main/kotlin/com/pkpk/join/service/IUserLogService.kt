package com.pkpk.join.service

import com.pkpk.join.model.UserLogTable


/**
 * 用户日志
 */
interface IUserLogService {


    /**
     * 添加一条日志
     *
     * @param userLogTable  数据
     */
    fun addLog(userLogTable: UserLogTable)

    /**
     * 添加一条日志
     *
     * @param contentText   log 内容
     * @param logType       日志类型
     */
    fun addLog(contentText: String? = null, userId: Long = 0, logType: Int = 0)


    /**
     * 添加一条普通日志
     *
     * @param userLogTable  数据
     */
    fun addLogNormal(userLogTable: UserLogTable)

    fun addLogNormal(contentText: String? = null, userId: Long = 0)


    /**
     * 添加一条登录日志
     *
     * @param userLogTable  数据
     */
    fun addLogLogin(userLogTable: UserLogTable)
    fun addLogLogin(contentText: String? = null, userId: Long = 0)






    fun queryAllLog(): List<UserLogTable>?

    fun queryLogByUserId(userId: Long): List<UserLogTable>?

    fun queryLogById(logId: Long): UserLogTable?

    fun queryLogByType(type: Long): List<UserLogTable>?


    /**
     * 删除一条日志
     *
     * @param logId 日志id
     */
    fun deleteLog(logId: Long)

    fun deleteLog(userLogTable: UserLogTable)


    /**
     * 根据用户id删除日志
     *
     * @param userId 用户id
     */
    fun deleteUserLog(userId: Long? = null)
    fun deleteUserLog(userLogTable: UserLogTable)

    /**
     * 删除全部日志
     *
     */
    fun deleteAllLog()
}