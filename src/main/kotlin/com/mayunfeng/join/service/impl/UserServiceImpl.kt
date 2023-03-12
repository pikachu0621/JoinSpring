package com.mayunfeng.join.service.impl

import com.mayunfeng.join.base.BaseServiceImpl
import com.mayunfeng.join.mapper.UserTableMapper
import com.mayunfeng.join.model.UserTable
import com.mayunfeng.join.service.*
import com.mayunfeng.join.utils.JsonResult
import com.mayunfeng.join.utils.OtherUtils
import com.mayunfeng.join.utils.SqlUtils
import com.mayunfeng.join.utils.TimeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.text.SimpleDateFormat
import java.util.*
import javax.annotation.Resource
@Service
class UserServiceImpl : BaseServiceImpl(), IUserService {

    @Value("\${config.user.default-pic.name}")
    private lateinit var defaultPicName: String

    @Value("\${config.token.time}")
    private var tokenTime: Long = -1

    @Value("\${config.user.max-length}")
    private var maxLength: Int = 12

    @Value("\${config.user.min-length}")
    private var minLength: Int = 6

    @Resource
    private lateinit var userTableMapper: UserTableMapper

    @Autowired
    private lateinit var tokenServiceImpl: TokenServiceImpl


    override fun login(userAccount: String?, userPassword: String?): JsonResult<UserTable> {
        logi("[$userAccount]   ----  [$userPassword]")
        if (OtherUtils.isFieldEmpty(userAccount, userPassword)) throw ParameterException()
        userAccount!!
        userPassword!!
        if (OtherUtils.isFieldIllegal(userAccount, userPassword)) throw ParameterIllegalException()
        var userData = SqlUtils.queryByFieldOne(userTableMapper, "user_account", userAccount)
        // 注册
        if (userData == null) {
            if (userAccount.length > maxLength || userAccount.length < minLength) throw UserAccountLengthException()
            if (userPassword.length > maxLength || userPassword.length < minLength) throw UserPasswordLengthException()
            // 注册
            userTableMapper.insert(
                UserTable(
                    userAccount,
                    userPassword,
                    defaultPicName,
                    userName = "default-$userAccount"
                )
            )
        }
        // 登录
        userData = SqlUtils.queryByFieldOne(userTableMapper, "user_account", userAccount)
        // 验证长度
        if (userData!!.userPassword != userPassword) throw UserPasswordException()
        if (userData.userLimit) throw UserBlacklistException()
        // 生成token
        val put = tokenServiceImpl.put(userData.id, userAccount, userPassword, tokenTime)
        userData.loginToken = put.tokenLogin
        // 返回的数据
        return JsonResult.ok(disposeReturnUserData(userData))
    }


    override fun userInfoByToken(tokenLogin: String): JsonResult<UserTable> {
        val tokenBean = tokenServiceImpl.queryByToken(tokenLogin)!!
        val userData = userTableMapper.selectById(tokenBean.userId)
        return JsonResult.ok(disposeReturnUserData(userData))
    }


    override fun editImage(
        tokenLogin: String,
        userImage: MultipartFile?
    ): JsonResult<UserTable> {
        // todo 上传图片

        return JsonResult.ok(UserTable())
    }


    override fun editName(tokenLogin: String, userName: String?): JsonResult<UserTable> {
       return JsonResult.ok(disposeReturnUserData(editOneSqlValue(tokenLogin, arrayOf(userName)){
            it.userName = userName
        }))
    }


    override fun editSex(tokenLogin: String, userSex: Boolean?): JsonResult<UserTable> =
        JsonResult.ok(disposeReturnUserData(editOneSqlValue(tokenLogin,  arrayOf(userSex)){
            it.userSex = userSex
        }))


    override fun editBirth(tokenLogin: String, userBirth: String?): JsonResult<UserTable> =
        JsonResult.ok(disposeReturnUserData(editOneSqlValue(tokenLogin, arrayOf(userBirth)){
            try {
                SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(userBirth!!)
            } catch (e: Exception) {
                throw DateTimeException()
            }
            it.userBirth = userBirth
        }))


    override fun editIntroduce(tokenLogin: String, userIntroduce: String?): JsonResult<UserTable> =
        JsonResult.ok(disposeReturnUserData(editOneSqlValue(tokenLogin, arrayOf(userIntroduce) ){
            it.userIntroduce = userIntroduce
        }))



    override fun editUnit(tokenLogin: String, userUnit: String?): JsonResult<UserTable> =
        JsonResult.ok(disposeReturnUserData(editOneSqlValue(tokenLogin, arrayOf(userUnit)){
            it.userIntroduce = userUnit
        }))



    override fun editPassword(
        tokenLogin: String,
        userOldPassword: String?,
        userNewPassword: String?
    ): JsonResult<UserTable> =
        JsonResult.ok(disposeReturnUserData(editOneSqlValue(tokenLogin, arrayOf(userOldPassword, userNewPassword)){
            if (it.userPassword != userOldPassword!!) throw UserOldPasswordException()
            if (userNewPassword!!.length > maxLength || userNewPassword.length < minLength) throw UserPasswordLengthException()
            it.userPassword = userNewPassword
        }))


    /**
     * 过滤/处理 数据
     */
    private fun disposeReturnUserData(userData: UserTable): UserTable {
        userData.userImg = "/myf-pic-api/${userData.userImg}"
        userData.userAge = TimeUtils.getDateDistanceYear(
            userData.userBirth,
            TimeUtils.getCurrentTime("yyyy-MM-dd")
        )
        return userData
    }


    /**
     * 修改数据
     */
    private fun editOneSqlValue(
        tokenLogin: String,
        v: Array<Any?> = arrayOf(),
        assignment: (userData: UserTable) -> Unit
    ): UserTable {
        v.forEach {
            if (it == null) throw ParameterException()
            if (it is String) if (it.replace(" ", "") == "") throw ParameterException()
        }
        val queryByToken = tokenServiceImpl.queryByToken(tokenLogin)
        val userData = userTableMapper.selectById(queryByToken!!.userId)
        assignment(userData)
        userTableMapper.updateById(userData)
        return userData
    }


}

