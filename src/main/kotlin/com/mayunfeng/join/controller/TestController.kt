package com.mayunfeng.join.controller

import com.mayunfeng.join.base.BaseController
import com.mayunfeng.join.service.ITestService
import com.mayunfeng.join.service.impl.TestServiceImpl
import com.mayunfeng.join.utils.JsonResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/myf-test-api")
@CrossOrigin // 跨域
class TestController : BaseController(), ITestService {


    @Autowired
    private lateinit var testServiceImpl: TestServiceImpl


    @GetMapping("/test")
    override fun test(): JsonResult<String> = testServiceImpl.test()


    @GetMapping("/test-time/{time}", "/test-time")
    override fun testTime(@PathVariable("time", required = false) aes: String?): JsonResult<Boolean> = testServiceImpl.testTime(aes)


    @GetMapping("/test-token/{token}", "/test-token")
    override fun testToken(@PathVariable("token", required = false)  token: String?): JsonResult<Boolean> =
        testServiceImpl.testToken(token)
}