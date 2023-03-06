package com.mayunfeng.join.service.impl

import com.mayunfeng.join.mapper.MyfUserTableMapper
import com.mayunfeng.join.model.MyfUserTableModel
import com.mayunfeng.join.service.IUserService
import com.mayunfeng.join.service.ParameterException
import com.mayunfeng.join.service.UsernameDuplicateException
import com.mayunfeng.join.utils.JsonResult
import org.springframework.stereotype.Service
import javax.annotation.Resource

@Service
class UserServiceImpl: BaseServiceImpl(), IUserService {

    @Resource
    private lateinit var userMapper: MyfUserTableMapper


    /**
     * 用户登录 / 注册
     */
    override fun login(userName: String?, userPws: String?): JsonResult<MyfUserTableModel> {
        // 逻辑
        if (JsonResult.isFieldEmpty(userName, userPws)) {
            throw ParameterException()
        }

        if (userPws != "123456") {
            throw UsernameDuplicateException()
        }

        return JsonResult.ok(null)
    }


}