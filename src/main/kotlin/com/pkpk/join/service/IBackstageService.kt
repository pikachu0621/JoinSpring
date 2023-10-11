package com.pkpk.join.service

import com.pkpk.join.model.GroupTable
import com.pkpk.join.model.UserTable
import com.pkpk.join.utils.JsonResult
import org.springframework.web.multipart.MultipartFile


/**
 * todo 后台管理 API
 * 未创建 Impl 未创建 Controller
 *
 *
 */
interface IBackstageService {

    /**
     * root 管理员登录
     * 登录完成返回 token
     *
     */
    fun loginRoot(rootAccount: String, rootPassword: String): JsonResult<UserTable>


    /**
     * 验证 Token   (需要验证用户等级,防止有人用普通用户token非法操作)
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
     *
     */
    fun delGroupByGroupId(groupId: Long): JsonResult<Boolean>


    /**
     * 根据userId 删除用户
     * 删除一切有关数据  包括创建的群组，加入的群组，签到信息，历史记录。。。等
     *
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
        userImage: MultipartFile?
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
        groupImg: MultipartFile
    ): JsonResult<Boolean>
}