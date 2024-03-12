package com.pkpk.join.controller

import com.pkpk.join.base.BaseController
import com.pkpk.join.config.API_USER
import com.pkpk.join.service.IUserService
import com.pkpk.join.service.impl.UserServiceImpl
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping(API_USER)
@CrossOrigin // 跨域
class UserController : BaseController(), IUserService {


    @Autowired
    private lateinit var userServiceImpl: UserServiceImpl


    @PostMapping("/login-user")
    @ResponseBody
    override fun login(
        @RequestParam("account", required = false) userAccount: String?,
        @RequestParam("password", required = false) userPassword: String?
    ) =  userServiceImpl.login(userAccount, userPassword)


    @GetMapping("/user-info")
    override fun userInfoByToken() = userServiceImpl.userInfoByToken()


    @GetMapping("/user-img", "/user-img/*", produces = [MediaType.IMAGE_PNG_VALUE])
    override fun userImage(
        @RequestParam("c", required = false) c: String?,
        @RequestParam("uid", required = false) userId: Long?
    ) = userServiceImpl.userImage(c, userId)


    @PostMapping("/edit-img")
    @ResponseBody
    override fun editImage(
        @RequestParam("img", required = false) userImage: MultipartFile?
    ) =  userServiceImpl.editImage(userImage)


    @GetMapping("/edit-nickname")
    override fun editNickName(
        @RequestParam("nickname", required = false) userNickName: String?
    ) =  userServiceImpl.editNickName(userNickName)


    @GetMapping("/edit-sex")
    override fun editSex(
        @RequestParam("sex", required = false) userSex: Boolean?
    ) =  userServiceImpl.editSex(userSex)


    @GetMapping("/edit-birth")
    override fun editBirth(
        @RequestParam("birth", required = false) userBirth: String?
    ) =  userServiceImpl.editBirth(userBirth)


    @GetMapping("/edit-ird")
    override fun editIntroduce(
        @RequestParam("ird", required = false) userIntroduce: String?
    ) =  userServiceImpl.editIntroduce(userIntroduce)


    @GetMapping("/edit-unit")
    override fun editUnit(
        @RequestParam("unit", required = false) userUnit: String?
    ) =  userServiceImpl.editUnit(userUnit)


    @PostMapping("/edit-password")
    @ResponseBody
    override fun editPassword(
        @RequestParam("old-password", required = false) userOldPassword: String?,
        @RequestParam("new-password", required = false) userNewPassword: String?,
    ) =  userServiceImpl.editPassword(userOldPassword, userNewPassword)

    @GetMapping("/edit-open")
    override fun editOpen(@RequestParam("open", required = false) isOpen: Boolean?) =  userServiceImpl.editOpen()

    @GetMapping("/out-login")
    override fun outLogin() =  userServiceImpl.outLogin()

}