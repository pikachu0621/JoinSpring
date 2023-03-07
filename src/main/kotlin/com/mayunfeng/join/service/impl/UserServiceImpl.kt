package com.mayunfeng.join.service.impl

import com.mayunfeng.join.mapper.UserTableMapper
import com.mayunfeng.join.model.TokenTableModel
import com.mayunfeng.join.model.UserTableModel
import com.mayunfeng.join.service.*
import com.mayunfeng.join.utils.JsonResult
import com.mayunfeng.join.utils.OtherUtils
import com.mayunfeng.join.utils.SqlUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.annotation.Resource

@Service
class UserServiceImpl: BaseServiceImpl(), IUserService {

    @Resource
    private lateinit var userTableMapper: UserTableMapper

    @Resource
    private lateinit var tokenServiceImpl: TokenServiceImpl


    @Value("\${config.token.time}")
    private var tokenTime: Long = -1



    /**
     * 用户登录 / 注册
     */
    override fun login(userAccount: String?, userPassword: String?): JsonResult<UserTableModel> {

        if (OtherUtils.isFieldEmpty(userAccount, userPassword))  throw ParameterException()
        userAccount!!
        userPassword!!
        if (OtherUtils.isFieldIllegal(userAccount, userPassword))  throw ParameterIllegalException()

        var userDate = SqlUtils.queryByFieldOne(userTableMapper, "user_account", userAccount)
        if (userDate == null){
            // 注册
            userTableMapper.insert(UserTableModel(userAccount, userPassword))
        }
        // 登录
        userDate = SqlUtils.queryByFieldOne(userTableMapper, "user_account", userAccount)
        if (userDate!!.userPassword != userPassword) throw UserPasswordException()
        if (userDate.userLimit) throw UserBlacklistException()

        val put = tokenServiceImpl.put(userDate.id, userAccount, userPassword, tokenTime)
        userDate.loginToken = put.tokenLogin

        logi(put.tokenLogin)
        return JsonResult.ok(userDate)
    }


}