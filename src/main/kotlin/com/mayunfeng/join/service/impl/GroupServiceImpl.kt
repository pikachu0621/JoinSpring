package com.mayunfeng.join.service.impl

import com.mayunfeng.join.config.AppConfig
import com.mayunfeng.join.service.IGroupService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest


@Service
class GroupServiceImpl: IGroupService {

    @Autowired
    private lateinit var APPConfig: AppConfig

    @Autowired
    private lateinit var request: HttpServletRequest

    // TODO --


}