package com.mayunfeng.join.service

import com.mayunfeng.join.model.UserTable
import com.mayunfeng.join.utils.JsonResult
import org.springframework.web.multipart.MultipartFile

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
     *
     * @param tokenLogin tokenLogin
     */
    fun userInfoByToken(tokenLogin: String): JsonResult<UserTable>


    /**
     * 修改头像
     *
     * @param userImage 用户头像
     */
    fun editImage(tokenLogin: String, userImage: MultipartFile?): JsonResult<UserTable>

    /**
     * 修改姓名
     *
     * @param userName 用户姓名
     */
    fun editName(tokenLogin: String, userName: String?): JsonResult<UserTable>


    /**
     * 修改性别
     *
     * @param userSex 用户性别
     */
    fun editSex(tokenLogin: String, userSex: Boolean?): JsonResult<UserTable>


    /**
     * 修改出生日期
     * yyyy-MM-dd
     *
     * @param userBirth 出生日期
     */
    fun editBirth(tokenLogin: String, userBirth: String?): JsonResult<UserTable>



    /**
     * 修改个性签名
     *
     * @param userIntroduce 个签
     */
    fun editIntroduce(tokenLogin: String, userIntroduce: String?): JsonResult<UserTable>


    /**
     * 修改所属单位
     *
     * @param userUnit 所属机构
     */
    fun editUnit(tokenLogin: String, userUnit: String?): JsonResult<UserTable>


    /**
     * 修改密码
     *
     * @param userOldPassword 旧密码
     * @param userNewPassword 新密码
     */
    fun editPassword(tokenLogin: String, userOldPassword: String?, userNewPassword: String?): JsonResult<UserTable>

}