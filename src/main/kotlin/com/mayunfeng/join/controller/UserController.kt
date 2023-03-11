package com.mayunfeng.join.controller

import com.mayunfeng.join.base.BaseController
import com.mayunfeng.join.interceptor.TOKEN_PARAMETER
import com.mayunfeng.join.model.UserTable
import com.mayunfeng.join.service.IUserService
import com.mayunfeng.join.service.impl.UserServiceImpl
import com.mayunfeng.join.utils.*
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Autowired


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


    @GetMapping("/user-info-token")
    override fun userInfoByToken(
        @RequestParam(TOKEN_PARAMETER, required = false) tokenLogin: String
    ): JsonResult<UserTable> =
        userServiceImpl.userInfoByToken(tokenLogin)



}