package com.pkpk.join.service

import com.pkpk.join.model.GroupTable
import com.pkpk.join.model.LGroupBean
import com.pkpk.join.model.UserTable
import com.pkpk.join.utils.JsonResult
import org.springframework.web.multipart.MultipartFile


/**
 * 组表
 */
interface IGroupService {


    /**
     * @param name 名字
     * @param type 类型
     * @param ird 介绍
     * @param seek 是否可搜索
     * @param pws 加入密码
     * @param ftf 是否开启面对面
     * @param qr 是否开启二维码
     */
    data class CreateGroupArgument(
        var name: String,
        var type: String,
        var ird: String?,
        var seek: Boolean?,
        var pws: String?,
        var ftf: Boolean?,
        var qr: Boolean?,
    )
    /**
     * 创建组
     *
     * @param img 头像
     * @param argument 参数
     */
    fun createGroup(
        img: MultipartFile?,
        argument: CreateGroupArgument
    ): JsonResult<GroupTable>


    /**
     * 获取当前用户已创建的组
     * 需要token
     *
     */
    fun userCreateGroup(): JsonResult<Array<GroupTable>>


    /**
     * 删除组
     * 需要token
     * @param id 组id
     */
    fun deleteUserGroup(id: Long?): JsonResult<Array<GroupTable>>


    /**
     * 删除组
     * 删除该用户绑定的组
     * @param userId 用户id
     */
    fun deleteGroupByUserId(userId: Long) {}


    /**
     * 编辑组
     * 一次性的全改
     * null 不修改
     *
     * 需要token
     *
     * @param id 组id
     *
     */
    fun editUserGroup(
        id: Long?,
        img: MultipartFile?,
        name: String?,
        type: String?,
        ird: String?,
    ): JsonResult<GroupTable>


    /**
     * 根据 组id 获取 数据
     *
     * @param id 组id
     *
     */
    fun queryGroupInfoById(id: Long?): JsonResult<GroupTable>


    /**
     *  踢出本组的某个用户
     *
     * @param targetUserId 要踢出的目标用户Id
     * @param  byGroupId 踢出那个组
     *
     *
     */
    fun comeOutUserByGroup(targetUserId: Long?, byGroupId: Long?): JsonResult<LGroupBean<Array<UserTable>>>


    /**
     * 根据组名查询 组
     * 模糊查询
     *
     */
    fun queryGroupByName(groupNameAndGroupId: String?): JsonResult<Array<GroupTable>>


    /**
     * todo 面对面加组
     * 发起组的 经纬度 密码
     * 有效时间为 10min
     * 如果该范围有同样密码的组 则 让用户重新输入
     *
     *
     */
    fun faceToFaceAddGroup(longitude: Long, latitude: Long, password: Long)

}