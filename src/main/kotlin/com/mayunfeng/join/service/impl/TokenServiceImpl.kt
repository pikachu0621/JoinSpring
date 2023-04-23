package com.mayunfeng.join.service.impl

import com.mayunfeng.join.base.BaseServiceImpl
import com.mayunfeng.join.config.AppConfig
import com.mayunfeng.join.mapper.TokenTableMapper
import com.mayunfeng.join.model.TokenTable
import com.mayunfeng.join.service.ITokenService
import com.mayunfeng.join.utils.OtherUtils
import com.mayunfeng.join.utils.SqlUtils
import com.mayunfeng.join.utils.TimeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.annotation.Resource

@Service
class TokenServiceImpl : BaseServiceImpl(), ITokenService {


    @Autowired
    private lateinit var tokenTableManager: TokenTableMapper

    @Autowired
    private lateinit var APPConfig: AppConfig


    override fun put(userId: Long, userAccount: String, userPassword: String, tokenTime: Long): TokenTable {
        val createToken = OtherUtils.createToken(APPConfig.configSalt, "$userId", userAccount, userPassword, "$tokenTime")
        // 删除上一个用户绑定的数据
        SqlUtils.deleteByField(tokenTableManager, "user_id", userId)
        logi("创建的token $createToken")
        return TokenTable(userId, createToken, tokenTime).apply {
            tokenTableManager.insert(this)
        }
    }


    override fun queryByToken(token: String): TokenTable? {
        return SqlUtils.queryByFieldOne(tokenTableManager, "token_login", token)
    }


    override fun queryByUserId(userId: Long): TokenTable? {
        return SqlUtils.queryByFieldOne(tokenTableManager, "user_id", userId)
    }

    override fun deleteByToken(token: String) {
        SqlUtils.deleteByField(tokenTableManager, "token_login", token)
    }

    override fun deleteByUserId(userId: Long) {
        SqlUtils.deleteByField(tokenTableManager, "user_id", userId)
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