package com.mayunfeng.join.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.mayunfeng.join.base.BaseServiceImpl
import com.mayunfeng.join.config.TOKEN_PARAMETER
import com.mayunfeng.join.mapper.GroupTableMapper
import com.mayunfeng.join.mapper.JoinGroupTableMapper
import com.mayunfeng.join.mapper.UserTableMapper
import com.mayunfeng.join.model.GroupTable
import com.mayunfeng.join.model.JoinGroupTable
import com.mayunfeng.join.model.LGroupBean
import com.mayunfeng.join.model.UserTable
import com.mayunfeng.join.service.*
import com.mayunfeng.join.utils.JsonResult
import com.mayunfeng.join.utils.OtherUtils
import com.mayunfeng.join.utils.SqlUtils
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


    override fun joinGroup(groupId: Long?): JsonResult<GroupTable> {
        if (OtherUtils.isFieldEmpty(groupId)) throw ParameterException()
        groupTableMapper.selectById(groupId) ?: throw GroupNulException()
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val userId = tokenServiceImpl.queryByToken(token)!!.userId
        if (verifyJoinGroupByUserId(groupId!!))  throw JoinGroupOkException()
        joinGroupTableMapper.insert(JoinGroupTable().apply {
            this.userId = userId
            this.groupId = groupId
            this.groupAdministrator = false
        })
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
                put("group_id", groupId)
                put("user_id", userId)
            })
        })
        return JsonResult.ok("ok")
    }

    override fun queryUserJoinGroup(): JsonResult<Array<GroupTable>> {
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val userId = tokenServiceImpl.queryByToken(token)!!.userId
        val joinGroupTables = SqlUtils.queryByFieldList(joinGroupTableMapper, "user_id", userId)
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
        val queryByFieldList = SqlUtils.queryByFieldList(joinGroupTableMapper, "group_id", groupId!!)
        if (queryByFieldList.isNullOrEmpty()) return JsonResult.ok(LGroupBean(false, arrayOf()))
        val arr = arrayListOf<UserTable>()
        queryByFieldList.forEach {
            arr.add(userServiceImpl.disposeReturnUserData(userTableMapper.selectById(it.userId)))
        }
        // 判断否为创建者
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val userId = tokenServiceImpl.queryByToken(token)!!.userId
        return JsonResult.ok(LGroupBean( groupTable.userId == userId , arr.asReversed().toTypedArray()))
    }





    override fun getJoinUserNum(groupId: Long): Int {
        groupTableMapper.selectById(groupId) ?: return 0
        val queryByFieldList = SqlUtils.queryByFieldList(joinGroupTableMapper, "group_id", groupId)
        if (queryByFieldList.isNullOrEmpty()) return 0
        return queryByFieldList.size
    }


    override fun queryJoinTopFourPeople(groupId: Long): List<UserTable> {
        val usTabs = arrayListOf<UserTable>()
        groupTableMapper.selectById(groupId) ?: return usTabs
        var joinGroups = SqlUtils.queryByFieldList(joinGroupTableMapper, "group_id", groupId)
        if (joinGroups.isNullOrEmpty()) return usTabs
        joinGroups = joinGroups.asReversed()
        for (i in joinGroups.indices){
            usTabs.add(userServiceImpl.disposeReturnUserData(userTableMapper.selectById(joinGroups[i].userId)))
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
                put("group_id", groupId)
                put("user_id", userIdTow)
            })
        })
        return !selectList.isNullOrEmpty()
    }


}