package com.pkpk.join.service.impl

import com.pkpk.join.base.BaseServiceImpl
import com.pkpk.join.config.AppConfig
import com.pkpk.join.config.TOKEN_PARAMETER
import com.pkpk.join.handler.UserWebSocketHandler
import com.pkpk.join.mapper.UserTableMapper
import com.pkpk.join.model.UserRank
import com.pkpk.join.model.UserTable
import com.pkpk.join.service.*
import com.pkpk.join.utils.JsonResult
import com.pkpk.join.utils.OtherUtils
import com.pkpk.join.utils.SqlUtils.delImageFile
import com.pkpk.join.utils.SqlUtils.queryByFieldOne
import com.pkpk.join.utils.TimeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.socket.CloseStatus
import java.awt.image.BufferedImage
import java.text.SimpleDateFormat
import java.util.*
import javax.imageio.ImageIO
import javax.servlet.http.HttpServletRequest

@Service
class UserServiceImpl : BaseServiceImpl(), IUserService {

    @Autowired
    private lateinit var userTableMapper: UserTableMapper

    @Autowired
    private lateinit var tokenServiceImpl: TokenServiceImpl

    @Autowired
    private lateinit var pictureServiceImpl: PictureServiceImpl

    @Autowired
    private lateinit var userWebSocketHandler: UserWebSocketHandler

    @Autowired
    private lateinit var userLogServiceImpl: UserLogServiceImpl

    @Autowired
    private lateinit var appConfig: AppConfig

    @Autowired
    private lateinit var request: HttpServletRequest

    @Autowired
    private lateinit var resourceLoader: ResourceLoader


    override fun login(userAccount: String?, userPassword: String?): JsonResult<UserTable> {
        if (OtherUtils.isFieldEmpty(userAccount, userPassword)) throw ParameterException()
        userAccount!!
        userPassword!!
        if (OtherUtils.isFieldIllegal(userAccount, userPassword)) throw ParameterIllegalException()
        var userData = userTableMapper.queryByFieldOne(UserTable::userAccount, userAccount)
        // 注册
        if (userData == null) {
            if (userAccount.length > appConfig.appConfigEdit.userAccountLengthLimit[1] || userAccount.length < appConfig.appConfigEdit.userAccountLengthLimit[0]) throw UserAccountLengthException()
            if (userPassword.length > appConfig.appConfigEdit.userAccountLengthLimit[1] || userPassword.length < appConfig.appConfigEdit.userAccountLengthLimit[0]) throw UserPasswordLengthException()
            // 注册
            userTableMapper.insert(
                UserTable(
                    userAccount,
                    userPassword,
                    appConfig.configDefaultPicName
                )
            )
        }
        // 登录
        userData = userTableMapper.queryByFieldOne(UserTable::userAccount, userAccount)
        // 验证长度
        if (userData!!.userPassword != userPassword) throw UserPasswordException()
        if (userData.userLimit) throw UserBlacklistException()
        // 生成token
        val put = tokenServiceImpl.put(userData.id, userAccount, userPassword, appConfig.appConfigEdit.tokenTime)
        userData.loginToken = put.tokenLogin
        // 返回的数据
        // 断开失效的ws
        userWebSocketHandler.disLinkUnboundToken()
        userLogServiceImpl.addLogLogin("用户登录" )
        return JsonResult.ok(disposeReturnUserData(userData))
    }

    override fun registeredRoot(userAccount: String, userPassword: String) {
        val userData = userTableMapper.queryByFieldOne(UserTable::userAccount, userAccount)
        if (userData == null) {
            userTableMapper.insert(UserTable(userAccount, userPassword, appConfig.configDefaultPicName, userGrade = UserRank.ROOT.LV))
        }
    }


    override fun userInfoByToken(): JsonResult<UserTable> {
        val tokenBean = tokenServiceImpl.queryByToken(OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!)!!
        val userData = userTableMapper.selectById(tokenBean.userId)
        if (userData.userLimit) throw UserBlacklistException()
        return JsonResult.ok(disposeReturnUserData(userData))
    }

    override fun userInfoById(userId: Long): UserTable {
        val userData = userTableMapper.selectById(userId)
        return disposeReturnUserData(userData, true)
    }


    override fun userImage(c: String?, userId: Long?): BufferedImage {
        var userBen: UserTable?
        val tokenBen = tokenServiceImpl.queryByToken(OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!)
        userBen = if (userId != null && userId != -1L) {
            userTableMapper.selectById(userId)
        } else {
            userTableMapper.selectById(tokenBen!!.userId)
        }
        if (userBen == null) {
            userBen = userTableMapper.selectById(tokenBen!!.userId)
        }
        return if (userBen!!.userImg == appConfig.configDefaultPicName) {
            if (userBen.userSex!!)
                ImageIO.read(resourceLoader.getResource(appConfig.configDefaultPicBoy).inputStream)
            else
                ImageIO.read(resourceLoader.getResource(appConfig.configDefaultPicGirl).inputStream)
        } else {
            pictureServiceImpl.requestImage(userBen.userImg, c)
        }
    }


    // 上传图片
    override fun editImage(
        userImage: MultipartFile?
    ): JsonResult<UserTable> =
        JsonResult.ok(
            disposeReturnUserData(
                editOneSqlValue(
                    OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!,
                    arrayOf(userImage)
                ) {
                    // 删除只有本人绑定的图片数据
                    userTableMapper.delImageFile(
                        UserTable::userImg,
                        it.userImg,
                        "${appConfig.configUserImageFilePath()}${it.userImg}"
                    )
                    it.userImg = pictureServiceImpl.upImage(userImage).result!!
                })
        )


    override fun editNickName(userNickName: String?): JsonResult<UserTable> =
        JsonResult.ok(
            disposeReturnUserData(
                editOneSqlValue(
                    OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!,
                    arrayOf(userNickName)
                ) {
                    it.userNickname = userNickName
                })
        )


    override fun editSex(userSex: Boolean?): JsonResult<UserTable> =
        JsonResult.ok(
            disposeReturnUserData(
                editOneSqlValue(
                    OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!,
                    arrayOf(userSex)
                ) {
                    it.userSex = userSex
                })
        )


    override fun editBirth(userBirth: String?): JsonResult<UserTable> =
        JsonResult.ok(
            disposeReturnUserData(
                editOneSqlValue(
                    OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!,
                    arrayOf(userBirth)
                ) {
                    try {
                        SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).parse(userBirth!!)
                    } catch (e: Exception) {
                        throw DateTimeException()
                    }
                    it.userBirth = userBirth
                })
        )


    override fun editIntroduce(userIntroduce: String?): JsonResult<UserTable> =
        JsonResult.ok(
            disposeReturnUserData(
                editOneSqlValue(
                    OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!,
                    arrayOf(userIntroduce)
                ) {
                    it.userIntroduce = userIntroduce
                })
        )


    override fun editUnit(userUnit: String?): JsonResult<UserTable> =
        JsonResult.ok(
            disposeReturnUserData(
                editOneSqlValue(
                    OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!,
                    arrayOf(userUnit)
                ) {
                    it.userUnit = userUnit
                })
        )


    override fun editPassword(
        userOldPassword: String?,
        userNewPassword: String?
    ): JsonResult<UserTable> {
        val tokenLogin = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        return JsonResult.ok(
            disposeReturnUserData(
                editOneSqlValue(
                    tokenLogin,
                    arrayOf(userOldPassword, userNewPassword)
                ) {
                    if (it.userPassword != userOldPassword!!) throw UserOldPasswordException()
                    if (userNewPassword!!.length > appConfig.appConfigEdit.userAccountLengthLimit[1] || userNewPassword.length < appConfig.appConfigEdit.userAccountLengthLimit[0]) throw UserPasswordLengthException()
                    if (it.userPassword == userNewPassword) throw UserEquallyPasswordException()
                    if (OtherUtils.isFieldIllegal(userNewPassword)) throw ParameterIllegalException()
                    it.userPassword = userNewPassword
                    tokenServiceImpl.deleteByToken(tokenLogin)
                    // 断开失效的ws
                    userWebSocketHandler.disLinkUnboundToken(CloseStatus.PROTOCOL_ERROR)
                })
        )
    }

    override fun editOpen(isOpen: Boolean?): JsonResult<UserTable> = JsonResult.ok(
        disposeReturnUserData(
            editOneSqlValue(
                OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
            ) {
                it.userOpenProfile = isOpen ?: !it.userOpenProfile
            })
    )

    override fun outLogin(): JsonResult<Boolean> {
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)
        return JsonResult.ok(
            if (token.isNullOrEmpty()) {
                false
            } else {
                tokenServiceImpl.deleteByToken(token)
                true
            }
        )
    }


    /**
     * 过滤/处理 数据
     *
     * @param isAddUserId 是否添加 userid  用在不是当前用户查询别的用户头像
     *
     */
    fun disposeReturnUserData(userData: UserTable, isAddUserId: Boolean = false): UserTable {
        // 限制时间
        // 可以不加 userData.userImg 直接根据 用户token 获取   但是防止前端缓存 要加上
        if (userData.userImg == appConfig.configDefaultPicName) {
            userData.userImg = appConfig.configDefaultPicName +
                    if (userData.userSex!!) "_boy"
                    else appConfig.configDefaultPicName + "_girl"
        }
        userData.userImg = "/pk-user-api/user-img/${userData.userImg}${
            if (appConfig.appConfigEdit.imageTime != -1L) {
                val createTimeAESBCB = OtherUtils.createTimeAESBCB(appConfig.appConfigEdit.tokenSalt)
                "?c=$createTimeAESBCB"
            } else ""
        }${
            if (appConfig.appConfigEdit.imageTime != -1L) if (isAddUserId) "&uid=${userData.id}" else ""
            else if (isAddUserId) "?uid=${userData.id}" else ""
        }"
        userData.userAge = TimeUtils.getDateDistanceYear(
            userData.userBirth,
            TimeUtils.getCurrentTime("yyyy-MM-dd")
        )
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)
        if (token.isNullOrEmpty()) return userData
        var userId = tokenServiceImpl.queryByToken(token)!!.userId
        if (userId < 0) userId = -userId
        val loginUser = userTableMapper.selectById(userId)
        if (userData.userOpenProfile    // 用户是否开放资料
            && userData.id != userId    // 请求用户是不是自己
            && loginUser.userGrade <= UserRank.NORMAL.LV
        ) { // 请求用户是否为管理员
            userData.userSex = null
            userData.userUnit = null
            userData.userBirth = ""
            userData.userAge = null
            userData.userIntroduce = null
            userData.userGrade = UserRank.NORMAL.LV
            userData.userLimit = false
            userData.userPassword = ""
            userData.loginToken = null
        }
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

