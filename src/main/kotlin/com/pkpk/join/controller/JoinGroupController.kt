package com.pkpk.join.controller

import com.pkpk.join.base.BaseController
import com.pkpk.join.model.GroupTable
import com.pkpk.join.model.LGroupBean
import com.pkpk.join.model.UserTable
import com.pkpk.join.service.IJoinGroupService
import com.pkpk.join.service.impl.JoinGroupServiceImpl
import com.pkpk.join.utils.JsonResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/pk-join-group-api")
@CrossOrigin
class JoinGroupController : BaseController(), IJoinGroupService {


    @Autowired
    private lateinit var joinGroupServiceImpl: JoinGroupServiceImpl


    @GetMapping("/join-group/{groupId}", "/join-group/**")
    override fun joinGroup(@PathVariable("groupId", required = false) groupId: Long?): JsonResult<GroupTable> =
        joinGroupServiceImpl.joinGroup(groupId)


    @GetMapping("/out-group/{groupId}", "/out-group/**")
    override fun outGroup(@PathVariable("groupId", required = false) groupId: Long?): JsonResult<String> =
        joinGroupServiceImpl.outGroup(groupId)


    @GetMapping("/user-join")
    override fun queryUserJoinGroup(): JsonResult<Array<GroupTable>> = joinGroupServiceImpl.queryUserJoinGroup()


    @GetMapping("/group-all-user/{groupId}", "/group-all-user/**")
    override fun queryJoinGroupAllUser(
        @PathVariable(
            "groupId",
            required = false
        ) groupId: Long?
    ): JsonResult<LGroupBean<Array<UserTable>>> = joinGroupServiceImpl.queryJoinGroupAllUser(groupId)



    override fun getJoinUserNum(groupId: Long): Int = 0
    override fun queryJoinTopFourPeople(groupId: Long): List<UserTable> = arrayListOf()
    override fun verifyJoinGroupByUserId(groupId: Long, userId: Long): Boolean = false
}