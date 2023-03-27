package com.mayunfeng.join.service.impl

import com.mayunfeng.join.base.BaseServiceImpl
import com.mayunfeng.join.mapper.StartSignTableMapper
import com.mayunfeng.join.model.StartSignTable
import com.mayunfeng.join.service.IStartSignService
import com.mayunfeng.join.service.ParameterException
import com.mayunfeng.join.utils.JsonResult
import com.mayunfeng.join.utils.OtherUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class StartSignServiceImpl:  BaseServiceImpl(), IStartSignService {

    @Autowired
    private lateinit var startSignTableMapper: StartSignTableMapper

    @Autowired
    private lateinit var groupServiceImpl: GroupServiceImpl


    override fun startSign(
        groupId: Long,
        signTitle: String?,
        signContent: String?,
        signType: Int,
        signKey: String?,
        signTime: Long
    ): JsonResult<StartSignTable> {
        if (OtherUtils.isFieldEmpty(groupId, signType, signTime)) throw ParameterException()
        val signTitleNul = if (signTitle.isNullOrEmpty()) "default" else signTitle
        val signContentNul = if (signContent.isNullOrEmpty()) "default" else signContent
        var signKeyNul = if (signKey.isNullOrEmpty()) "-1" else signKey
        val userId = groupServiceImpl.verifyGroup(groupId)
        if (signType == 3){
            signKeyNul = OtherUtils.createTimeMd5()
        }
        val startSignTable = StartSignTable(
            userId,
            groupId,
            signTitleNul,
            signContentNul,
            signType,
            signKeyNul,
            signTime
        )
        startSignTableMapper.insert(startSignTable)
        return JsonResult.ok(startSignTableMapper.selectById(startSignTable))
    }






    override fun delSign(signId: Long): JsonResult<String> {
        // TODO("Not yet implemented")
        return JsonResult.ok("")
    }
}