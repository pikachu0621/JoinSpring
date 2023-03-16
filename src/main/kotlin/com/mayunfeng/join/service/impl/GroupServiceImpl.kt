package com.mayunfeng.join.service.impl

import com.mayunfeng.join.base.BaseServiceImpl
import com.mayunfeng.join.config.AppConfig
import com.mayunfeng.join.config.TOKEN_PARAMETER
import com.mayunfeng.join.mapper.GroupTableMapper
import com.mayunfeng.join.model.GroupTable
import com.mayunfeng.join.service.*
import com.mayunfeng.join.utils.JsonResult
import com.mayunfeng.join.utils.OtherUtils
import com.mayunfeng.join.utils.SqlUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest


@Service
class GroupServiceImpl: BaseServiceImpl(), IGroupService {

    @Autowired
    private lateinit var APPConfig: AppConfig

    @Autowired
    private lateinit var pictureServiceImpl: PictureServiceImpl

    @Autowired
    private lateinit var tokenServiceImpl: TokenServiceImpl

    @Autowired
    private lateinit var request: HttpServletRequest

    @Autowired
    private lateinit var groupTableMapper: GroupTableMapper



    override fun createGroup(img: MultipartFile?, name: String?, type: String?, ird: String?): JsonResult<GroupTable> {
        if (OtherUtils.isFieldEmpty(img, name, type, ird)) throw ParameterException()
        if (name!!.length > 20) throw GroupNameLengthException()
        if (ird!!.length > 100) throw GroupIrdLengthException()
        // logi(APPConfig.clientConfigGroupType.contentToString())
        logi(type!!)
        if (!APPConfig.clientConfigGroupType.contains(type)) throw GroupTypeException()
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!

        val groupTable = GroupTable().apply {
            this.userId = tokenServiceImpl.queryByToken(token)!!.userId
            this.groupImg = pictureServiceImpl.upImage(img).result!!
            this.groupName = name
            this.groupType = type
            this.groupIntroduce = ird
        }
        groupTableMapper.insert(groupTable)
        return JsonResult.ok(disposeReturnData(groupTableMapper.selectById(groupTable)))
    }




    override fun userCreateGroup(): JsonResult<Array<GroupTable>> {
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val userId = tokenServiceImpl.queryByToken(token)!!.userId
        var queryByFieldList = SqlUtils.queryByFieldList(groupTableMapper, "user_id", userId)
        if (queryByFieldList.isNullOrEmpty()) queryByFieldList = arrayListOf()
        queryByFieldList.forEach { disposeReturnData(it) }
        return JsonResult.ok(queryByFieldList.toTypedArray())
    }


    /**
     * 过滤/处理 数据
     */
    private fun disposeReturnData(groupTable: GroupTable): GroupTable {
        // 限制时间
        // /myf-pic-api/
        // /user/img/
        groupTable.groupImg = " /myf-pic-api/${groupTable.groupImg}${
            if (APPConfig.configImageTime != -1L) {
                val createTimeAESBCB = OtherUtils.createTimeAESBCB(APPConfig.configSalt)
                "?c=$createTimeAESBCB"
            } else {
                ""
            }
        }"
        return groupTable
    }


}