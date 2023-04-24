package com.mayunfeng.join.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.mayunfeng.join.base.BaseServiceException
import com.mayunfeng.join.base.BaseServiceImpl
import com.mayunfeng.join.config.AppConfig
import com.mayunfeng.join.config.TOKEN_PARAMETER
import com.mayunfeng.join.mapper.GroupTableMapper
import com.mayunfeng.join.mapper.JoinGroupTableMapper
import com.mayunfeng.join.mapper.UserSignTableMapper
import com.mayunfeng.join.mapper.UserTableMapper
import com.mayunfeng.join.model.GroupTable
import com.mayunfeng.join.model.JoinGroupTable
import com.mayunfeng.join.model.LGroupBean
import com.mayunfeng.join.model.UserTable
import com.mayunfeng.join.service.*
import com.mayunfeng.join.utils.JsonResult
import com.mayunfeng.join.utils.OtherUtils
import com.mayunfeng.join.utils.OtherUtils.isNumber
import com.mayunfeng.join.utils.SqlUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
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

    @Autowired
    private lateinit var userTableMapper: UserTableMapper

    @Autowired
    private lateinit var joinGroupServiceImpl: JoinGroupServiceImpl

    @Autowired
    private lateinit var joinGroupTableMapper: JoinGroupTableMapper

    @Autowired
    private lateinit var userSignTableMapper: UserSignTableMapper


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


    override fun deleteUserGroup(id: Long?): JsonResult<Array<GroupTable>> {
        if( OtherUtils.isFieldEmpty(id)) throw ParameterException()
        val userId = verifyGroup(id!!)
        val groupTable = groupTableMapper.selectById(id)
        SqlUtils.delImageFile(groupTableMapper, "group_img", groupTable.groupImg, "${APPConfig.configUserImageFilePath()}${groupTable.groupImg}")
        // 删除所有加入此组的用户记录
        joinGroupTableMapper.delete(QueryWrapper<JoinGroupTable>().apply{
            eq("group_id", id)
        })
        groupTableMapper.deleteById(id)
        return JsonResult.ok(readUserListGroup(userId))
    }

    // root等级 用户
    override fun deleteGroupByUserId(userId: Long) {
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val user = userTableMapper.selectById(-tokenServiceImpl.queryByToken(token)!!.userId) ?: throw DataNulException()
        if (user.userGrade == 0) throw GroupUserAuthorityEditException()
        val queryByFieldList = SqlUtils.queryByFieldList(groupTableMapper, "user_id", userId) ?: return
        // 先删除该用户加入的组
        SqlUtils.deleteByField(joinGroupTableMapper, "user_id", userId)
        // 再删除该用户创建的组
        SqlUtils.deleteByField(joinGroupTableMapper, "user_id", userId)
        // 然后删除该用户的签到任务
        SqlUtils.deleteByField(userSignTableMapper, "user_id", userId)
        // 最后删除该用户创建的组
        queryByFieldList.forEach {
            SqlUtils.delImageFile(groupTableMapper, "group_img", it.groupImg, "${APPConfig.configUserImageFilePath()}${it.groupImg}")
            // 删除所有加入此组的用户记录
            SqlUtils.deleteByField(joinGroupTableMapper, "group_id", it.id)
            groupTableMapper.deleteById(it.id)
        }
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
        val groupTable = groupTableMapper.selectById(id) ?: throw GroupNulException()
        return JsonResult.ok(disposeReturnData(groupTable))
    }

    override fun comeOutUserByGroup(targetUserId: Long?, byGroupId: Long?): JsonResult<LGroupBean<Array<UserTable>>> {
        if( OtherUtils.isFieldEmpty(targetUserId, byGroupId)) throw ParameterException()
        val userId = verifyGroup(byGroupId!!)
        if (targetUserId == userId) throw GroupByToMyException()
        userTableMapper.selectById(targetUserId) ?: throw UserNulException()
        if (!joinGroupServiceImpl.verifyJoinGroupByUserId(byGroupId, targetUserId!!))  throw GroupUserNotJoinException()
        joinGroupTableMapper.delete(QueryWrapper<JoinGroupTable>().apply {
            allEq(hashMapOf<String, Any>().apply {
                put("group_id", byGroupId)
                put("user_id", targetUserId)
            })
        })
        return joinGroupServiceImpl.queryJoinGroupAllUser(byGroupId)
    }


    override fun queryGroupByName(groupNameAndGroupId: String?): JsonResult<Array<GroupTable>> {
        if( OtherUtils.isFieldEmpty(groupNameAndGroupId)) throw ParameterException()
        val arrayListOf = arrayListOf<GroupTable>()
        val groupId = groupNameAndGroupId!!.isNumber()
        groupTableMapper.queryLikeGroup(groupId, groupNameAndGroupId, "desc")?.let { arrayListOf.addAll(it) }
        return JsonResult.ok(disposeReturnData(arrayListOf.toTypedArray()))
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
     *  @param  groupId
     *
     * 返回 user id
     */
    fun verifyGroup(groupId: Long): Long{
        val groupTable = groupTableMapper.selectById(groupId) ?: throw GroupNulException()
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val userId = tokenServiceImpl.queryByToken(token)!!.userId
        if (userId < 0){
            val userData =  userTableMapper.selectById(userId) ?: throw DataNulException()
            if (userData.userGrade == 2) return userId
        }
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



    fun disposeReturnData(groupTable: Array<GroupTable>): Array<GroupTable> {
        val arrayListOf = arrayListOf<GroupTable>()
        groupTable.forEach {
            arrayListOf.add(disposeReturnData(it))
        }
        return arrayListOf.toTypedArray()
    }


    /**
     * 过滤/处理 数据
     */
     fun disposeReturnData(groupTable: GroupTable): GroupTable {
        // 限制时间
        // /myf-pic-api/
        // /user/img/
        groupTable.groupImg = "/myf-pic-api/${groupTable.groupImg}${
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