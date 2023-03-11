package com.mayunfeng.join.model

import com.baomidou.mybatisplus.annotation.TableName
import com.gitee.sunchenbin.mybatis.actable.annotation.Column
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant
import java.io.Serializable


/**
 * token 表
 */
@TableName("myf_token_table")
data class TokenTable(

    @Column(
        comment = "属于谁用户表id",
        isNull = false,
        defaultValue = "0"
    )
    var userId: Long = 0,

    @Column(
        comment = "token MD5 唯一",
        isNull = false,
        defaultValue = "-1"
    )
    var tokenLogin: String =  "",


    @Column(
        comment = "有效时长 单位秒(20s)   -1 永久",
        isNull = false,
        defaultValue = "20"
    )
    var tokenTime: Long =  -1,


    @Column(
        comment = "是否失效",
        isNull = false,
        type = MySqlTypeConstant.INT,
        length = 1,
        defaultValue = "0"
    )
    var tokenFailure: Boolean =  false,

): BaseTable(), Serializable