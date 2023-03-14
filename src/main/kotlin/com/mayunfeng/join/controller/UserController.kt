package com.mayunfeng.join.controller

import com.mayunfeng.join.base.BaseController
import com.mayunfeng.join.model.UserTable
import com.mayunfeng.join.service.IUserService
import com.mayunfeng.join.service.impl.UserServiceImpl
import com.mayunfeng.join.utils.*
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/myf-user-api")
@CrossOrigin // 跨域
class UserController : BaseController(), IUserService {


    @Autowired
    private lateinit var userServiceImpl: UserServiceImpl




    @PostMapping("/login-user")
    @ResponseBody
    override fun login(
        @RequestParam("account", required = false) userAccount: String?,
        @RequestParam("password", required = false) userPassword: String?
    ): JsonResult<UserTable> = userServiceImpl.login(userAccount, userPassword)




    @GetMapping("/user-info")
    override fun userInfoByToken(): JsonResult<UserTable> =
        userServiceImpl.userInfoByToken()




    @PostMapping("/edit-img")
    @ResponseBody
    override fun editImage(
        @RequestParam("img", required = false) userImage: MultipartFile?
    ): JsonResult<UserTable> = userServiceImpl.editImage(userImage)




    @GetMapping("/edit-name")
    override fun editName(
        @RequestParam("name", required = false) userName: String?
    ): JsonResult<UserTable> = userServiceImpl.editName(userName)




    @GetMapping("/edit-sex")
    override fun editSex(
        @RequestParam("sex", required = false) userSex: Boolean?
    ): JsonResult<UserTable> = userServiceImpl.editSex(userSex)




    @GetMapping("/edit-birth")
    override fun editBirth(
        @RequestParam("birth", required = false) userBirth: String?
    ): JsonResult<UserTable> = userServiceImpl.editBirth(userBirth)




    @GetMapping("/edit-ird")
    override fun editIntroduce(
        @RequestParam("ird", required = false) userIntroduce: String?
    ): JsonResult<UserTable> = userServiceImpl.editIntroduce(userIntroduce)




    @GetMapping("/edit-unit")
    override fun editUnit(
        @RequestParam("unit", required = false) userUnit: String?
    ): JsonResult<UserTable> = userServiceImpl.editUnit(userUnit)




    @PostMapping("/edit-password")
    @ResponseBody
    override fun editPassword(
        @RequestParam("old-password", required = false) userOldPassword: String?,
        @RequestParam("new-password", required = false) userNewPassword: String?,
    ): JsonResult<UserTable> = userServiceImpl.editPassword(userOldPassword, userNewPassword)

}