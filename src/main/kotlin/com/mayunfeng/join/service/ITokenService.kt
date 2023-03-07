package com.mayunfeng.join.service

import com.mayunfeng.join.model.TokenTableModel

interface ITokenService {


    /**
     * 添加 token
     *
     * @param userId 用户id
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param tokenTime 用户持续时长
     *
     */
    fun put(userId: Long, userAccount: String, userPassword: String, tokenTime: Long): TokenTableModel




    /**
     * 根据 token 获取数据
     *
     * @param token token
     */
    fun queryByToken(token: String): TokenTableModel?



    /**
     * 根据 user_id 获取数据
     *
     * @param userId userId
     */
    fun queryByUserId(userId: Long): TokenTableModel?


    /**
     * 根据 token 删除数据
     *
     * @param token token
     */
    fun deleteByToken(token: String)


    /**
     * 根据 user_id 删除数据
     *
     * @param userId userId
     */
    fun deleteByUserId(userId: Long)



    /**
     * 根据 token 验证
     *
     * @param token token
     * @return true 可用   false 不可用
     */
    fun verify(token: String): Boolean

}