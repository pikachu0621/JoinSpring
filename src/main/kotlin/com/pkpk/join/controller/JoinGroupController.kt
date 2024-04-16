package com.pkpk.join.controller

import com.pkpk.join.base.BaseController
import com.pkpk.join.config.API_JOIN_GROUP
import com.pkpk.join.model.UserTable
import com.pkpk.join.service.IJoinGroupService
import com.pkpk.join.service.impl.JoinGroupServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(API_JOIN_GROUP)
@CrossOrigin
class JoinGroupController : BaseController(), IJoinGroupService {


    @Autowired
    private lateinit var joinGroupServiceImpl: JoinGroupServiceImpl


    @GetMapping("/join-group")
    override fun joinGroup(
        @RequestParam("groupId", required = true) groupId: Long,
        @RequestParam("groupVerify", required = false) groupVerifyCode: String?
    ) = joinGroupServiceImpl.joinGroup(
        groupId,
        groupVerifyCode
    )


    @GetMapping("/out-group/{groupId}", "/out-group/**")
    override fun outGroup(@PathVariable("groupId", required = false) groupId: Long?) =
        joinGroupServiceImpl.outGroup(groupId)


    @GetMapping("/user-join")
    override fun queryUserJoinGroup() = joinGroupServiceImpl.queryUserJoinGroup()


    @GetMapping("/group-all-user/{groupId}", "/group-all-user/**")
    override fun queryJoinGroupAllUser(
        @PathVariable(
            "groupId",
            required = false
        ) groupId: Long?
    ) = joinGroupServiceImpl.queryJoinGroupAllUser(groupId)


    override fun getJoinUserNum(groupId: Long): Int = 0
    override fun queryJoinTopFourPeople(groupId: Long): List<UserTable> = arrayListOf()
    override fun verifyJoinGroupByUserId(groupId: Long, userId: Long): Boolean = false
}