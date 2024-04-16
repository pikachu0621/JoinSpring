package com.pkpk.join.controller

import com.pkpk.join.base.BaseController
import com.pkpk.join.config.API_GROUP
import com.pkpk.join.service.IGroupService
import com.pkpk.join.service.impl.GroupServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
@RestController
@RequestMapping(API_GROUP)
@CrossOrigin
class GroupController : BaseController(), IGroupService {


    @Autowired
    private lateinit var groupServiceImpl: GroupServiceImpl


    @PostMapping("/create")
    @ResponseBody
    override fun createGroup(
        @RequestParam("name", required = true) name: String,
        @RequestParam("img", required = true) img: MultipartFile?,
        @RequestParam("type", required = false) type: String?,
        @RequestParam("ird", required = false) ird: String?,
        @RequestParam("search", required = false) search: Boolean?,
        @RequestParam("verify", required = false) verify: String?
    ) = groupServiceImpl.createGroup(name, img, type, ird, search, verify)

    @GetMapping("/user-create-group")
    override fun userCreateGroup() = groupServiceImpl.userCreateGroup()


    @GetMapping("/delete-group/{id}", "/delete-group/**")
    override fun deleteUserGroup(@PathVariable("id", required = false) id: Long?) = groupServiceImpl.deleteUserGroup(
        id
    )


    @PostMapping("/edit-group")
    @ResponseBody
    override fun editUserGroup(
        @RequestParam("id", required = true) id: Long,
        @RequestParam("img", required = false) img: MultipartFile?,
        @RequestParam("name", required = false) name: String?,
        @RequestParam("type", required = false) type: String?,
        @RequestParam("ird", required = false) ird: String?,
        @RequestParam("search", required = false) search: Boolean?,
        @RequestParam("verify", required = false) verify: String?
    ) = groupServiceImpl.editUserGroup(id, img, name, type, ird, search, verify)


    @GetMapping("/query-group/{id}", "/query-group/**")
    override fun queryGroupInfoById(@PathVariable("id", required = false) id: Long?) = groupServiceImpl.queryGroupInfoById(
        id
    )


    @GetMapping("/remove-user-group")
    override fun comeOutUserByGroup(
        @RequestParam("user-id", required = false) targetUserId: Long?,
        @RequestParam("group-id", required = false) byGroupId: Long?
    ) = groupServiceImpl.comeOutUserByGroup(targetUserId, byGroupId)

    @GetMapping("/like-group")
    override fun queryGroupByName(
        @RequestParam("id-name", required = false) groupNameAndGroupId: String?
    ) = groupServiceImpl.queryGroupByName(groupNameAndGroupId)

    override fun faceToFaceAddGroup(longitude: Long, latitude: Long, password: Long) = groupServiceImpl.faceToFaceAddGroup(longitude, latitude, password)
}

