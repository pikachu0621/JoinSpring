package com.mayunfeng.join.controller

import com.mayunfeng.join.base.BaseController
import com.mayunfeng.join.interceptor.TOKEN_PARAMETER
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
    override fun userInfoByToken(
        @RequestParam(TOKEN_PARAMETER) tokenLogin: String
    ): JsonResult<UserTable> =
        userServiceImpl.userInfoByToken(tokenLogin)




    @PostMapping("/edit-img")
    @ResponseBody
    override fun editImage(@RequestParam(TOKEN_PARAMETER) tokenLogin: String,
                           @RequestParam("img", required = false) userImage: MultipartFile?
    ): JsonResult<UserTable> = userServiceImpl.editImage(tokenLogin, userImage)




    @GetMapping("/edit-name")
    override fun editName(@RequestParam(TOKEN_PARAMETER) tokenLogin: String,
                          @RequestParam("name", required = false) userName: String?
    ): JsonResult<UserTable> = userServiceImpl.editName(tokenLogin , userName)




    @GetMapping("/edit-sex")
    override fun editSex(
        @RequestParam(TOKEN_PARAMETER) tokenLogin: String,
        @RequestParam("sex", required = false) userSex: Boolean?
    ): JsonResult<UserTable> = userServiceImpl.editSex(tokenLogin , userSex)




    @GetMapping("/edit-birth")
    override fun editBirth(@RequestParam(TOKEN_PARAMETER) tokenLogin: String,
                           @RequestParam("birth", required = false)  userBirth: String?
    ): JsonResult<UserTable> = userServiceImpl.editBirth(tokenLogin , userBirth)




    @GetMapping("/edit-ird")
    override fun editIntroduce(@RequestParam(TOKEN_PARAMETER) tokenLogin: String,
                               @RequestParam("ird", required = false) userIntroduce: String?
    ): JsonResult<UserTable> = userServiceImpl.editIntroduce(tokenLogin, userIntroduce)




    @GetMapping("/edit-unit")
    override fun editUnit(@RequestParam(TOKEN_PARAMETER) tokenLogin: String,
                          @RequestParam("unit", required = false) userUnit: String?
    ): JsonResult<UserTable> = userServiceImpl.editUnit(tokenLogin, userUnit)




    @PostMapping("/edit-password")
    @ResponseBody
    override fun editPassword(
        @RequestParam(TOKEN_PARAMETER) tokenLogin: String,
        @RequestParam("old-password", required = false) userOldPassword: String?,
        @RequestParam("new-password", required = false) userNewPassword: String?,
    ): JsonResult<UserTable> = userServiceImpl.editPassword(tokenLogin, userOldPassword, userNewPassword)

}