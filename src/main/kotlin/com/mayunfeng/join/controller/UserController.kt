package com.mayunfeng.join.controller

import com.mayunfeng.join.model.MyfUserTableModel
import com.mayunfeng.join.service.impl.UserServiceImpl
import com.mayunfeng.join.utils.*
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Autowired


@RestController
@RequestMapping("/myf-api")
@CrossOrigin // 跨域
class UserController : BaseController() {


    @Autowired
    private lateinit var userServiceImpl: UserServiceImpl

    // 用户登录/注册
    @PostMapping("/login-user")
    @ResponseBody
    fun userLogin(
        @RequestParam("user_name", required = false) name: String?,
        @RequestParam("user_pws", required = false) pws: String?
    ): JsonResult<MyfUserTableModel> = userServiceImpl.login(name, pws)


}