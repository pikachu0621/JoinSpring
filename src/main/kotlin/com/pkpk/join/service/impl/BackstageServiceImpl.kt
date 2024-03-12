package com.pkpk.join.service.impl

import com.pkpk.join.base.BaseServiceImpl
import com.pkpk.join.config.AppConfig
import com.pkpk.join.config.AppConfigEdit
import com.pkpk.join.config.TOKEN_PARAMETER
import com.pkpk.join.handler.UserWebSocketHandler
import com.pkpk.join.mapper.GroupTableMapper
import com.pkpk.join.mapper.UserTableMapper
import com.pkpk.join.model.GroupTable
import com.pkpk.join.model.UserRank
import com.pkpk.join.model.UserTable
import com.pkpk.join.service.*
import com.pkpk.join.utils.JsonResult
import com.pkpk.join.utils.OtherUtils
import com.pkpk.join.utils.SqlUtils.delImageFile
import com.pkpk.join.utils.SqlUtils.queryByFieldOne
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.socket.CloseStatus
import javax.servlet.http.HttpServletRequest

@Service
class BackstageServiceImpl : BaseServiceImpl(), IBackstageService {

    @Autowired
    private lateinit var appConfig: AppConfig

    @Autowired
    private lateinit var tokenServiceImpl: TokenServiceImpl

    @Autowired
    private lateinit var userServiceImpl: UserServiceImpl

    @Autowired
    private lateinit var groupServiceImpl: GroupServiceImpl

    @Autowired
    private lateinit var pictureServiceImpl: PictureServiceImpl

    @Autowired
    private lateinit var userWebSocketHandler: UserWebSocketHandler

    @Autowired
    private lateinit var request: HttpServletRequest

    @Autowired
    private lateinit var userTableMapper: UserTableMapper

    @Autowired
    private lateinit var groupTableMapper: GroupTableMapper

    @Autowired
    private lateinit var userLogServiceImpl: UserLogServiceImpl


    override fun loginRoot(rootAccount: String, rootPassword: String): JsonResult<UserTable> {
        val userData =
            userTableMapper.queryByFieldOne(UserTable::userAccount, rootAccount) ?: throw UserNulException()
        if (userData.userPassword != rootPassword) throw UserPasswordException()

        if (userData.userLimit) throw UserBlacklistException()
        if (userData.userGrade == UserRank.NORMAL.LV) throw BackstageAuthorityException()
        // 生成token   - 防止前端  token失效
        val put = tokenServiceImpl.put(-(userData.id), rootAccount, rootPassword, appConfig.appConfigEdit.tokenTime)
        userData.loginToken = put.tokenLogin
        userLogServiceImpl.addLogLogin("后台登录")
        return JsonResult.ok(userData)
    }


    override fun verifyToken(token: String, needRoot: Boolean): JsonResult<UserTable> {
        if (!tokenServiceImpl.verify(token)) throw BackstageTokenException()
        val userId = -tokenServiceImpl.queryByToken(token)!!.userId
        val userInfoById = userServiceImpl.userInfoById(userId)
        if (userInfoById.userLimit) throw UserBlacklistException()
        if (userInfoById.userGrade == UserRank.NORMAL.LV) throw BackstageAuthorityException()
        return JsonResult.ok(userInfoById)
    }

    override fun getAllUser(): JsonResult<Array<UserTable>> {
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        verifyToken(token)
        val queryAllUser = userTableMapper.queryAllUser() ?: return JsonResult.ok(arrayOf())
        queryAllUser.forEach {
            userServiceImpl.disposeReturnUserData(it, true)
        }
        return JsonResult.ok(queryAllUser.toTypedArray())
    }

    override fun getAllGroup(): JsonResult<Array<GroupTable>> {
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val verifyToken = verifyToken(token)
        val queryAllUser = groupTableMapper.queryAllData() ?: return JsonResult.ok(arrayOf())
        userLogServiceImpl.addLogLogin("后台全部删除组")
        return JsonResult.ok(groupServiceImpl.disposeReturnData(queryAllUser.toTypedArray()))
    }


    override fun delGroupByGroupId(groupId: Long): JsonResult<Boolean> {
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val verifyToken = verifyToken(token)
        groupServiceImpl.deleteUserGroup(groupId)
        userLogServiceImpl.addLogLogin("后台删除组")
        return JsonResult.ok(false)
    }


    override fun delUserByUserId(userId: Long): JsonResult<Boolean> {
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val userData = verifyToken(token).result!!
        val userInfoById = userTableMapper.selectById(userId) ?: throw DataNulException()
        if (userData.id == userId) throw BackstageDelToMeException()
        if (userData.userGrade == UserRank.ADMIN.LV && userInfoById.userGrade >= UserRank.ADMIN.LV) throw BackstageEditToRootException()


        // 删除只有本人绑定的图片数据
        userTableMapper.deleteById(userId)
        // 删除用户头像
        userTableMapper.delImageFile(UserTable::userImg,
            userInfoById.userImg,
            "${appConfig.configUserImageFilePath()}${userInfoById.userImg}"
        )
        groupServiceImpl.deleteGroupByUserId(userId)
        // 断开失效的ws
        // 通知前端下线
        tokenServiceImpl.deleteByUserId(userId)
        userWebSocketHandler.disLinkUnboundToken(CloseStatus.PROTOCOL_ERROR)
        userLogServiceImpl.addLogLogin("后台删除用户 用户ID: ${userData.id}, 账号: ${userData.userAccount}}")
        return JsonResult.ok(true)
    }

    override fun rootEditUserInfo(
        userImage: MultipartFile?,
        argument: IBackstageService.EditUserInfoArgument?
    ): JsonResult<Boolean> {
        if (argument == null) throw ParameterException()
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val userData = verifyToken(token).result!!
        // 这里可以限制 本账号不能修改本账号数据
        val userInfoById = userTableMapper.selectById(argument.userId) ?: throw DataNulException()
        // 管理员禁止编辑 高于 1 的用户
        if (userData.userGrade == UserRank.ADMIN.LV && userInfoById.userGrade >= UserRank.ADMIN.LV && userInfoById.id != userData.id) throw BackstageEditToRootException()
        if (userData.id == argument.userId && argument.userLimit == true) throw BackstageLimitToRootException()
        if (userData.id == argument.userId && argument.userGrade != null && argument.userGrade != userData.userGrade) throw BackstageGradeToMeException()
        if (userData.userGrade == UserRank.ADMIN.LV && argument.userGrade != null && argument.userGrade!! > UserRank.NORMAL.LV && userInfoById.id != userData.id) throw BackstageGradeToException()

        val oldLimit = userInfoById.userLimit
        val oldPassword = userInfoById.userPassword

        userInfoById.apply {
            if (!argument.userPassword.isNullOrEmpty()) this.userPassword = argument.userPassword!!
            if (argument.userSex != null) this.userSex = argument.userSex
            if (!argument.userNickname.isNullOrEmpty()) this.userNickname = argument.userNickname
            if (!argument.userUnit.isNullOrEmpty()) this.userUnit = argument.userUnit
            if (!argument.userBirth.isNullOrEmpty()) this.userBirth = argument.userBirth!!
            if (!argument.userIntroduce.isNullOrEmpty()) this.userIntroduce = argument.userIntroduce
            if (argument.userGrade != null && userGrade >= UserRank.NORMAL.LV && userGrade <= UserRank.ROOT.LV) this.userGrade = argument.userGrade!!
            if (argument.userLimit != null) this.userLimit = argument.userLimit!!
            if (!(userImage == null || userImage.isEmpty || userImage.size <= 10)) {
                userTableMapper.delImageFile(UserTable::userImg, this.userImg,
                    "${appConfig.configUserImageFilePath()}${this.userImg}"
                )
                this.userImg = pictureServiceImpl.upImage(userImage).result!!
            }
        }
        userTableMapper.updateById(userInfoById)
        // 修改密码后 后端也要下线
        if (oldPassword != userInfoById.userPassword && userInfoById.userGrade == UserRank.ROOT.LV) {
            tokenServiceImpl.deleteByUserId(-userData.id)
        }

        if (oldPassword != userInfoById.userPassword || oldLimit != userInfoById.userLimit) {
            // 断开失效的ws
            // 通知前端下线
            tokenServiceImpl.deleteByUserId(argument.userId)
            userWebSocketHandler.disLinkUnboundToken(CloseStatus.PROTOCOL_ERROR)
        }
        userLogServiceImpl.addLogLogin("后台修改用户 用户ID: ${userData.id}, 账号: ${userData.userAccount}}")
        return JsonResult.ok(true)
    }

    override fun rootEditGroupInfo(
        groupName: String,
        groupIntroduce: String,
        groupType: String,
        groupImg: MultipartFile,
    ): JsonResult<Boolean> {
        // todo
        return JsonResult.ok(false)
    }

    override fun rootGetAppConfig(): JsonResult<AppConfigEdit> {
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val verifyToken = verifyToken(token)
        if (verifyToken.result!!.userGrade != UserRank.ROOT.LV) throw BackstageAuthorityException()
        return JsonResult.ok(appConfig.appConfigEdit)
    }


    override fun rootEditAppConfig(appConfigEdit: AppConfigEdit?): JsonResult<Boolean> {
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val verifyToken = verifyToken(token)
        if (verifyToken.result!!.userGrade != UserRank.ROOT.LV) throw BackstageAuthorityException()
        appConfigEdit ?: throw ParameterException()
        if (appConfigEdit.appPackageName.isEmpty()) throw ParameterException()
        if (appConfigEdit.groupTypeArr.isEmpty()) throw ParameterException()
        if (appConfigEdit.imagePassword.isEmpty()) throw ParameterException()
        if (appConfigEdit.imageTime < -1 || appConfigEdit.imageTime == 0L) throw BackstageEditAppConfigException()
        if (appConfigEdit.imageSize < 1) throw BackstageEditAppConfigSizeException()
        if (appConfigEdit.tokenSalt.isEmpty()) throw ParameterException()
        if (appConfigEdit.tokenTime < -1 || appConfigEdit.imageTime == 0L) throw BackstageEditAppConfigException()
        if (appConfigEdit.userAccountLengthLimit.isEmpty()
            || appConfigEdit.userAccountLengthLimit.size != 2
            || appConfigEdit.userAccountLengthLimit[0] > appConfigEdit.userAccountLengthLimit[1]
        ) throw BackstageEditAppConfigAccountLengthLimitException()
        appConfig.setDataAppConfigEdit(appConfigEdit)
        userLogServiceImpl.addLogLogin("后台修改Spring配置")
        return JsonResult.ok(true)
    }

}