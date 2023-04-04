package com.mayunfeng.join.controller

import com.mayunfeng.join.base.BaseController
import com.mayunfeng.join.model.UserSignAndStartSign
import com.mayunfeng.join.model.UserSignTable
import com.mayunfeng.join.service.IUserSignService
import com.mayunfeng.join.service.impl.UserSignServiceImpl
import com.mayunfeng.join.utils.JsonResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/myf-user-sign-api")
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




}