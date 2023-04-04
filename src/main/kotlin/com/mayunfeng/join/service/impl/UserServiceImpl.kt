package com.mayunfeng.join.service.impl

import com.mayunfeng.join.base.BaseServiceImpl
import com.mayunfeng.join.config.AppConfig
import com.mayunfeng.join.config.TOKEN_PARAMETER
import com.mayunfeng.join.handler.UserWebSocketHandler
import com.mayunfeng.join.mapper.UserTableMapper
import com.mayunfeng.join.model.UserTable
import com.mayunfeng.join.service.*
import com.mayunfeng.join.utils.JsonResult
import com.mayunfeng.join.utils.OtherUtils
import com.mayunfeng.join.utils.SqlUtils
import com.mayunfeng.join.utils.TimeUtils
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
    private lateinit var APPConfig: AppConfig

    @Autowired
    private lateinit var request: HttpServletRequest

    @Autowired
    private lateinit var resourceLoader: ResourceLoader


    override fun login(userAccount: String?, userPassword: String?): JsonResult<UserTable> {
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
        // 断开失效的ws
        userWebSocketHandler.disLinkUnboundToken()
        return JsonResult.ok(disposeReturnUserData(userData))
    }


    override fun userInfoByToken(): JsonResult<UserTable> {
        val tokenBean = tokenServiceImpl.queryByToken(OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!)!!
        val userData = userTableMapper.selectById(tokenBean.userId)
        return JsonResult.ok(disposeReturnUserData(userData))
    }

    override fun userInfoById(userId: Long): UserTable {
        val userData = userTableMapper.selectById(userId)
        return disposeReturnUserData(userData, true)
    }


    override fun userImage(c: String?, userId: Long?): BufferedImage {
        var userBen: UserTable?
        val tokenBen = tokenServiceImpl.queryByToken(OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!)
        userBen = if (userId != null && userId != -1L){
            userTableMapper.selectById(userId)
        } else {
            userTableMapper.selectById(tokenBen!!.userId)
        }
        if (userBen == null){
            userBen = userTableMapper.selectById(tokenBen!!.userId)
        }
        return if(userBen!!.userImg == APPConfig.configDefaultPicName ){
            if (userBen.userSex!!)
                ImageIO.read(resourceLoader.getResource(APPConfig.configDefaultPicBoy).inputStream)
            else
                ImageIO.read(resourceLoader.getResource(APPConfig.configDefaultPicGirl).inputStream)
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
                    SqlUtils.delImageFile(userTableMapper, "user_img", it.userImg, "${APPConfig.configUserImageFilePath()}${it.userImg}")
                    it.userImg = pictureServiceImpl.upImage(userImage).result!!
                })
        )


    override fun editName(userName: String?): JsonResult<UserTable> =
        JsonResult.ok(
            disposeReturnUserData(
                editOneSqlValue(
                    OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!,
                    arrayOf(userName)
                ) {
                    it.userName = userName
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
                    if (userNewPassword!!.length > APPConfig.configMaxLength || userNewPassword.length < APPConfig.configMinLength) throw UserPasswordLengthException()
                    if (it.userPassword == userNewPassword) throw UserEquallyPasswordException()
                    if (OtherUtils.isFieldIllegal(userNewPassword)) throw ParameterIllegalException()
                    it.userPassword = userNewPassword
                    tokenServiceImpl.deleteByToken(tokenLogin)
                    // 断开失效的ws
                    userWebSocketHandler.disLinkUnboundToken(CloseStatus.PROTOCOL_ERROR)
                })
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
        if(userData.userImg == APPConfig.configDefaultPicName){
            userData.userImg = if (userData.userSex!!){
                APPConfig.configDefaultPicName + "_boy"
            } else {
                APPConfig.configDefaultPicName + "_girl"
            }
        }
        userData.userImg = "/myf-user-api/user-img/${userData.userImg}${
            if (APPConfig.configImageTime != -1L) {
                val createTimeAESBCB = OtherUtils.createTimeAESBCB(APPConfig.configSalt)
                "?c=$createTimeAESBCB"
            } else ""
        }${
            if (APPConfig.configImageTime != -1L) if (isAddUserId) "&uid=${userData.id}" else ""
            else if (isAddUserId) "?uid=${userData.id}" else ""
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

