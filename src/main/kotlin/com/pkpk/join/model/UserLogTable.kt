package com.pkpk.join.model

import com.baomidou.mybatisplus.annotation.TableName
import com.gitee.sunchenbin.mybatis.actable.annotation.Column
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant
import com.pkpk.join.config.TABLE_USER_LOGIN_LOG

enum class LogRank(
    val LV: Int
){
    // 普通日志
    NORMAL(0),
    // 登录日志
    LOGIN(1)
}


@TableName(TABLE_USER_LOGIN_LOG)
data class UserLogTable(

    @Column(
        comment = "用户ID",
        isNull = false,
        defaultValue = "0",
    )
    var userId: Long = 0,


    /**
     * 参考
     * [LogRank]
     */
    @Column(
        comment = "日志类型: 0 = 普通日志，1 = 登录日志",
        isNull = false,
        defaultValue = "0",
        length = 1
    )
    var logType: Int = LogRank.NORMAL.LV,

    @Column(
        comment = "ip",
        isNull = false,
        defaultValue = "0.0.0.0"
    )
    var ip: String = "0.0.0.0",

    @Column(
        comment = "IP地址",
        isNull = true
    )
    var ipAddress: String? = null,

    @Column(
        comment = "ip其他信息",
        isNull = true
    )
    var ipOther: String? = null,

    @Column(
        comment = "log 内容",
        isNull = true
    )
    var contentText: String? = null,

    @Column(
        comment = "设备信息 json",
        isNull = true,
        type = MySqlTypeConstant.TEXT
    )
    var deviceInfo: String? = null,

): BaseTable()
