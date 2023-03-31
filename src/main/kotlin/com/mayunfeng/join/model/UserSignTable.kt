package com.mayunfeng.join.model

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import com.fasterxml.jackson.annotation.JsonProperty
import com.gitee.sunchenbin.mybatis.actable.annotation.Column
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNativeDefValue
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant
import java.io.Serializable

/**
 * 用户待签到表
 *
 */
@TableName("myf_user_sign_table")
class UserSignTable(


    @Column(
        comment = "用户ID-谁的数据",
        isNull = false,
        defaultValue = "0"
    )
    var userId: Long = 0,

    @Column(
        comment = "签到ID-属于那个签到的",
        isNull = false,
    )
    var signId: Long = 0,


    @Column(
        comment = "签到是否完成",   // 1 完成   0 未完成  true = 1
        isNull = false,
        type = MySqlTypeConstant.INT,
        length = 1,
        defaultValue = "0"
    )
    var signComplete: Boolean = false,


    @Column(
        comment = "签到时间",
        defaultValue = "",
        isNull = true
    )
    var signTime: String? = "",


    // 用户数据
    @TableField(exist = false)
    var userTable: UserTable? = null,

) : BaseTable(), Serializable


data class UserSignAndStartSign(
    val userSignTable: ArrayList<UserSignTable>,
    val startSignTable: StartSignTable,
):  Serializable
