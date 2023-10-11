package com.mayunfeng.join.service.impl

import com.mayunfeng.join.base.BaseServiceImpl
import com.mayunfeng.join.config.AppConfig
import com.mayunfeng.join.config.TOKEN_PARAMETER
import com.mayunfeng.join.handler.UserWebSocketHandler
import com.mayunfeng.join.mapper.GroupTableMapper
import com.mayunfeng.join.mapper.UserTableMapper
import com.mayunfeng.join.model.GroupTable
import com.mayunfeng.join.model.UserTable
import com.mayunfeng.join.service.*
import com.mayunfeng.join.utils.JsonResult
import com.mayunfeng.join.utils.OtherUtils
import com.mayunfeng.join.utils.SqlUtils.delImageFile
import com.mayunfeng.join.utils.SqlUtils.queryByFieldOne
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.socket.CloseStatus
import javax.servlet.http.HttpServletRequest

@Service
class BackstageServiceImpl : BaseServiceImpl(), IBackstageService {

    @Autowired
    private lateinit var APPConfig: AppConfig

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


    override fun loginRoot(rootAccount: String, rootPassword: String): JsonResult<UserTable> {
        val userData =
            userTableMapper.queryByFieldOne("user_account", rootAccount) ?: throw UserNulException()
        if (userData.userPassword != rootPassword) throw UserPasswordException()

        if (userData.userLimit) throw UserBlacklistException()
        if (userData.userGrade == 0 ) throw BackstageAuthorityException()
        // 生成token   - 防止前端  token失效
        val put = tokenServiceImpl.put(-(userData.id), rootAccount, rootPassword, APPConfig.configTokenTime)
        userData.loginToken = put.tokenLogin
        return JsonResult.ok(userData)
    }


    override fun verifyToken(token: String, needRoot: Boolean): JsonResult<UserTable> {
        if (!tokenServiceImpl.verify(token)) throw BackstageTokenException()
        val userId = -tokenServiceImpl.queryByToken(token)!!.userId
        val userInfoById = userServiceImpl.userInfoById(userId)
        if (userInfoById.userLimit) throw UserBlacklistException()
        if (userInfoById.userGrade == 0) throw BackstageAuthorityException()
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
        verifyToken(token)
        val queryAllUser = groupTableMapper.queryAllData() ?: return JsonResult.ok(arrayOf())
        return JsonResult.ok(groupServiceImpl.disposeReturnData(queryAllUser.toTypedArray()))
    }


    override fun delGroupByGroupId(groupId: Long): JsonResult<Boolean> {
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        verifyToken(token)
        groupServiceImpl.deleteUserGroup(groupId)
        return JsonResult.ok(false)
    }


    override fun delUserByUserId(userId: Long): JsonResult<Boolean> {
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val userData = verifyToken(token).result!!
        val userInfoById = userTableMapper.selectById(userId) ?: throw DataNulException()
        if (userData.id == userId) throw BackstageDelToMeException()
        if (userData.userGrade == 1 && userInfoById.userGrade >= 1) throw BackstageEditToRootException()


        // 删除只有本人绑定的图片数据
        userTableMapper.deleteById(userId)
        // 删除用户头像
        userTableMapper.delImageFile("user_img", userInfoById.userImg, "${APPConfig.configUserImageFilePath()}${userInfoById.userImg}")
        groupServiceImpl.deleteGroupByUserId(userId)
        // 断开失效的ws
        // 通知前端下线
        tokenServiceImpl.deleteByUserId(userId)
        userWebSocketHandler.disLinkUnboundToken(CloseStatus.PROTOCOL_ERROR)
        return JsonResult.ok(true)
    }

    override fun rootEditUserInfo(
        userId: Long,
        userPassword: String?,
        userSex: Boolean?,
        userNickname: String?,
        userUnit: String?,
        userBirth: String?,
        userIntroduce: String?,
        userGrade: Int?,
        userLimit: Boolean?,
        userImage: MultipartFile?
    ): JsonResult<Boolean> {
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val userData = verifyToken(token).result!!
        // 这里可以限制 本账号不能修改本账号数据
        val userInfoById = userTableMapper.selectById(userId) ?: throw DataNulException()
        // 管理员禁止编辑 高于 1 的用户
        if (userData.userGrade == 1 && userInfoById.userGrade >= 1 && userInfoById.id != userData.id) throw BackstageEditToRootException()
        if (userData.id == userId && userLimit == true) throw BackstageLimitToRootException()
        if (userData.id == userId && userGrade != null && userGrade != userData.userGrade) throw BackstageGradeToMeException()
        if (userData.userGrade == 1 && userGrade != null && userGrade > 0 && userInfoById.id != userData.id) throw BackstageGradeToException()

        val oldLimit = userInfoById.userLimit
        val oldPassword = userInfoById.userPassword

        userInfoById.apply {
            if (!userPassword.isNullOrEmpty()) this.userPassword = userPassword
            if (userSex != null ) this.userSex = userSex
            if (!userNickname.isNullOrEmpty()) this.userNickname = userNickname
            if (!userUnit.isNullOrEmpty()) this.userUnit = userUnit
            if (!userBirth.isNullOrEmpty()) this.userBirth = userBirth
            if (!userIntroduce.isNullOrEmpty()) this.userIntroduce = userIntroduce
            if (userGrade != null && userGrade >= 0 && userGrade <= 3) this.userGrade = userGrade
            if (userLimit != null ) this.userLimit = userLimit
            if (!(userImage == null || userImage.isEmpty || userImage.size <= 10)) {
                userTableMapper.delImageFile(
                    "user_img",
                    this.userImg,
                    "${APPConfig.configUserImageFilePath()}${this.userImg}"
                )
                this.userImg = pictureServiceImpl.upImage(userImage).result!!
            }
        }
        userTableMapper.updateById(userInfoById)
        // 修改密码后 后端也要下线
        if (oldPassword != userInfoById.userPassword && userInfoById.userGrade == 2){
            tokenServiceImpl.deleteByUserId(-userData.id)
        }

        if (oldPassword != userInfoById.userPassword || oldLimit != userInfoById.userLimit ){
            // 断开失效的ws
            // 通知前端下线
            tokenServiceImpl.deleteByUserId(userId)
            userWebSocketHandler.disLinkUnboundToken(CloseStatus.PROTOCOL_ERROR)
        }
        return JsonResult.ok(true)
    }

    override fun rootEditGroupInfo(
        groupName: String,
        groupIntroduce: String,
        groupType: String,
        groupImg: MultipartFile
    ): JsonResult<Boolean> {
        return JsonResult.ok(false)
    }

}