package com.pkpk.join.controller

import com.pkpk.join.base.BaseController
import com.pkpk.join.service.IPublicService
import com.pkpk.join.service.impl.PublicServiceImpl
import com.pkpk.join.utils.JsonResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/pk-puc-api")
@CrossOrigin // 跨域
class PublicController : BaseController(), IPublicService {


    @Autowired
    private lateinit var publicServiceImpl: PublicServiceImpl


    @GetMapping("/test")
    override fun test(): JsonResult<String> = publicServiceImpl.test()


    @GetMapping("/test-time/{time}", "/test-time")
    override fun testTime(@PathVariable("time", required = false) aes: String?): JsonResult<Boolean> = publicServiceImpl.testTime(aes)


    @GetMapping("/test-token/{token}", "/test-token")
    override fun testToken(@PathVariable("token", required = false)  token: String?): JsonResult<Boolean> =
        publicServiceImpl.testToken(token)



    @GetMapping("/puc-group-type")
    override fun getGroupType(): JsonResult<Array<String>>  = publicServiceImpl.getGroupType()


    @PostMapping("/b8bf3c230a63bd35")
    @ResponseBody
    override fun upFile(@RequestParam("f") file: MultipartFile?): JsonResult<String> = publicServiceImpl.upFile(file)


}