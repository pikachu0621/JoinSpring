package com.mayunfeng.join.controller

import com.mayunfeng.join.base.BaseController
import com.mayunfeng.join.model.UserSignAndStartSign
import com.mayunfeng.join.model.UserSignTable
import com.mayunfeng.join.service.IUserService
import com.mayunfeng.join.service.IUserSignService
import com.mayunfeng.join.utils.JsonResult
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/myf-user-sign")
@CrossOrigin // 跨域
class UserSignController: BaseController(), IUserSignService {




    override fun startUserSign(signId: Long, key: String?): JsonResult<Boolean> {

        return JsonResult.ok(false)
    }

    override fun queryAllBySignId(signId: Long): JsonResult<UserSignAndStartSign> {
        TODO("Not yet implemented")
    }

    override fun queryUserSign(): JsonResult<ArrayList<UserSignTable>> {
        TODO("Not yet implemented")
    }


}