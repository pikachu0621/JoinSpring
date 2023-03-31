package com.mayunfeng.join.controller

import com.mayunfeng.join.base.BaseController
import com.mayunfeng.join.model.GroupTable
import com.mayunfeng.join.model.LGroupBean
import com.mayunfeng.join.model.StartSignTable
import com.mayunfeng.join.model.UserTable
import com.mayunfeng.join.service.IGroupService
import com.mayunfeng.join.service.IStartSignService
import com.mayunfeng.join.service.impl.GroupServiceImpl
import com.mayunfeng.join.service.impl.StartSignServiceImpl
import com.mayunfeng.join.utils.JsonResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/myf-sign-api")
@CrossOrigin
class StartSignController : BaseController(), IStartSignService {


    @Autowired
    private lateinit var startSignServiceImpl: StartSignServiceImpl


    @PostMapping("/start")
    @ResponseBody
    override fun startSign(
        @RequestParam("group-id") groupId: Long,
        @RequestParam("sign-title", required = false) signTitle: String?,
        @RequestParam("sign-content", required = false) signContent: String?,
        @RequestParam("sign-type") signType: Int,
        @RequestParam("sign-key", required = false) signKey: String?,
        @RequestParam("sign-time") signTime: Long
    ): JsonResult<StartSignTable> =
        startSignServiceImpl.startSign(
            groupId,
            signTitle,
            signContent,
            signType,
            signKey,
            signTime
        )

    @GetMapping("/all-info")
    override fun getSignAllInfoListByUserId(): JsonResult<Array<StartSignTable>> =
        startSignServiceImpl.getSignAllInfoListByUserId()



    @GetMapping("/delete-sign/{signId}", "/delete-sign/**")
    override fun delSign(@PathVariable("signId", required = false) signId: Long): JsonResult<Array<StartSignTable>> =
        startSignServiceImpl.delSign(signId)

}