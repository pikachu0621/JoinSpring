package com.mayunfeng.join.service.impl

import com.mayunfeng.join.base.BaseServiceImpl
import com.mayunfeng.join.mapper.UserTableMapper
import com.mayunfeng.join.model.UserTable
import com.mayunfeng.join.service.*
import com.mayunfeng.join.utils.JsonResult
import com.mayunfeng.join.utils.OtherUtils
import com.mayunfeng.join.utils.SqlUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.annotation.Resource

@Service
class UserServiceImpl: BaseServiceImpl(), IUserService {

    @Value("\${config.user.default-pic.name}")
    private lateinit var defaultPicName: String
    @Value("\${config.token.time}")
    private  var tokenTime: Long = -1
    @Value("\${config.user.max-length}")
    private  var maxLength: Int = 12
    @Value("\${config.user.min-length}")
    private  var minLength: Int = 6

    @Resource
    private lateinit var userTableMapper: UserTableMapper

    @Autowired
    private lateinit var tokenServiceImpl: TokenServiceImpl



    override fun login(userAccount: String?, userPassword: String?): JsonResult<UserTable> {

        logi("[$userAccount]   ----  [$userPassword]")

        if (OtherUtils.isFieldEmpty(userAccount, userPassword))  throw ParameterException()
        userAccount!!
        userPassword!!
        if (OtherUtils.isFieldIllegal(userAccount, userPassword))  throw ParameterIllegalException()

        var userDate = SqlUtils.queryByFieldOne(userTableMapper, "user_account", userAccount)
        if (userDate == null){
            if (userAccount.length > maxLength || userAccount.length < minLength) throw UserAccountLengthException()
            if (userPassword.length > maxLength || userPassword.length < minLength) throw UserPasswordLengthException()
            // 注册
            userTableMapper.insert(UserTable(userAccount, userPassword, defaultPicName, userName = "default-$userAccount"))
        }
        // 登录
        userDate = SqlUtils.queryByFieldOne(userTableMapper, "user_account", userAccount)

        if (userDate!!.userPassword != userPassword) throw UserPasswordException()
        if (userDate.userLimit) throw UserBlacklistException()

        val put = tokenServiceImpl.put(userDate.id, userAccount, userPassword, tokenTime)
        userDate.loginToken = put.tokenLogin
        userDate.userPassword = ""
        userDate.userImg = "/myf-pic-api/${userDate.userImg}"
        return JsonResult.ok(userDate)
    }



    override fun userInfoByToken(tokenLogin: String): JsonResult<UserTable> {
        val tokenBean = tokenServiceImpl.queryByToken(tokenLogin)!!
        val userTableModel = userTableMapper.selectById(tokenBean.userId)
        userTableModel.userPassword = ""
        userTableModel.loginToken = ""
        userTableModel.userImg = "/myf-pic-api/${userTableModel.userImg}"
        return JsonResult.ok(userTableModel)
    }


}