package com.mayunfeng.join.controller

import com.mayunfeng.join.base.BaseController
import com.mayunfeng.join.model.GroupTable
import com.mayunfeng.join.model.UserTable
import com.mayunfeng.join.service.IBackstageService
import com.mayunfeng.join.service.impl.BackstageServiceImpl
import com.mayunfeng.join.utils.JsonResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/myf-bg-api")
@CrossOrigin
class BackstageController : BaseController(),  IBackstageService {

    @Autowired
    private lateinit var backstageServiceImpl: BackstageServiceImpl

    @PostMapping("/login")
    @ResponseBody
    override fun loginRoot(
        @RequestParam("account") rootAccount: String,
        @RequestParam("password") rootPassword: String
    ): JsonResult<UserTable> =
        backstageServiceImpl.loginRoot(rootAccount, rootPassword)


    @GetMapping("/info")
    override fun verifyToken(@RequestParam("token") token: String): JsonResult<UserTable> =
        backstageServiceImpl.verifyToken(token)

    @GetMapping("/all-user")
    override fun getAllUser(): JsonResult<Array<UserTable>> = backstageServiceImpl.getAllUser()

    @GetMapping("/all-group")
    override fun getAllGroup(): JsonResult<Array<GroupTable>> = backstageServiceImpl.getAllGroup()

    @GetMapping("/del-group/{groupId}", "/del-group/**")
    override fun delGroupByGroupId(@PathVariable("groupId") groupId: Long): JsonResult<Boolean> =
        backstageServiceImpl.delGroupByGroupId(groupId)

    @GetMapping("/del-user/{userId}", "/del-user/**")
    override fun delUserByUserId(@PathVariable("userId") userId: Long): JsonResult<Boolean> =
        backstageServiceImpl.delUserByUserId(userId)


    @PostMapping("/edit-user")
    @ResponseBody
    override fun rootEditUserInfo(
        @RequestParam("user_id")  userId: Long,
        @RequestParam("password", required = false) userPassword: String?,
        @RequestParam("sex", required = false) userSex: Boolean?,
        @RequestParam("name", required = false) userName: String?,
        @RequestParam("unit", required = false) userUnit: String?,
        @RequestParam("birth", required = false) userBirth: String?,
        @RequestParam("introduce", required = false) userIntroduce: String?,
        @RequestParam("grade", required = false) userGrade: Int?,
        @RequestParam("limit", required = false) userLimit: Boolean?,
        @RequestParam("image", required = false) userImage: MultipartFile?
    ): JsonResult<Boolean> = backstageServiceImpl.rootEditUserInfo(
        userId,
        userPassword,
        userSex,
        userName,
        userUnit,
        userBirth,
        userIntroduce,
        userGrade,
        userLimit,
        userImage
    )

    @PostMapping("/edit-group")
    @ResponseBody
    override fun rootEditGroupInfo(
        @RequestParam("name") groupName: String,
        @RequestParam("introduce") groupIntroduce: String,
        @RequestParam("type") groupType: String,
        @RequestParam("image") groupImg: MultipartFile
    ): JsonResult<Boolean> = backstageServiceImpl.rootEditGroupInfo(groupName, groupIntroduce, groupType, groupImg)
}