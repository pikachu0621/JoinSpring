package com.mayunfeng.join.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.mayunfeng.join.base.BaseServiceException
import com.mayunfeng.join.base.BaseServiceImpl
import com.mayunfeng.join.config.AppConfig
import com.mayunfeng.join.config.TOKEN_PARAMETER
import com.mayunfeng.join.mapper.GroupTableMapper
import com.mayunfeng.join.mapper.JoinGroupTableMapper
import com.mayunfeng.join.mapper.UserTableMapper
import com.mayunfeng.join.model.GroupTable
import com.mayunfeng.join.model.JoinGroupTable
import com.mayunfeng.join.model.UserTable
import com.mayunfeng.join.service.*
import com.mayunfeng.join.utils.JsonResult
import com.mayunfeng.join.utils.OtherUtils
import com.mayunfeng.join.utils.SqlUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
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

    @Autowired
    private lateinit var userTableMapper: UserTableMapper

    @Autowired
    private lateinit var joinGroupServiceImpl: JoinGroupServiceImpl

    @Autowired
    private lateinit var joinGroupTableMapper: JoinGroupTableMapper


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
        val groupTableInsert = groupTableMapper.selectById(groupTable)
        joinGroupServiceImpl.joinGroup(groupTableInsert.id)
        return JsonResult.ok(disposeReturnData(groupTableInsert))
    }


    override fun userCreateGroup(): JsonResult<Array<GroupTable>> {
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val userId = tokenServiceImpl.queryByToken(token)!!.userId
        return JsonResult.ok(readUserListGroup(userId))
    }

    // 删除所有加入此组的用户记录
    override fun deleteUserGroup(id: Long?): JsonResult<Array<GroupTable>> {
        if( OtherUtils.isFieldEmpty(id)) throw ParameterException()
        val userId = verifyGroup(id!!)
        val groupTable = groupTableMapper.selectById(id)
        SqlUtils.delImageFile(groupTableMapper, "group_img", groupTable.groupImg, "${APPConfig.configUserImageFilePath()}${groupTable.groupImg}")
        joinGroupTableMapper.delete(QueryWrapper<JoinGroupTable>().apply{
            eq("group_id", id)
        })
        groupTableMapper.deleteById(id)
        return JsonResult.ok(readUserListGroup(userId))
    }


    override fun editUserGroup(
        id: Long?,
        img: MultipartFile?,
        name: String?,
        type: String?,
        ird: String?
    ): JsonResult<GroupTable> {
        if (OtherUtils.isFieldEmpty(id)) throw ParameterException()
        if (!name.isNullOrEmpty() && name.length > 20) throw GroupNameLengthException()
        if (!ird.isNullOrEmpty() && ird.length > 100) throw GroupIrdLengthException()
        if (!type.isNullOrEmpty() && !APPConfig.clientConfigGroupType.contains(type)) throw GroupTypeException()
        verifyGroup(id!!)
        val groupTable = groupTableMapper.selectById(id).apply {
            if (img != null){
                // 删除只有本人绑定的图片数据
                SqlUtils.delImageFile(groupTableMapper, "group_img", this.groupImg, "${APPConfig.configUserImageFilePath()}${this.groupImg}")
                this.groupImg = pictureServiceImpl.upImage(img).result!!
            }
            if (!name.isNullOrEmpty()) this.groupName = name
            if (!type.isNullOrEmpty()) this.groupType = type
            if (!ird.isNullOrEmpty()) this.groupIntroduce = ird
        }
        groupTableMapper.updateById(groupTable)
        return JsonResult.ok(disposeReturnData(groupTable))
    }


    override fun queryGroupInfoById(id: Long?): JsonResult<GroupTable> {
        if (OtherUtils.isFieldEmpty(id)) throw ParameterException()
        verifyGroup(id!!)
        val groupTable = groupTableMapper.selectById(id) ?: throw GroupNulException()
        return JsonResult.ok(disposeReturnData(groupTable))
    }





    /**
     * 根据Token 获取用户数据
     */
    fun queryByToken(): UserTable {
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val userId = tokenServiceImpl.queryByToken(token)!!.userId
        return userTableMapper.selectById(userId)
    }


    /**
     * 验证该用户 是否有该组操作权限
     *
     * 返回 user id
     */
    fun verifyGroup(id: Long): Long{
        val groupTable = groupTableMapper.selectById(id) ?: throw GroupNulException()
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val userId = tokenServiceImpl.queryByToken(token)!!.userId
        if (groupTable.userId != userId) throw GroupUserAuthorityEditException()
        return userId
    }


    /**
     * 获取该用户创建的所有群
     */
    fun readUserListGroup(userId: Long): Array<GroupTable> {
        var queryByFieldList = SqlUtils.queryByFieldList(groupTableMapper, "user_id", userId)
        if (queryByFieldList.isNullOrEmpty()) queryByFieldList = arrayListOf()
        queryByFieldList.forEach {
            disposeReturnData(it)
        }
        return queryByFieldList.toTypedArray()
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
        // 该组人数
        // 该组前4名
        // 用户是否加入该组
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val userId = tokenServiceImpl.queryByToken(token)!!.userId
        if (groupTable.userId == userId){
            groupTable.groupAndUser = 2
        } else {
            groupTable.groupAndUser =  if (joinGroupServiceImpl.verifyJoinGroupByUserId(groupTable.id)) 1 else 0
        }
        groupTable.groupTopFourPeople = joinGroupServiceImpl.queryJoinTopFourPeople(groupTable.id)
        groupTable.groupPeople = joinGroupServiceImpl.getJoinUserNum(groupTable.id)
        return groupTable
    }


}