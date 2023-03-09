package com.mayunfeng.join.service

import com.mayunfeng.join.model.UserTableModel
import com.mayunfeng.join.utils.JsonResult

interface IUserService {


    /**
     * 用户登录 / 注册
     * 有用户则登录
     *
     * @param userAccount 账号
     * @param userPassword 密码
     * @return 返回 需要携带 token 用于链接 WebSocket
     */
    fun login(userAccount: String?, userPassword: String?): JsonResult<UserTableModel>


    /**
     * token 获取用户数据
     * @param tokenLogin tokenLogin
     */
    fun userInfoByToken(tokenLogin: String): JsonResult<UserTableModel>

}