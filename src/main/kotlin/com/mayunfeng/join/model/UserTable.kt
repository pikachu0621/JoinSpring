package com.mayunfeng.join.model

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import com.gitee.sunchenbin.mybatis.actable.annotation.Column
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant
import java.io.Serializable



/**
 * 用户表
 */
@TableName("myf_user_table")
data class UserTable(


    @Column(
        comment = "用户账号",
        isNull = false
    )
    var userAccount: String = "",


    @Column(
        comment = "用户账号密码",
        isNull = false
    )
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
        comment = "用户姓名",
        isNull = true,
        defaultValue = "x-x-x"
    )
    var userName: String? = "x-x-x",


    @Column(
        comment = "用户 学校/单位",
        isNull = true,
        defaultValue = "x-x-x-x"
    )
    var userUnit: String? = "x-x-x-x",


    @Column(
        comment = "用户年龄",
        isNull = true,
        length = 3,
        defaultValue = "0"
    )
    var userAge: Int = 0,


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
    var userGrade: Int = 0,


    @Column(
        comment = "用户是否拉黑  0 = false = 不拉黑  1 = true = 拉黑",
        type = MySqlTypeConstant.INT,
        isNull = false,
        length = 1,
        defaultValue = "0"
    )
    var userLimit: Boolean = false,


    @TableField(exist = false)
    var loginToken: String? = null
) : BaseTable(), Serializable