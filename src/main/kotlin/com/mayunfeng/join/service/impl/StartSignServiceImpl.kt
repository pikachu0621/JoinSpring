package com.mayunfeng.join.service.impl

import com.mayunfeng.join.base.BaseServiceImpl
import com.mayunfeng.join.config.TOKEN_PARAMETER
import com.mayunfeng.join.mapper.StartSignTableMapper
import com.mayunfeng.join.model.StartSignTable
import com.mayunfeng.join.service.*
import com.mayunfeng.join.utils.JsonResult
import com.mayunfeng.join.utils.OtherUtils
import com.mayunfeng.join.utils.SqlUtils
import com.mayunfeng.join.utils.TimeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Time
import java.util.UUID
import javax.servlet.http.HttpServletRequest

@Service
class StartSignServiceImpl : BaseServiceImpl(), IStartSignService {


    @Autowired
    private lateinit var tokenServiceImpl: TokenServiceImpl

    @Autowired
    private lateinit var groupServiceImpl: GroupServiceImpl

    @Autowired
    private lateinit var userSignServiceImpl: UserSignServiceImpl

    @Autowired
    private lateinit var joinGroupServiceImpl: JoinGroupServiceImpl

    @Autowired
    private lateinit var userServiceImpl: UserServiceImpl

    @Autowired
    private lateinit var request: HttpServletRequest

    @Autowired
    private lateinit var startSignTableMapper: StartSignTableMapper


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

        val queryJoinGroupAllUser = joinGroupServiceImpl.queryJoinGroupAllUser(groupId)
        if ((queryJoinGroupAllUser.result!!.result!!.size <= 1)) throw StartSignNulAddUserEditException()

        if (signType == 2) {
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
        userSignServiceImpl.notifyUserSign(groupId, startSignTable.id)
        return JsonResult.ok(startSignTableMapper.selectById(startSignTable))
    }


    override fun getSignAllInfoListByUserId(): JsonResult<Array<StartSignTable>> {
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)
        val queryByToken = tokenServiceImpl.queryByToken(token!!)
        val queryByFieldList =
            startSignTableMapper.querySignAllInfoUserId(queryByToken!!.userId, "desc") ?: arrayListOf()
        queryByFieldList.forEach { dataIntegration(it) }
        return JsonResult.ok(queryByFieldList.toTypedArray())
    }


    override fun queryStartSignInfoById(signId: Long): StartSignTable {
        val startSignTable = startSignTableMapper.selectById(signId) ?: throw StartSignNulException()
        return dataIntegration(startSignTable)
    }


    override fun delSign(signId: Long): JsonResult<Array<StartSignTable>> {
        verifySign(signId)
        userSignServiceImpl.delUserSignBySign(signId)
        startSignTableMapper.deleteById(signId)
        return getSignAllInfoListByUserId()
    }


    /**
     * 验证该用户 是否有该签到操作权限
     *
     *  @param  signId
     *
     * 返回 user id
     */
    fun verifySign(signId: Long): Long {
        val startSignTable = startSignTableMapper.selectById(signId) ?: throw StartSignNulException()
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER)!!
        val userId = tokenServiceImpl.queryByToken(token)!!.userId
        if (startSignTable.userId != userId) throw StartSignUserAuthorityEditException()
        return userId
    }


    fun dataIntegration(startSignTable: StartSignTable): StartSignTable {

        val totalNumberOfPeople = userSignServiceImpl.getTotalNumberOfPeople(startSignTable.id)
        val numberOfPeopleCompleted = userSignServiceImpl.getNumberOfPeopleCompleted(startSignTable.id)
        startSignTable.signAllPeople = totalNumberOfPeople
        startSignTable.signHaveCompletedPeople = numberOfPeopleCompleted
        startSignTable.signNotCompletedPeople = totalNumberOfPeople - numberOfPeopleCompleted
        startSignTable.userTable = userServiceImpl.userInfoById(startSignTable.userId)

        if (startSignTable.signTime != -1L){
            if (!startSignTable.signExpire){
                val timeDistance = TimeUtils.getTimeDistance(TimeUtils.getCurrentTime(), startSignTable.createTime!!)
                startSignTable.signExpire = timeDistance > startSignTable.signTime
                if (!startSignTable.signExpire){
                    startSignTable.signTimeRemaining = startSignTable.signTime - timeDistance
                }
                if (startSignTable.signExpire) {
                    startSignTableMapper.updateById(startSignTable)
                }
            }
        }

        try {
            startSignTable.signGroupInfo = groupServiceImpl.queryGroupInfoById(startSignTable.groupId).result
        } catch (_: Exception) { }
        return startSignTable
    }

}