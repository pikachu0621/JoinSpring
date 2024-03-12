package com.pkpk.join.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.gitee.sunchenbin.mybatis.actable.utils.ColumnUtils
import com.pkpk.join.base.BaseServiceImpl
import com.pkpk.join.config.DEVICE_INFO_PARAMETER
import com.pkpk.join.config.TOKEN_PARAMETER
import com.pkpk.join.mapper.UserLogMapper
import com.pkpk.join.model.UserLogTable
import com.pkpk.join.service.*
import com.pkpk.join.utils.AESBCBUtils
import com.pkpk.join.utils.AESBCBUtils.aesDecrypt
import com.pkpk.join.utils.JsonUtil.jsonValue
import com.pkpk.join.utils.OtherUtils
import com.pkpk.join.utils.RequestUtil.urlGet
import com.pkpk.join.utils.SqlUtils.deleteByField
import com.pkpk.join.utils.SqlUtils.queryByFieldList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest


@Service
class UserLogServiceImpl : BaseServiceImpl(), IUserLogService {

    @Autowired
    private lateinit var tokenServiceImpl: TokenServiceImpl

    @Autowired
    private lateinit var userLogMapper: UserLogMapper

    @Autowired
    private lateinit var request: HttpServletRequest


    override fun addLog(userLogTable: UserLogTable) {
        userLogMapper.insert(userLogTable)
    }

    override fun addLog(
        contentText: String?,
        userId: Long,
        logType: Int,
    ) {
        val ipInfo = getIpInfo()
        addLog(
            UserLogTable(
                userId = if(userId == 0L) ipInfo.userId else userId ,
                logType = logType,
                ip = ipInfo.ip,
                ipAddress = ipInfo.ipAddress,
                ipOther = ipInfo.ipOther,
                contentText = contentText,
                deviceInfo = ipInfo.deviceInfo
            )
        )
    }

    override fun addLogNormal(userLogTable: UserLogTable) = addLog(userLogTable.apply { logType = 0 })


    override fun addLogNormal(
        contentText: String?,
        userId: Long,
    ) = addLog(contentText, userId, 0)


    override fun addLogLogin(userLogTable: UserLogTable) = addLog(userLogTable.apply { logType = 1 })

    override fun addLogLogin(
        contentText: String?,
        userId: Long,
    ) = addLog(contentText, userId, 1)


    override fun deleteLog(logId: Long) {
        userLogMapper.deleteById(logId)
    }

    override fun deleteLog(userLogTable: UserLogTable) {
        userLogMapper.deleteById(userLogTable.id)
    }

    override fun deleteUserLog(userId: Long?) {
        userLogMapper.deleteByField(UserLogTable::userId, userId ?: getUserId())
    }

    override fun deleteUserLog(userLogTable: UserLogTable) {
        userLogMapper.deleteById(userLogTable.userId)
    }

    override fun deleteAllLog() {
        userLogMapper.deleteAll()
    }

    override fun queryAllLog() = userLogMapper.queryAll()

    override fun queryLogByUserId(userId: Long) = userLogMapper.queryByFieldList(UserLogTable::userId, userId)

    override fun queryLogById(logId: Long) = userLogMapper.selectById(logId)

    override fun queryLogByType(type: Long) = userLogMapper.queryByFieldList(UserLogTable::logType, type)



    private fun getUserId(): Long {
        val token = OtherUtils.getMustParameter(request, TOKEN_PARAMETER) ?: return 0
        val userId = tokenServiceImpl.queryByToken(token)?.userId ?: 0
        return if (userId < 0) -userId else userId
    }

    private fun getDeviceInfo(): String {
        val deviceInfo = OtherUtils.getMustParameter(request, DEVICE_INFO_PARAMETER) ?: return "unknown"
        val deviceInfoDecrypt = deviceInfo.aesDecrypt() ?: return "unknown"
        return deviceInfoDecrypt
    }

    private data class IpInfo(
        val userId: Long,
        var ip: String = "unknown",
        var ipAddress: String = "unknown",
        var ipOther: String = "unknown",
        var deviceInfo: String = "unknown",
    )

    private fun getIpInfo(): IpInfo {
        val userId = getUserId()
        val deviceInfo = getDeviceInfo()
        val ip = OtherUtils.getRemoteIP(request) ?: "unknown"
        var ipOther = "unknown"
        val ipAddress = if (ip == "unknown") {
            "unknown"
        } else {
            try {
                val urlGet = "http://127.0.0.1:8011/ip/${ip}".urlGet<String>()
                if (urlGet.isNullOrEmpty()) {
                    "unknown"
                } else {
                    ipOther = urlGet.jsonValue("result.subInfo") ?: "unknown"
                    urlGet.jsonValue("result.mainInfo") ?: "unknown"
                }
            } catch (_: Exception) {
                "unknown"
            }
        }
        return IpInfo(userId, ip , ipAddress, ipOther, deviceInfo)
    }
}