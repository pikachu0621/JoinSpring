package com.pkpk.join.controller

import com.pkpk.join.base.BaseController
import com.pkpk.join.config.API_BG
import com.pkpk.join.config.AppConfigEdit
import com.pkpk.join.model.GroupTable
import com.pkpk.join.model.UserTable
import com.pkpk.join.service.IBackstageService
import com.pkpk.join.service.impl.BackstageServiceImpl
import com.pkpk.join.utils.JsonResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping(API_BG)
@CrossOrigin
class BackstageController : BaseController(), IBackstageService {

    @Autowired
    private lateinit var backstageServiceImpl: BackstageServiceImpl

    @PostMapping("/login")
    @ResponseBody
    override fun loginRoot(
        @RequestParam("account") rootAccount: String,
        @RequestParam("password") rootPassword: String,
    ) = backstageServiceImpl.loginRoot(rootAccount, rootPassword)

   
    @GetMapping("/info")
    override fun verifyToken(@RequestParam("token") token: String, needRoot: Boolean) =
        backstageServiceImpl.verifyToken(token)

    @GetMapping("/all-user")
    override fun getAllUser() = backstageServiceImpl.getAllUser()

    @GetMapping("/all-group")
    override fun getAllGroup() = backstageServiceImpl.getAllGroup()

    @GetMapping("/del-group/{groupId}", "/del-group/**")
    override fun delGroupByGroupId(@PathVariable("groupId") groupId: Long) =
        backstageServiceImpl.delGroupByGroupId(groupId)

    @GetMapping("/del-user/{userId}", "/del-user/**")
    override fun delUserByUserId(@PathVariable("userId") userId: Long) = backstageServiceImpl.delUserByUserId(userId)


    @PostMapping("/edit-user")
    @ResponseBody
    override fun rootEditUserInfo(
        @RequestParam("image", required = false) userImage: MultipartFile?,
        @RequestBody argument: IBackstageService.EditUserInfoArgument?,
    ) = backstageServiceImpl.rootEditUserInfo(userImage, argument)

    @PostMapping("/edit-group")
    @ResponseBody
    override fun rootEditGroupInfo(
        @RequestParam("name") groupName: String,
        @RequestParam("introduce") groupIntroduce: String,
        @RequestParam("type") groupType: String,
        @RequestParam("image") groupImg: MultipartFile,
    ) = backstageServiceImpl.rootEditGroupInfo(groupName, groupIntroduce, groupType, groupImg)


    @GetMapping("/query-app-config")
    override fun rootGetAppConfig() = backstageServiceImpl.rootGetAppConfig()

    @PostMapping("/edit-config")
    override fun rootEditAppConfig(@RequestBody(required = false) appConfigEdit: AppConfigEdit?) =
        backstageServiceImpl.rootEditAppConfig(appConfigEdit)
}