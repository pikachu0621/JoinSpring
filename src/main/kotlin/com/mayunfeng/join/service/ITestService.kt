package com.mayunfeng.join.service

import com.mayunfeng.join.utils.JsonResult

interface ITestService {


    /**
     * http  test
     */
    fun test(): JsonResult<String>


    /**
     * 验证token 可用性
     * @param token
     */
    fun testToken(token: String?): JsonResult<Boolean>

}