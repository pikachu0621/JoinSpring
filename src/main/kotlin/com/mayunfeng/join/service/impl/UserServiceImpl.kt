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


    override fun login(userAccount: String?, userPassword: String?): JsonResult<UserTableModel> {

        logi("[$userAccount]   ----  [$userPassword]")

        if (OtherUtils.isFieldEmpty(userAccount, userPassword))  throw ParameterException()
        userAccount!!
        userPassword!!
        if (OtherUtils.isFieldIllegal(userAccount, userPassword))  throw ParameterIllegalException()

        var userDate = SqlUtils.queryByFieldOne(userTableMapper, "user_account", userAccount)
        if (userDate == null){

            if (userAccount.length > 12 || userAccount.length < 6) throw UserAccountLengthException()

            if (userPassword.length > 12 || userPassword.length < 6) throw UserPasswordLengthException()

            // 注册
            userTableMapper.insert(UserTableModel(userAccount, userPassword))
        }
        // 登录
        userDate = SqlUtils.queryByFieldOne(userTableMapper, "user_account", userAccount)

        if (userDate!!.userPassword != userPassword) throw UserPasswordException()

        if (userDate.userLimit) throw UserBlacklistException()

        val put = tokenServiceImpl.put(userDate.id, userAccount, userPassword, tokenTime)
        userDate.loginToken = put.tokenLogin

        userDate.userPassword = ""
        return JsonResult.ok(userDate)
    }



    override fun userInfoByToken(tokenLogin: String): JsonResult<UserTableModel> {
        val tokenBean = tokenServiceImpl.queryByToken(tokenLogin)!!
        val userTableModel = userTableMapper.selectById(tokenBean.userId)
        userTableModel.userPassword = ""
        userTableModel.loginToken = ""
        return JsonResult.ok(userTableModel)
    }


}