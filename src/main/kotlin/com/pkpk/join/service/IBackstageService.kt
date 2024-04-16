package com.pkpk.join.service

import com.pkpk.join.config.AppConfigEdit
import com.pkpk.join.model.GroupTable
import com.pkpk.join.model.UserTable
import com.pkpk.join.utils.JsonResult
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest


/**
 * todo 后台管理 API
 * 未创建 Impl 未创建 Controller
 *
 *
 */
interface IBackstageService {

    /**
     * todo 可为全局传参
     *
     * @param userId        用户ID
     * @param userPassword  用户密码
     * @param userSex       用户姓别
     * @param userNickname  用户昵称
     * @param userUnit      用户单位
     * @param userBirth     用户生日
     * @param userIntroduce 用户简介
     * @param userGrade     用户等级
     * @param userLimit     用户限制
     */
    data class EditUserInfoArgument(
        var userId: Long,
        var userPassword: String?,
        var userSex: Boolean?,
        var userNickname: String?,
        var userUnit: String?,
        var userBirth: String?,
        var userIntroduce: String?,
        var userGrade: Int?,
        var userLimit: Boolean?,
    )


    /**
     * root 管理员登录
     * 登录完成返回 token
     *
     * @param rootAccount 用户账号
     * @param rootPassword 用户密码
     */
    fun loginRoot(rootAccount: String, rootPassword: String): JsonResult<UserTable>


    /**
     * 验证 Token   (需要验证用户等级,防止有人用普通用户token非法操作)
     * @param token
     * @param needRoot 是否验证 root 用户 2
     */
    fun verifyToken(token: String, needRoot: Boolean = false): JsonResult<UserTable>


    /**
     * 获取全部用户
     *
     */
    fun getAllUser(): JsonResult<Array<UserTable>>


    /**
     * 获取全部群组
     *
     */
    fun getAllGroup(): JsonResult<Array<GroupTable>>


    /**
     * 根据groupID删除群组
     * 绑定的组员也一并删除
     * @param groupId
     */
    fun delGroupByGroupId(groupId: Long): JsonResult<Boolean>


    /**
     * 根据userId 删除用户
     * 删除一切有关数据  包括创建的群组，加入的群组，签到信息，历史记录。。。等
     * @param userId
     */
    fun delUserByUserId(userId: Long): JsonResult<Boolean>


    /**
     * 修改用户信息
     * 包括所有信息
     *
     * 空字段为不修改
     *
     */
    fun rootEditUserInfo(
        userId: Long,
        userPassword: String?,
        userSex: Boolean?,
        userNickname: String?,
        userUnit: String?,
        userBirth: String?,
        userIntroduce: String?,
        userGrade: Int?,
        userLimit: Boolean?,
        userImage: MultipartFile?,
    ): JsonResult<Boolean>


    /**
     * 修改组信息 包括所有信息
     *
     * 空字段为不修改
     *
     */
    fun rootEditGroupInfo(
        groupName: String,
        groupIntroduce: String,
        groupType: String,
        groupImg: MultipartFile,
    ): JsonResult<Boolean>


    /**
     * 获取SpringBoot配置
     *
     * 参数对应 [com.pkpk.join.config.AppConfigEdit]
     */
    fun rootGetAppConfig(): JsonResult<AppConfigEdit>


    /**
     * 修改SpringBoot配置
     *
     * 参数对应 [com.pkpk.join.config.AppConfigEdit]
     */
    fun rootEditAppConfig(
        appConfigEdit: AppConfigEdit?,
    ): JsonResult<Boolean>


}