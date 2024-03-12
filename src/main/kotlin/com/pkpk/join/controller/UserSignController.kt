package com.pkpk.join.controller

import com.pkpk.join.base.BaseController
import com.pkpk.join.config.API_USER_SIGN
import com.pkpk.join.service.IUserSignService
import com.pkpk.join.service.impl.UserSignServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping(API_USER_SIGN)
@CrossOrigin // 跨域
class UserSignController : BaseController(), IUserSignService {


    @Autowired
    private lateinit var userSignServiceImpl: UserSignServiceImpl


    @GetMapping("/start-sign")
    override fun startUserSign(
        @RequestParam("sign-id") signId: Long,
        @RequestParam("key", required = false) key: String?
    ) =  userSignServiceImpl.startUserSign(signId, key)


    @GetMapping("/all-info/{signId}", "/all-info")
    override fun queryAllBySignId(@PathVariable("signId") signId: Long) = userSignServiceImpl.queryAllBySignId(signId)

    @GetMapping("/my-sign-info")
    override fun queryUserSign() =  userSignServiceImpl.queryUserSign()

    @GetMapping("/my-sign-all-info")
    override fun queryUserAllSign() =  userSignServiceImpl.queryUserAllSign()


}