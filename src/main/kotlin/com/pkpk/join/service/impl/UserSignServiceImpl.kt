package com.pkpk.join.service.impl

import com.pkpk.join.base.BaseServiceImpl
import com.pkpk.join.config.AppConfig
import com.pkpk.join.config.TOKEN_PARAMETER
import com.pkpk.join.handler.UserWebSocketHandler
import com.pkpk.join.mapper.UserSignTableMapper
import com.pkpk.join.model.UserSignAndStartSign
import com.pkpk.join.model.UserSignTable
import com.pkpk.join.service.*
import com.pkpk.join.utils.JsonResult
import com.pkpk.join.utils.OtherUtils
import com.pkpk.join.utils.SqlUtils.deleteByField
import com.pkpk.join.utils.SqlUtils.queryByFieldList
import com.pkpk.join.utils.TimeUtils
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
    private lateinit var appConfig: AppConfig

    @Autowired
    private lateinit var userLogServiceImpl: UserLogServiceImpl

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
        // 通知创建者有用户签到
        userWebSocketHandler.sendMessageToUserId(true, queryStartSignInfoById.userId)
        userLogServiceImpl.addLogNormal("签到")
        return JsonResult.ok(true)
    }

    override fun queryAllBySignId(signId: Long): JsonResult<UserSignAndStartSign> {
        if (OtherUtils.isFieldEmpty(signId)) throw ParameterException()
        startSignServiceImpl.verifySign(signId)
        val queryByFieldList = userSignTableMapper.queryByFieldList(UserSignTable::signId, signId) ?: arrayListOf()
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

    override fun queryUserAllSign(): JsonResult<Array<UserSignTable>> {
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val userId = tokenServiceImpl.queryByToken(token)!!.userId
        val userSignTables = userSignTableMapper.queryToBeAll(userId) ?: arrayListOf()
        val userSignTablesDispose1 = arrayListOf<UserSignTable>()  // 进行中
/*        val userSignTablesDispose2 = arrayListOf<UserSignTable>()  // 已完成
        val userSignTablesDispose3 = arrayListOf<UserSignTable>()  // 未完成*/
        userSignTables.forEach {
            val queryStartSignInfoById = startSignServiceImpl.queryStartSignInfoById(it.signId)
            it.startSignInfo = queryStartSignInfoById
            userSignTablesDispose1.add(it)
            /*if (it.signComplete){
                userSignTablesDispose2.add(it)
            } else if (queryStartSignInfoById.signExpire) {
                userSignTablesDispose3.add(it)
            } else {
                userSignTablesDispose1.add(it)
            }*/
        }
/*        userSignTablesDispose1.addAll(userSignTablesDispose2)
        userSignTablesDispose1.addAll(userSignTablesDispose3)*/
        return JsonResult.ok(userSignTablesDispose1.toTypedArray())
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
                // 通知用户签到
                userWebSocketHandler.sendMessageToUserId(settleUserSign(userSignTable), it.id)
            }
        }
    }


    override fun delUserSignBySign(signId: Long) {
        startSignServiceImpl.verifySign(signId)
        userSignTableMapper.deleteByField( "sign_id", signId)
    }


    /**
     * 获取总人数
     */
    fun getTotalNumberOfPeople(signId: Long): Int {
        val queryByFieldList = userSignTableMapper.queryByFieldList(UserSignTable::signId, signId)
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