package com.mayunfeng.join.service.impl

import com.mayunfeng.join.base.BaseServiceImpl
import com.mayunfeng.join.service.ITestService
import com.mayunfeng.join.service.ParameterException
import com.mayunfeng.join.utils.JsonResult
import com.mayunfeng.join.utils.OtherUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class TestServiceImpl:  BaseServiceImpl(), ITestService {


    @Autowired
    private lateinit var tokenServiceImpl: TokenServiceImpl

    override fun test(): JsonResult<String>  = JsonResult.ok("红豆生南国，春来发几枝。")

    override fun testToken(token: String?): JsonResult<Boolean> {
        if (OtherUtils.isFieldEmpty(token))  throw ParameterException()
        return JsonResult.ok(tokenServiceImpl.verify(token!!))
    }

}