package com.pkpk.join.controller

import com.pkpk.join.base.BaseController
import com.pkpk.join.model.GroupTable
import com.pkpk.join.model.LGroupBean
import com.pkpk.join.model.UserTable
import com.pkpk.join.service.IGroupService
import com.pkpk.join.service.impl.GroupServiceImpl
import com.pkpk.join.utils.JsonResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/pk-group-api")
@CrossOrigin
class GroupController : BaseController(), IGroupService {


    @Autowired
    private lateinit var groupServiceImpl: GroupServiceImpl


    @PostMapping("/create")
    @ResponseBody
    override fun createGroup(
        @RequestParam("img", required = false) img: MultipartFile?,
        @RequestParam("name", required = false) name: String?,
        @RequestParam("type", required = false) type: String?,
        @RequestParam("ird", required = false) ird: String?
    ):
            JsonResult<GroupTable> = groupServiceImpl.createGroup(img, name, type, ird)

    @GetMapping("/user-create-group")
    override fun userCreateGroup(): JsonResult<Array<GroupTable>> = groupServiceImpl.userCreateGroup()


    @GetMapping("/delete-group/{id}", "/delete-group/**")
    override fun deleteUserGroup(@PathVariable("id", required = false) id: Long?): JsonResult<Array<GroupTable>> =
        groupServiceImpl.deleteUserGroup(id)


    @PostMapping("/edit-group")
    @ResponseBody
    override fun editUserGroup(
        @RequestParam("id", required = true) id: Long?,
        @RequestParam("img", required = false) img: MultipartFile?,
        @RequestParam("name", required = false) name: String?,
        @RequestParam("type", required = false) type: String?,
        @RequestParam("ird", required = false) ird: String?
    ): JsonResult<GroupTable> = groupServiceImpl.editUserGroup(id, img, name, type, ird)


    @GetMapping("/query-group/{id}", "/query-group/**")
    override fun queryGroupInfoById(@PathVariable("id", required = false) id: Long?): JsonResult<GroupTable> =
        groupServiceImpl.queryGroupInfoById(id)


    @GetMapping("/remove-user-group")
    override fun comeOutUserByGroup(
        @RequestParam("user-id", required = false) targetUserId: Long?,
        @RequestParam("group-id", required = false) byGroupId: Long?
    ): JsonResult<LGroupBean<Array<UserTable>>> = groupServiceImpl.comeOutUserByGroup(targetUserId, byGroupId)

    @GetMapping("/like-group")
    override fun queryGroupByName(
        @RequestParam("id-name", required = false) groupNameAndGroupId: String?
    ): JsonResult<Array<GroupTable>> = groupServiceImpl.queryGroupByName(groupNameAndGroupId)
}