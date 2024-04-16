package com.pkpk.join.model

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import com.fasterxml.jackson.annotation.JsonProperty
import com.gitee.sunchenbin.mybatis.actable.annotation.Column
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant
import com.pkpk.join.config.DEFAULT
import com.pkpk.join.config.TABLE_USER
import java.io.Serializable



enum class UserRank(
    val LV: Int
){
    // 普通用户
    NORMAL(0),
    // 管理员
    ADMIN(1),
    // root 用户
    ROOT(2)
}


/**
 * 用户表
 */
@TableName(TABLE_USER)
data class UserTable(

    //     @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // 不返回给前端
    //     @TableField(exist = false) 不写入数据库

    @Column(
        comment = "用户账号",
        isNull = false
    )
    var userAccount: String = "",


    @Column(
        comment = "用户账号密码",
        isNull = false
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var userPassword: String = "",


    @Column(
        comment = "用户头像",
        isNull = false,
    )
    var userImg: String = "",


    @Column(
        comment = "用户性别  0=false=女   1=true=男",
        type = MySqlTypeConstant.INT,
        isNull = true,
        length = 1,
        defaultValue = "0"
    )
    var userSex: Boolean? = false,


    @Column(
        comment = "用户昵称",
        isNull = true,
        defaultValue = DEFAULT
    )
    var userNickname: String? = DEFAULT,


    @Column(
        comment = "用户 学校/单位",
        isNull = true,
        defaultValue = DEFAULT
    )
    var userUnit: String? = DEFAULT,


    @Column(
        comment = "用户出生日期",
        isNull = true,
        defaultValue = "2000-01-01"
    )
    var userBirth: String = "2000-01-01",

    @TableField(exist = false)
    var userAge: Int? = 0,

    @Column(
        comment = "用户简介",
        isNull = true,
        defaultValue = "这个家伙很懒，什么都没留下 >_<"
    )
    var userIntroduce: String? = "这个家伙很懒，什么都没留下 >_<",

    @Column(
        comment = "用户等级  0 普通用户 1 管理员 2 root管理员",
        isNull = false,
        length = 1,
        defaultValue = "0"
    )
    var userGrade: Int = UserRank.NORMAL.LV,


    @Column(
        comment = "用户是否拉黑  0 = false = 不拉黑  1 = true = 拉黑",
        type = MySqlTypeConstant.INT,
        isNull = false,
        length = 1,
        defaultValue = "0"
    )
    var userLimit: Boolean = false,


    @Column(
        comment = "私密账号  0 = false = 不私密  1 = true = 私密",
        type = MySqlTypeConstant.INT,
        isNull = false,
        length = 1,
        defaultValue = "0"
    )
    var userOpenProfile: Boolean = false,


    @Column(
        comment = "上次登录日志",
        type = MySqlTypeConstant.INT,
        isNull = false,
        defaultValue = "0"
    )
    var lastTimeLoginLog: Long = 0,



    @TableField(exist = false)
    var loginToken: String? = null,

    ) : BaseTable(), Serializable