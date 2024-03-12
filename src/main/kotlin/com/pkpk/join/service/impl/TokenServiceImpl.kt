package com.pkpk.join.service.impl

import com.pkpk.join.base.BaseServiceImpl
import com.pkpk.join.config.AppConfig
import com.pkpk.join.mapper.TokenTableMapper
import com.pkpk.join.model.TokenTable
import com.pkpk.join.service.ITokenService
import com.pkpk.join.utils.OtherUtils
import com.pkpk.join.utils.SqlUtils.deleteByField
import com.pkpk.join.utils.SqlUtils.queryByFieldOne
import com.pkpk.join.utils.TimeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TokenServiceImpl : BaseServiceImpl(), ITokenService {


    @Autowired
    private lateinit var tokenTableManager: TokenTableMapper

    @Autowired
    private lateinit var appConfig: AppConfig


    override fun put(userId: Long, userAccount: String, userPassword: String, tokenTime: Long): TokenTable {
        val createToken = OtherUtils.createToken(appConfig.appConfigEdit.tokenSalt, "$userId", userAccount, userPassword, "$tokenTime")
        // 删除上一个用户绑定的数据
        tokenTableManager.deleteByField("user_id", userId)
        logi("创建的token $createToken")
        return TokenTable(userId, createToken, tokenTime).apply {
            tokenTableManager.insert(this)
        }
    }


    override fun queryByToken(token: String): TokenTable? {
        return tokenTableManager.queryByFieldOne(TokenTable::tokenLogin, token)
    }


    override fun queryByUserId(userId: Long): TokenTable? {
        return tokenTableManager.queryByFieldOne(TokenTable::userId, userId)
    }

    override fun deleteByToken(token: String) {
        tokenTableManager.deleteByField(TokenTable::tokenLogin, token)
    }

    override fun deleteByUserId(userId: Long) {
        tokenTableManager.deleteByField(TokenTable::userId, userId)
    }


    // 2023-03-07 23:56:00
    // yyyy-MM-dd HH:mm:ss
    // true 有效 false无效
    override fun verify(token: String): Boolean {
        val tokenData = queryByToken(token) ?: return false
        if (tokenData.tokenTime == -1L) return true
        val currentTime = TimeUtils.getCurrentTime()
        val timeDistance = TimeUtils.getTimeDistance(currentTime, tokenData.createTime!!)
        logi("当前时间：$currentTime  token创建时间：${tokenData.createTime}  相差时间s：$timeDistance    token有效时间：${tokenData.tokenTime}")
        return timeDistance <= tokenData.tokenTime
    }

}