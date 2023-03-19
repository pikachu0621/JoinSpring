package com.mayunfeng.join.service

import com.mayunfeng.join.model.GroupTable
import com.mayunfeng.join.model.UserTable
import com.mayunfeng.join.utils.JsonResult

interface IJoinGroupService {


    /**
     * todo  加入某个组
     *
     * 需要 token
     *
     */
    fun joinGroup(groupId : Long?): JsonResult<GroupTable>


    /**
     * todo  退出某个组
     *
     * 需要 token
     *
     */
    fun outGroup(groupId : Long?):  JsonResult<String>


    /**
     * todo 获取我加入的组
     * 需要 token
     *
     */
    fun queryUserJoinGroup(): JsonResult<Array<GroupTable>>


    /**
     * todo 获取某个组所有用户
     *
     */
    fun queryJoinGroupAllUser(groupId : Long?): JsonResult<Array<UserTable>>


    /**
     * todo  获取加入 该组的总人数
     * 不开放 该方法API
     *
     */
    fun getJoinUserNum(groupId : Long): Int



    /**
     * todo 获取加入该组的前4名用户
     * 不开放 该方法API
     *
     */
    fun queryJoinTopFourPeople(groupId : Long): List<UserTable>


    /**
     * todo 验证用户是否加入该组
     * 不开放 该方法API
     *  true   加入
     *  false  未加入
     */
    fun verifyJoinGroupByUserId(groupId: Long): Boolean



}