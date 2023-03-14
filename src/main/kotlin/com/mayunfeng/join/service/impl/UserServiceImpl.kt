package com.mayunfeng.join.service.impl

import com.mayunfeng.join.base.BaseServiceException
import com.mayunfeng.join.base.BaseServiceImpl
import com.mayunfeng.join.config.AppConfig
import com.mayunfeng.join.config.TOKEN_PARAMETER
import com.mayunfeng.join.mapper.UserTableMapper
import com.mayunfeng.join.model.UserTable
import com.mayunfeng.join.service.*
import com.mayunfeng.join.utils.JsonResult
import com.mayunfeng.join.utils.OtherUtils
import com.mayunfeng.join.utils.SqlUtils
import com.mayunfeng.join.utils.TimeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.text.SimpleDateFormat
import java.util.*
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

@Service
class UserServiceImpl : BaseServiceImpl(), IUserService {

    @Resource
    private lateinit var userTableMapper: UserTableMapper

    @Autowired
    private lateinit var tokenServiceImpl: TokenServiceImpl

    @Autowired
    private lateinit var pictureServiceImpl: PictureServiceImpl

    @Autowired
    private lateinit var APPConfig: AppConfig

    @Autowired
    private lateinit var request: HttpServletRequest

    override fun login(userAccount: String?, userPassword: String?): JsonResult<UserTable> {
        logi("[$userAccount]   ----  [$userPassword]")
        if (OtherUtils.isFieldEmpty(userAccount, userPassword)) throw ParameterException()
        userAccount!!
        userPassword!!
        if (OtherUtils.isFieldIllegal(userAccount, userPassword)) throw ParameterIllegalException()
        var userData = SqlUtils.queryByFieldOne(userTableMapper, "user_account", userAccount)
        // 注册
        if (userData == null) {
            if (userAccount.length > APPConfig.configMaxLength || userAccount.length < APPConfig.configMinLength) throw UserAccountLengthException()
            if (userPassword.length > APPConfig.configMaxLength || userPassword.length < APPConfig.configMinLength) throw UserPasswordLengthException()
            // 注册
            userTableMapper.insert(
                UserTable(
                    userAccount,
                    userPassword,
                    APPConfig.configDefaultPicName
                )
            )
        }
        // 登录
        userData = SqlUtils.queryByFieldOne(userTableMapper, "user_account", userAccount)
        // 验证长度
        if (userData!!.userPassword != userPassword) throw UserPasswordException()
        if (userData.userLimit) throw UserBlacklistException()
        // 生成token
        val put = tokenServiceImpl.put(userData.id, userAccount, userPassword, APPConfig.configTokenTime)
        userData.loginToken = put.tokenLogin
        // 返回的数据
        return JsonResult.ok(disposeReturnUserData(userData))
    }


    override fun userInfoByToken(): JsonResult<UserTable> {
        val tokenBean = tokenServiceImpl.queryByToken(request.getHeader(TOKEN_PARAMETER))!!
        val userData = userTableMapper.selectById(tokenBean.userId)
        return JsonResult.ok(disposeReturnUserData(userData))
    }


    override fun editImage(
        userImage: MultipartFile?
    ): JsonResult<UserTable> {
        // todo 上传图片
        pictureServiceImpl.upImage(userImage).error_code ?: throw BaseServiceException()


        return JsonResult.ok(UserTable())
    }


    override fun editName(userName: String?): JsonResult<UserTable> {
        return JsonResult.ok(
            disposeReturnUserData(
                editOneSqlValue(
                    request.getHeader(TOKEN_PARAMETER),
                    arrayOf(userName)
                ) {
                    it.userName = userName
                })
        )
    }


    override fun editSex(userSex: Boolean?): JsonResult<UserTable> =
        JsonResult.ok(disposeReturnUserData(editOneSqlValue(request.getHeader(TOKEN_PARAMETER), arrayOf(userSex)) {
            it.userSex = userSex
        }))


    override fun editBirth(userBirth: String?): JsonResult<UserTable> =
        JsonResult.ok(disposeReturnUserData(editOneSqlValue(request.getHeader(TOKEN_PARAMETER), arrayOf(userBirth)) {
            try {
                SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(userBirth!!)
            } catch (e: Exception) {
                throw DateTimeException()
            }
            it.userBirth = userBirth
        }))


    override fun editIntroduce(userIntroduce: String?): JsonResult<UserTable> =
        JsonResult.ok(
            disposeReturnUserData(
                editOneSqlValue(
                    request.getHeader(TOKEN_PARAMETER),
                    arrayOf(userIntroduce)
                ) {
                    it.userIntroduce = userIntroduce
                })
        )


    override fun editUnit(userUnit: String?): JsonResult<UserTable> =
        JsonResult.ok(disposeReturnUserData(editOneSqlValue(request.getHeader(TOKEN_PARAMETER), arrayOf(userUnit)) {
            it.userUnit = userUnit
        }))


    override fun editPassword(
        userOldPassword: String?,
        userNewPassword: String?
    ): JsonResult<UserTable> {
        val tokenLogin = request.getHeader(TOKEN_PARAMETER)
        return JsonResult.ok(
            disposeReturnUserData(
                editOneSqlValue(
                    request.getHeader(tokenLogin),
                    arrayOf(userOldPassword, userNewPassword)
                ) {
                    if (it.userPassword != userOldPassword!!) throw UserOldPasswordException()
                    if (userNewPassword!!.length > APPConfig.configMaxLength || userNewPassword.length < APPConfig.configMinLength) throw UserPasswordLengthException()
                    if (it.userPassword == userNewPassword) throw UserEquallyPasswordException()
                    it.userPassword = userNewPassword
                    tokenServiceImpl.deleteByToken(request.getHeader(tokenLogin))
                })
        )
    }


    /**
     * 过滤/处理 数据
     */
    private fun disposeReturnUserData(userData: UserTable): UserTable {
        // 限制时间
        userData.userImg = "/myf-pic-api/${
            if (APPConfig.configTokenTime != -1L) {
                val createTimeAESBCB = OtherUtils.createTimeAESBCB(APPConfig.configSalt)
                "${userData.userImg}?c=$createTimeAESBCB"
            } else {
                userData.userImg
            }
        }"
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

