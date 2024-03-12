package com.pkpk.join.controller

import com.pkpk.join.base.BaseController
import com.pkpk.join.config.AppConfigEdit
import com.pkpk.join.config.API_PUBLIC
import com.pkpk.join.service.IPublicService
import com.pkpk.join.service.impl.PublicServiceImpl
import com.pkpk.join.utils.JsonResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping(API_PUBLIC)
@CrossOrigin // 跨域
class PublicController : BaseController(), IPublicService {


    @Autowired
    private lateinit var publicServiceImpl: PublicServiceImpl


    @PostMapping("/test")
    override fun test(@RequestBody(required = false) appConfigEdit: AppConfigEdit?): JsonResult<AppConfigEdit> =
        publicServiceImpl.test(appConfigEdit)

    @GetMapping("/test-xff")
    override fun testIp() = publicServiceImpl.testIp()

    @GetMapping("/test-time/{time}", "/test-time")
    override fun testTime(@PathVariable("time", required = false) aes: String?) = publicServiceImpl.testTime(aes)


    @GetMapping("/test-token/{token}", "/test-token")
    override fun testToken(@PathVariable("token", required = false) token: String?) = publicServiceImpl.testToken(token)


    @GetMapping("/puc-group-type")
    override fun getGroupType() = publicServiceImpl.getGroupType()


    @GetMapping("/git-logs/{project}", "/git-logs")
    override fun getGithubCommitLogs(@PathVariable("project", required = false) project: String?) =
        publicServiceImpl.getGithubCommitLogs(project)


    @PostMapping("/b8bf3c230a63bd35")
    @ResponseBody
    override fun upFile(@RequestParam("f") file: MultipartFile?) = publicServiceImpl.upFile(file)


}