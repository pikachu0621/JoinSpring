package com.pkpk.join.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.pkpk.join.base.BaseServiceImpl
import com.pkpk.join.config.AppConfig
import com.pkpk.join.config.TOKEN_PARAMETER
import com.pkpk.join.mapper.GroupTableMapper
import com.pkpk.join.mapper.JoinGroupTableMapper
import com.pkpk.join.mapper.UserTableMapper
import com.pkpk.join.model.GroupTable
import com.pkpk.join.model.JoinGroupTable
import com.pkpk.join.model.LGroupBean
import com.pkpk.join.model.UserTable
import com.pkpk.join.service.*
import com.pkpk.join.utils.JsonResult
import com.pkpk.join.utils.OtherUtils
import com.pkpk.join.utils.SqlUtils.F
import com.pkpk.join.utils.SqlUtils.queryByFieldList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

@Service
class JoinGroupServiceImpl:  BaseServiceImpl(), IJoinGroupService {


    @Autowired
    private lateinit var tokenServiceImpl: TokenServiceImpl

    @Autowired
    private lateinit var request: HttpServletRequest

    @Autowired
    private lateinit var groupTableMapper: GroupTableMapper

    @Autowired
    private lateinit var groupServiceImpl: GroupServiceImpl

    @Autowired
    private lateinit var joinGroupTableMapper: JoinGroupTableMapper

    @Autowired
    private lateinit var userServiceImpl: UserServiceImpl

    @Autowired
    private lateinit var userTableMapper: UserTableMapper

    @Autowired
    private lateinit var appConfig: AppConfig

    @Autowired
    private lateinit var userLogServiceImpl: UserLogServiceImpl


    override fun joinGroup(groupId: Long, groupVerifyCode: String?): JsonResult<GroupTable> {
        if (OtherUtils.isFieldEmpty(groupId)) throw ParameterException()
        val groupTable = groupTableMapper.selectById(groupId) ?: throw GroupNulException()
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val userId = tokenServiceImpl.queryByToken(token)!!.userId
        if (verifyJoinGroupByUserId(groupId))  throw JoinGroupOkException()

        if (!groupTable.groupVerifyPws.isNullOrEmpty()) {
            if (groupTable.groupVerifyPws != groupVerifyCode){
                throw GroupPwsErrorException()
            }
        }
        joinGroupTableMapper.insert(JoinGroupTable().apply {
            this.userId = userId
            this.groupId = groupId
            this.groupAdministrator = false
        })
        userLogServiceImpl.addLogNormal("加入组")
        return groupServiceImpl.queryGroupInfoById(groupId)
    }




    override fun outGroup(groupId: Long?): JsonResult<String> {
        if (OtherUtils.isFieldEmpty(groupId)) throw ParameterException()
        groupTableMapper.selectById(groupId) ?: throw GroupNulException()
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val userId = tokenServiceImpl.queryByToken(token)!!.userId
        if (!verifyJoinGroupByUserId(groupId!!))  throw JoinGroupNoException()
        joinGroupTableMapper.delete(QueryWrapper<JoinGroupTable>().apply {
            allEq(hashMapOf<String, Any>().apply {
                put(JoinGroupTable::groupId.F(), groupId)
                put(JoinGroupTable::userId.F(), userId)
            })
        })
        userLogServiceImpl.addLogNormal("退出组")
        return JsonResult.ok("ok")
    }

    override fun queryUserJoinGroup(): JsonResult<Array<GroupTable>> {
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val userId = tokenServiceImpl.queryByToken(token)!!.userId
        val joinGroupTables = joinGroupTableMapper.queryByFieldList(JoinGroupTable::userId, userId)
        if (joinGroupTables.isNullOrEmpty()) {
            return JsonResult.ok(arrayOf())
        }
        val groupTables = arrayListOf<GroupTable>()
        joinGroupTables.forEach {
            groupTables.add(groupServiceImpl.queryGroupInfoById(it.groupId).result!!)
        }
        return JsonResult.ok(groupTables.toTypedArray())
    }


    override fun queryJoinGroupAllUser(groupId: Long?): JsonResult<LGroupBean<Array<UserTable>>> {
        if (OtherUtils.isFieldEmpty(groupId)) throw ParameterException()
        val groupTable = groupTableMapper.selectById(groupId) ?: throw GroupNulException()
        val queryByFieldList = joinGroupTableMapper.queryAllJoinGroupUser(groupId!!)
        if (queryByFieldList.isNullOrEmpty()) return JsonResult.ok(LGroupBean(false, arrayOf()))
        val arr = arrayListOf<UserTable>()
        queryByFieldList.forEach {
            arr.add(userServiceImpl.disposeReturnUserData(userTableMapper.selectById(it.userId), true))
        }
        // 判断否为创建者
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val userId = tokenServiceImpl.queryByToken(token)!!.userId
        return JsonResult.ok(LGroupBean( groupTable.userId == userId , arr.toTypedArray()))
    }





    override fun getJoinUserNum(groupId: Long): Int {
        groupTableMapper.selectById(groupId) ?: return 0
        val queryByFieldList = joinGroupTableMapper.queryByFieldList(JoinGroupTable::groupId, groupId)
        if (queryByFieldList.isNullOrEmpty()) return 0
        return queryByFieldList.size
    }


    override fun queryJoinTopFourPeople(groupId: Long): List<UserTable> {
        val usTabs = arrayListOf<UserTable>()
        groupTableMapper.selectById(groupId) ?: return usTabs
        val joinGroups = joinGroupTableMapper.queryAllJoinGroupUser(groupId)
        if (joinGroups.isNullOrEmpty()) return usTabs
        for (i in joinGroups.indices){
            usTabs.add(userServiceImpl.disposeReturnUserData(userTableMapper.selectById(joinGroups[i].userId), true))
            if (i >= 3) break
        }
        return usTabs
    }



    // 用户是否已加入该组
    // true 加入
    override fun verifyJoinGroupByUserId(groupId: Long, userId: Long): Boolean {
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        var userIdTow = userId
        if (userIdTow == -1L){
            userIdTow = tokenServiceImpl.queryByToken(token)!!.userId
        }
        val selectList = joinGroupTableMapper.selectList(QueryWrapper<JoinGroupTable>().apply {
            allEq(hashMapOf<String, Any>().apply {
                put(JoinGroupTable::groupId.F(), groupId)
                put(JoinGroupTable::userId.F(), userIdTow)
            })
        })
        return !selectList.isNullOrEmpty()
    }


}