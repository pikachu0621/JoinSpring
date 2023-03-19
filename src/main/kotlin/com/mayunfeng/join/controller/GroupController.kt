package com.mayunfeng.join.controller

import com.mayunfeng.join.base.BaseController
import com.mayunfeng.join.model.GroupTable
import com.mayunfeng.join.service.IGroupService
import com.mayunfeng.join.service.impl.GroupServiceImpl
import com.mayunfeng.join.utils.JsonResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/myf-group-api")
@CrossOrigin
class GroupController:  BaseController(), IGroupService{


    @Autowired
    private lateinit var groupServiceImpl: GroupServiceImpl


    @PostMapping("/create")
    @ResponseBody
    override fun createGroup(
        @RequestParam("img", required = false) img: MultipartFile?,
        @RequestParam("name", required = false) name: String?,
        @RequestParam("type", required = false) type: String?,
        @RequestParam("ird", required = false) ird: String?):
            JsonResult<GroupTable>  = groupServiceImpl.createGroup(img, name, type, ird)

    @GetMapping("/user-create-group")
    override fun userCreateGroup(): JsonResult<Array<GroupTable>> = groupServiceImpl.userCreateGroup()


    @GetMapping("/delete-group/{id}", "/delete-group/**")
    override fun deleteUserGroup(@PathVariable("id", required = false) id: Long?): JsonResult<Array<GroupTable>> = groupServiceImpl.deleteUserGroup(id)


    @PostMapping("/edit-group")
    @ResponseBody
    override fun editUserGroup(
        @RequestParam("id", required = true)  id: Long?,
        @RequestParam("img", required = false) img: MultipartFile?,
        @RequestParam("name", required = false) name: String?,
        @RequestParam("type", required = false) type: String?,
        @RequestParam("ird", required = false) ird: String?
    ): JsonResult<GroupTable>  = groupServiceImpl.editUserGroup(id, img, name, type, ird)



    @GetMapping("/query-group/{id}", "/query-group/**")
    override fun queryGroupInfoById(@PathVariable("id", required = false) id: Long?): JsonResult<GroupTable> = groupServiceImpl.queryGroupInfoById(id)
}