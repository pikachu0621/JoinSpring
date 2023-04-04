package com.mayunfeng.join.service

import com.mayunfeng.join.model.UserSignAndStartSign
import com.mayunfeng.join.model.UserSignTable
import com.mayunfeng.join.utils.JsonResult


/**
 * 用户待签到数据
 */
interface IUserSignService {


    /**
     * 用户签到
     *
     * @param signId 签到Id
     * @param key 密码
     *
     */
    fun startUserSign(signId: Long, key: String?): JsonResult<Boolean>


    /**
     * 根据签到ID获取全部签到 数据 要包含用户图片
     *
     */
    fun queryAllBySignId(signId: Long): JsonResult<UserSignAndStartSign>


    /**
     * 获取该用户当前签到任务
     *
     */
    fun queryUserSign(): JsonResult<Array<UserSignTable>>




    /**
     * 通知用户签到（添加数据）
     *
     */
    fun notifyUserSign(groupId: Long, signId: Long){ }


    /**
     * 根据 signId 删除数据
     *
     */
    fun delUserSignBySign(signId: Long){ }






}