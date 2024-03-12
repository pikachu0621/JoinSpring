package com.pkpk.join.controller

import com.pkpk.join.base.BaseController
import com.pkpk.join.config.API_START_SIGN
import com.pkpk.join.service.IStartSignService
import com.pkpk.join.service.impl.StartSignServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(API_START_SIGN)
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
        @RequestParam("sign-time") signTime: Long,
    ) = startSignServiceImpl.startSign(
        groupId,
        signTitle,
        signContent,
        signType,
        signKey,
        signTime
    )

    @GetMapping("/all-info")
    override fun getSignAllInfoListByUserId() = startSignServiceImpl.getSignAllInfoListByUserId()


    @GetMapping("/delete-sign/{signId}", "/delete-sign/**")
    override fun delSign(@PathVariable("signId", required = false) signId: Long) = startSignServiceImpl.delSign(signId)

}