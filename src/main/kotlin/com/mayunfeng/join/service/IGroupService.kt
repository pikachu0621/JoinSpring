package com.mayunfeng.join.service

import com.mayunfeng.join.model.GroupTable
import com.mayunfeng.join.model.LGroupBean
import com.mayunfeng.join.model.UserTable
import com.mayunfeng.join.utils.JsonResult
import org.springframework.web.multipart.MultipartFile


/**
 * 组表
 */
interface IGroupService {


    /**
     * 创建组
     *
     * @param img 头像
     * @param name 名字
     * @param type 类型
     * @param ird 介绍
     *
     */
    fun createGroup(img: MultipartFile?,  name: String?, type: String?, ird: String?): JsonResult<GroupTable>


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
    fun deleteGroupByUserId(userId: Long){ }


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
    fun editUserGroup(id: Long?, img: MultipartFile?, name: String?, type: String?, ird: String?): JsonResult<GroupTable>


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
     * 有效时间为 3min
     * 如果该范围有同样密码的组 则 让用户重新输入
     *
     *
     */
    fun faceToFaceAddGroup(longitude: Long, latitude: Long, password: Long){

    }

}