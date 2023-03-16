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
        @RequestParam("img", required = true) img: MultipartFile?,
        @RequestParam("name", required = true) name: String?,
        @RequestParam("type", required = true) type: String?,
        @RequestParam("ird", required = true) ird: String?):
            JsonResult<GroupTable>  = groupServiceImpl.createGroup(img, name, type, ird)

    @GetMapping("/user-create-group")
    override fun userCreateGroup(): JsonResult<Array<GroupTable>> = groupServiceImpl.userCreateGroup()
}