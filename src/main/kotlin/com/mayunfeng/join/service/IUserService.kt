package com.mayunfeng.join.service

import com.mayunfeng.join.model.UserTable
import com.mayunfeng.join.utils.JsonResult
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage

interface IUserService {


    /**
     * 用户登录 / 注册
     * 有用户则登录
     *
     * @param userAccount 账号
     * @param userPassword 密码
     * @return 返回 需要携带 token 用于链接 WebSocket
     */
    fun login(userAccount: String?, userPassword: String?): JsonResult<UserTable>


    /**
     * token 获取用户数据
     * 需要token
     *
     */
    fun userInfoByToken(): JsonResult<UserTable>

    /**
     * userId 获取用户数据
     *
     */
    fun userInfoById(userId: Long): UserTable{
        return UserTable()
    }


    /**
     * 加载用户头像
     * 需要token
     * @param c 时效AES 可空
     *
     */
    fun userImage(c: String?, userId: Long? = -1L): BufferedImage



    /**
     * 修改头像
     * 需要token
     *
     * @param userImage 用户头像
     */
    fun editImage(userImage: MultipartFile?): JsonResult<UserTable>



    /**
     * 修改姓名
     * 需要token
     *
     * @param userName 用户姓名
     */
    fun editName(userName: String?): JsonResult<UserTable>


    /**
     * 修改性别
     * 需要token
     *
     * @param userSex 用户性别
     */
    fun editSex(userSex: Boolean?): JsonResult<UserTable>


    /**
     * 修改出生日期
     * yyyy-MM-dd
     * 需要token
     *
     * @param userBirth 出生日期
     */
    fun editBirth(userBirth: String?): JsonResult<UserTable>



    /**
     * 修改个性签名
     * 需要token
     *
     * @param userIntroduce 个签
     */
    fun editIntroduce(userIntroduce: String?): JsonResult<UserTable>


    /**
     * 修改所属单位
     * 需要token
     *
     * @param userUnit 所属机构
     */
    fun editUnit(userUnit: String?): JsonResult<UserTable>


    /**
     * 修改密码
     * 需要token
     *
     * @param userOldPassword 旧密码
     * @param userNewPassword 新密码
     */
    fun editPassword(userOldPassword: String?, userNewPassword: String?): JsonResult<UserTable>

}