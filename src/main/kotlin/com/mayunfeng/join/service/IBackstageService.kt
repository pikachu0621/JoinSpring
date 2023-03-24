package com.mayunfeng.join.service


/**
 * 后台管理 API
 *
 *
 */
interface IBackstageService {

    /**
     * root 管理员登录
     * 登录完成返回 token
     *
     */
    fun loginRoot()


    /**
     * 获取全部用户
     *
     */
    fun getAllUser()


    /**
     * 获取全部群组
     *
     */
    fun getAllGroup()


    /**
     * 根据groupID删除群组
     * 绑定的组员也一并删除
     *
     */
    fun delGroupByGroupId()


    /**
     * 根据UserId拉黑用户
     *
     */
    fun shieldUserByUserId()


    /**
     * 根据userId 删除用户
     * 删除一切有关数据  包括创建的群组，加入的群组，签到信息，历史记录。。。等
     *
     */
    fun delUserByUserId()


    /**
     * 修改用户信息
     * 包括所有信息
     *
     * 空字段为不修改
     *
     */
    fun rootEditUserInfo()


    /**
     * 修改组信息 包括所有信息
     *
     * 空字段为不修改
     *
     */
    fun rootEditGroupInfo()
}