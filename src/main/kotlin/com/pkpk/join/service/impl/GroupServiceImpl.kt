package com.pkpk.join.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.pkpk.join.base.BaseServiceImpl
import com.pkpk.join.config.API_PICTURE
import com.pkpk.join.config.AppConfig
import com.pkpk.join.config.DEFAULT
import com.pkpk.join.config.TOKEN_PARAMETER
import com.pkpk.join.mapper.GroupTableMapper
import com.pkpk.join.mapper.JoinGroupTableMapper
import com.pkpk.join.mapper.UserSignTableMapper
import com.pkpk.join.mapper.UserTableMapper
import com.pkpk.join.model.*
import com.pkpk.join.service.*
import com.pkpk.join.utils.JsonResult
import com.pkpk.join.utils.OtherUtils
import com.pkpk.join.utils.OtherUtils.isNumber
import com.pkpk.join.utils.SqlUtils.delImageFile
import com.pkpk.join.utils.SqlUtils.deleteByField
import com.pkpk.join.utils.SqlUtils.F
import com.pkpk.join.utils.SqlUtils.queryByFieldList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest


@Service
class GroupServiceImpl: BaseServiceImpl(), IGroupService {

    @Autowired
    private lateinit var appConfig: AppConfig

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

    @Autowired
    private lateinit var userLogServiceImpl: UserLogServiceImpl

    override fun createGroup(
        name: String,
        img: MultipartFile?,
        type: String?,
        ird: String?,
        search: Boolean?,
        verify: String?
    ): JsonResult<GroupTable> {
        if (OtherUtils.isFieldEmpty(name)) throw ParameterException()
        if (name.length > 20) throw GroupNameLengthException()
        if (!ird.isNullOrEmpty() && ird.length > 100) throw GroupIrdLengthException()
        if (!verify.isNullOrEmpty() && (verify.length > 6 || verify.length < 4)) throw GroupPwsLengthException()
        if (!appConfig.appConfigEdit.groupTypeArr.contains(type)) throw GroupTypeException()

        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val userData = tokenServiceImpl.queryByToken(token)!!
        val groupTable = GroupTable().apply {
            this.userId = userData.userId
            this.groupImg = if (img == null) DEFAULT else pictureServiceImpl.upImage(img).result!!
            this.groupName = name
            this.groupType = if(ird.isNullOrEmpty()) appConfig.appConfigEdit.groupTypeArr[0] else type
            this.groupIntroduce = if(ird.isNullOrEmpty()) DEFAULT else ird
            this.groupIsSearch = search ?: true
            this.groupVerifyPws = verify
        }
        groupTableMapper.insert(groupTable)
        val groupTableInsert = groupTableMapper.selectById(groupTable)
        joinGroupServiceImpl.joinGroup(groupTableInsert.id, verify)
        userLogServiceImpl.addLogNormal("创建组")
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
        groupTableMapper.delImageFile(GroupTable::groupImg, groupTable.groupImg, "${appConfig.configUserImageFilePath()}${groupTable.groupImg}")
        // 删除所有加入此组的用户记录
        joinGroupTableMapper.delete(QueryWrapper<JoinGroupTable>().apply{
            eq(JoinGroupTable::groupId.F(), id)
        })
        groupTableMapper.deleteById(id)
        userLogServiceImpl.addLogNormal("删除组")
        return JsonResult.ok(readUserListGroup(userId))
    }

    // root等级 用户
    override fun deleteGroupByUserId(userId: Long) {
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val user = userTableMapper.selectById(-tokenServiceImpl.queryByToken(token)!!.userId) ?: throw DataNulException()
        if (user.userGrade == UserRank.NORMAL.LV) throw GroupUserAuthorityEditException()
        val queryByFieldList = groupTableMapper.queryByFieldList( GroupTable::userId, userId) ?: return
        // 先删除该用户加入的组
        joinGroupTableMapper.deleteByField(JoinGroupTable::userId, userId)
        // 再删除该用户创建的组
        groupTableMapper.deleteByField(GroupTable::userId, userId)
        // 然后删除该用户的签到任务
        userSignTableMapper.deleteByField(UserSignTable::userId, userId)
        // 最后删除该用户创建的组
        queryByFieldList.forEach {
            groupTableMapper.delImageFile(GroupTable::groupImg, it.groupImg, "${appConfig.configUserImageFilePath()}${it.groupImg}")
            // 删除所有加入此组的用户记录
            joinGroupTableMapper.deleteByField(JoinGroupTable::groupId, it.id)
            groupTableMapper.deleteById(it.id)
        }
    }


    override fun editUserGroup(
        id: Long,
        img: MultipartFile?,
        name: String?,
        type: String?,
        ird: String?,
        search: Boolean?,
        verify: String?
    ): JsonResult<GroupTable> {
        if (OtherUtils.isFieldEmpty(id)) throw ParameterException()
        if (!name.isNullOrEmpty() && name.length > 20) throw GroupNameLengthException()
        if (!ird.isNullOrEmpty() && ird.length > 100) throw GroupIrdLengthException()
        if (!verify.isNullOrEmpty() && (verify.length > 6 || verify.length < 4)) throw GroupPwsLengthException()
        if (!type.isNullOrEmpty() && !appConfig.appConfigEdit.groupTypeArr.contains(type)) throw GroupTypeException()
        verifyGroup(id)
        val groupTable = groupTableMapper.selectById(id).apply {
            if (img != null){
                // 删除只有本人绑定的图片数据
                groupTableMapper.delImageFile(GroupTable::groupImg, this.groupImg, "${appConfig.configUserImageFilePath()}${this.groupImg}")
                this.groupImg = pictureServiceImpl.upImage(img).result!!
            }
            if (!name.isNullOrEmpty()) this.groupName = name
            if (!type.isNullOrEmpty()) this.groupType = type
            if (!ird.isNullOrEmpty()) this.groupIntroduce = ird
            if (!verify.isNullOrEmpty()) this.groupVerifyPws = verify
            if (search != null) this.groupIsSearch = search
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
                put(JoinGroupTable::groupId.F(), byGroupId)
                put(JoinGroupTable::userId.F(), targetUserId)
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

    override fun faceToFaceAddGroup(longitude: Long, latitude: Long, password: Long) {

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
            val userData =  userTableMapper.selectById(-userId) ?: throw DataNulException()
            if (userData.userGrade == UserRank.ROOT.LV) return userId
        }
        // 越权判断
        if (groupTable.userId != userId) throw GroupUserAuthorityEditException()
        return userId
    }


    /**
     * 获取该用户创建的所有群
     */
    fun readUserListGroup(userId: Long): Array<GroupTable> {
        var queryByFieldList = groupTableMapper.queryByFieldList(GroupTable::userId, userId)
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
        // /pk-pic-api/
        // /user/img/
        fun imageTimeAes() = if (appConfig.appConfigEdit.imageTime != -1L) {
            val createTimeAESBCB = OtherUtils.createTimeAESBCB(appConfig.appConfigEdit.tokenSalt)
            "?c=$createTimeAESBCB"
        } else ""

        groupTable.groupImg =
            if (groupTable.groupImg == DEFAULT) "${API_PICTURE}/static/${appConfig.configDefaultPicGroup}${imageTimeAes()}"
            else "${API_PICTURE}/${groupTable.groupImg}${imageTimeAes()}"
        // 该组人数
        // 该组前4名
        // 用户是否加入该组
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val userId = tokenServiceImpl.queryByToken(token)!!.userId
        groupTable.groupIsVerify = !groupTable.groupVerifyPws.isNullOrEmpty()
        if (groupTable.userId == userId){
            groupTable.groupAndUser = 2
        } else {
            groupTable.groupAndUser =  if (joinGroupServiceImpl.verifyJoinGroupByUserId(groupTable.id)) 1 else 0
            // 不是管理员
            groupTable.groupVerifyPws = null
            groupTable.groupIsSearch = false
        }
        groupTable.groupTopFourPeople = joinGroupServiceImpl.queryJoinTopFourPeople(groupTable.id)
        groupTable.groupPeople = joinGroupServiceImpl.getJoinUserNum(groupTable.id)
        return groupTable
    }


}