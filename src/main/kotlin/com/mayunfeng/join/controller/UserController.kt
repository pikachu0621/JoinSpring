package com.mayunfeng.join.controller

import com.mayunfeng.join.model.UserTableModel
import com.mayunfeng.join.service.impl.TokenServiceImpl
import com.mayunfeng.join.service.impl.UserServiceImpl
import com.mayunfeng.join.utils.*
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.Resource


@RestController
@RequestMapping("/myf-api")
@CrossOrigin // 跨域
class UserController : BaseController() {


    @Autowired
    private lateinit var userServiceImpl: UserServiceImpl

    @Autowired
    private lateinit var tokenServiceImpl: TokenServiceImpl



    // 用户登录/注册
    @PostMapping("/login-user")
    @ResponseBody
    fun userLogin(
        @RequestParam("account", required = false) account: String?,
        @RequestParam("password", required = false) password: String?
    ): JsonResult<UserTableModel> = userServiceImpl.login(account, password)


    @GetMapping("/test")
    fun test(): Boolean{
        // 504421d1f969682397229460991aed02
        return tokenServiceImpl.verify("504421d1f969682397229460991aed02")
    }


}