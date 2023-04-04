package com.mayunfeng.join.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper
import com.mayunfeng.join.base.BaseServiceImpl
import com.mayunfeng.join.config.TOKEN_PARAMETER
import com.mayunfeng.join.handler.UserWebSocketHandler
import com.mayunfeng.join.mapper.StartSignTableMapper
import com.mayunfeng.join.mapper.UserSignTableMapper
import com.mayunfeng.join.model.UserSignAndStartSign
import com.mayunfeng.join.model.UserSignTable
import com.mayunfeng.join.service.*
import com.mayunfeng.join.utils.JsonResult
import com.mayunfeng.join.utils.OtherUtils
import com.mayunfeng.join.utils.SqlUtils
import com.mayunfeng.join.utils.TimeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest


@Service
class UserSignServiceImpl : BaseServiceImpl(), IUserSignService {

    @Autowired
    private lateinit var tokenServiceImpl: TokenServiceImpl

    @Autowired
    private lateinit var groupServiceImpl: GroupServiceImpl

    @Autowired
    private lateinit var joinGroupServiceImpl: JoinGroupServiceImpl

    @Autowired
    private lateinit var startSignServiceImpl: StartSignServiceImpl

    @Autowired
    private lateinit var userServiceImpl: UserServiceImpl

    @Autowired
    private lateinit var userWebSocketHandler: UserWebSocketHandler

    @Autowired
    private lateinit var userSignTableMapper: UserSignTableMapper

    @Autowired
    private lateinit var request: HttpServletRequest




    override fun startUserSign(signId: Long, key: String?): JsonResult<Boolean> {
        if (OtherUtils.isFieldEmpty(signId)) throw ParameterException()
        val queryStartSignInfoById = startSignServiceImpl.queryStartSignInfoById(signId)
        if (queryStartSignInfoById.signExpire) throw StartSignThisCheckInHasEndedException()
        if (queryStartSignInfoById.signType >= 1 ){
            if(queryStartSignInfoById.signKey != key) throw StartSignKeyException()
        }
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val queryToSign = userSignTableMapper.queryToSign(tokenServiceImpl.queryByToken(token)!!.userId, signId) ?: throw StartSignDelException()
        queryToSign.signComplete = true
        queryToSign.signTime = TimeUtils.getCurrentTime()
        userSignTableMapper.updateById(queryToSign)
        return JsonResult.ok(true)
    }

    override fun queryAllBySignId(signId: Long): JsonResult<UserSignAndStartSign> {
        if (OtherUtils.isFieldEmpty(signId)) throw ParameterException()
        startSignServiceImpl.verifySign(signId)
        val queryByFieldList = SqlUtils.queryByFieldList(userSignTableMapper, "sign_id", signId)
        if (queryByFieldList.isNullOrEmpty()) throw DataNulException()
        queryByFieldList.forEach {
            it.userTable = userServiceImpl.userInfoById(it.userId)
        }
        return JsonResult.ok(
            UserSignAndStartSign(
                queryByFieldList,
                startSignServiceImpl.queryStartSignInfoById(signId)
            )
        )
    }

    override fun queryUserSign(): JsonResult<Array<UserSignTable>> {
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val userId = tokenServiceImpl.queryByToken(token)!!.userId
        val userSignTables = userSignTableMapper.queryToBeCompleted(userId) ?: arrayListOf()
        val userSignTablesDispose = arrayListOf<UserSignTable>()
        userSignTables.forEach {
            val queryStartSignInfoById = startSignServiceImpl.queryStartSignInfoById(it.signId)
            if (!queryStartSignInfoById.signExpire) {
                it.startSignInfo = queryStartSignInfoById
                userSignTablesDispose.add(it)
            }
        }
        return JsonResult.ok(userSignTablesDispose.toTypedArray())
    }


    fun settleUserSign(userSignTable: UserSignTable): UserSignTable {
        val queryStartSignInfoById = startSignServiceImpl.queryStartSignInfoById(userSignTable.signId)
        if (!queryStartSignInfoById.signExpire) {
            userSignTable.startSignInfo = queryStartSignInfoById
        }
        return userSignTable
    }

    override fun notifyUserSign(groupId: Long, signId: Long) {
        if (OtherUtils.isFieldEmpty(groupId, signId)) throw ParameterException()
        val adminUserId = groupServiceImpl.verifyGroup(groupId)
        startSignServiceImpl.verifySign(signId)
        val queryJoinGroupAllUser = joinGroupServiceImpl.queryJoinGroupAllUser(groupId).result!!.result!!
        if ((queryJoinGroupAllUser.size <= 1)) throw StartSignNulAddUserEditException()
        queryJoinGroupAllUser.forEach {
            if (it.id != adminUserId) { // 创建者不发起
                val userSignTable = UserSignTable().apply {
                    this.userId = it.id
                    this.signId = signId
                }
                userSignTableMapper.insert(userSignTable)
                // 通知用户
                userWebSocketHandler.sendMessageToUserId(settleUserSign(userSignTable), it.id)
            }
        }
    }


    override fun delUserSignBySign(signId: Long) {
        startSignServiceImpl.verifySign(signId)
        SqlUtils.deleteByField(userSignTableMapper, "sign_id", signId)
    }


    /**
     * 获取总人数
     */
    fun getTotalNumberOfPeople(signId: Long): Int {
        val queryByFieldList = SqlUtils.queryByFieldList(userSignTableMapper, "sign_id", signId)
        if (queryByFieldList.isNullOrEmpty()) return 0
        return queryByFieldList.size
    }


    /**
     * 获取已完成人数
     */
    fun getNumberOfPeopleCompleted(signId: Long): Int {
        return userSignTableMapper.queryPeopleCompleted(signId)
    }


}