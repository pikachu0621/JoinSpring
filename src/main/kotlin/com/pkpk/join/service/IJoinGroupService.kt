package com.pkpk.join.service

import com.pkpk.join.model.GroupTable
import com.pkpk.join.model.LGroupBean
import com.pkpk.join.model.UserTable
import com.pkpk.join.utils.JsonResult

interface IJoinGroupService {


    /**
     * 加入某个组
     *
     * 需要 token
     *
     */
    fun joinGroup(groupId : Long?): JsonResult<GroupTable>


    /**
     * 退出某个组
     *
     * 需要 token
     *
     */
    fun outGroup(groupId : Long?):  JsonResult<String>


    /**
     * 获取我加入的组
     * 需要 token
     *
     */
    fun queryUserJoinGroup(): JsonResult<Array<GroupTable>>


    /**
     * 获取某个组所有用户
     *
     */
    fun queryJoinGroupAllUser(groupId : Long?):  JsonResult<LGroupBean<Array<UserTable>>>


    /**
     * 获取加入 该组的总人数
     * 不开放 该方法API
     *
     */
    fun getJoinUserNum(groupId : Long): Int



    /**
     * 获取加入该组的前4名用户
     * 不开放 该方法API
     *
     */
    fun queryJoinTopFourPeople(groupId : Long): List<UserTable>


    /**
     * 验证用户是否加入该组
     * 不开放 该方法API
     * @param userId  -1 获取token里的userId
     *  true   加入
     *  false  未加入
     */
    fun verifyJoinGroupByUserId(groupId: Long, userId: Long = -1): Boolean



}