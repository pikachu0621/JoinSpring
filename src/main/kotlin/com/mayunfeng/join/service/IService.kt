package com.mayunfeng.join.service

import com.mayunfeng.join.model.MyfUserTableModel
import com.mayunfeng.join.utils.JsonResult

interface IUserService {

    /**
     * 用户登录 / 注册
     * 没有该用户时注册
     * 有用户则登录
     * 返回 token 用于链接WebSocket
     */
    fun login(userName: String?, user_pws: String?): JsonResult<MyfUserTableModel>


}