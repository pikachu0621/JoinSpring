package com.pkpk.join.controller

import com.pkpk.join.base.BaseController
import com.pkpk.join.model.UserSignAndStartSign
import com.pkpk.join.model.UserSignTable
import com.pkpk.join.service.IUserSignService
import com.pkpk.join.service.impl.UserSignServiceImpl
import com.pkpk.join.utils.JsonResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/pk-user-sign-api")
@CrossOrigin // 跨域
class UserSignController : BaseController(), IUserSignService {


    @Autowired
    private lateinit var userSignServiceImpl: UserSignServiceImpl


    @GetMapping("/start-sign")
    override fun startUserSign(
        @RequestParam("sign-id") signId: Long,
        @RequestParam("key", required = false) key: String?
    ): JsonResult<Boolean> = userSignServiceImpl.startUserSign(signId, key)


    @GetMapping("/all-info/{signId}", "/all-info")
    override fun queryAllBySignId(@PathVariable("signId") signId: Long): JsonResult<UserSignAndStartSign> =
        userSignServiceImpl.queryAllBySignId(signId)

    @GetMapping("/my-sign-info")
    override fun queryUserSign(): JsonResult<Array<UserSignTable>> = userSignServiceImpl.queryUserSign()

    @GetMapping("/my-sign-all-info")
    override fun queryUserAllSign(): JsonResult<Array<UserSignTable>> = userSignServiceImpl.queryUserAllSign()


}