package com.mayunfeng.join.service

import com.mayunfeng.join.utils.JsonResult
import java.util.Arrays

interface IPublicService {


    /**
     * http  test
     */
    fun test(): JsonResult<String>



    /**
     * 验证token 可用性
     * @param aes
     */
    fun testTime(aes: String?): JsonResult<Boolean>

    /**
     * 验证token 可用性
     * @param token
     */
    fun testToken(token: String?): JsonResult<Boolean>


    /**
     * 获取group type
     */
    fun getGroupType(): JsonResult<Array<String>>
}