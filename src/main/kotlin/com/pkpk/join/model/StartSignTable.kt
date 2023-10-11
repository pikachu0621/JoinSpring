package com.pkpk.join.model

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import com.gitee.sunchenbin.mybatis.actable.annotation.Column
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant
import java.io.Serializable

@TableName("pk_start_sign_table")
data class StartSignTable(


    @Column(
        comment = "用户ID-哪个用户发起的",
        isNull = false,
        defaultValue = "0"
    )
    var userId: Long = 0,

    @Column(
        comment = "组的ID-在哪个组发起的",
        isNull = false,
    )
    var groupId: Long = 0,


    @Column(
        comment = "标题",
        isNull = true,
        defaultValue = "default"
    )
    var signTitle: String = "default",


    @Column(
        comment = "内容",
        isNull = true,
        defaultValue = "default"
    )
    var signContent: String = "default",


    @Column(
        comment = "类型",  // 0 无密码打卡   1 签到码打卡    2 二维码打卡     3 手势打卡
        isNull = false,
        type = MySqlTypeConstant.INT,
        length = 1,
        defaultValue = "0"
    )
    var signType: Int = 0,


    @Column(
        comment = "key-密码   -1 无密码",
        isNull = true,
        defaultValue = "-1"
    )
    var signKey: String = "-1",


    @Column(
        comment = "有效时长 单位秒(s)   -1 永久",
        isNull = true,
        defaultValue = "-1"
    )
    var signTime: Long = -1,




    @Column(
        comment = "签到率 用于记录最后签到率(失效时)，动态生成会受退群影响",
        isNull = false,
        type = MySqlTypeConstant.INT,
        length = 3,
        defaultValue = "0"
    )
    var signRatio: Int = 0,



    @Column(
        comment = "是否过期",   // 1 过期   0 未过期
        isNull = false,
        type = MySqlTypeConstant.INT,
        length = 1,
        defaultValue = "0"
    )
    var signExpire: Boolean = false,


    @Column(
        comment = "map位置   [x(圆心经度),y(圆心维度),r(半径)]  -1不限位置",
        isNull = true,
        defaultValue = "-1"
    )
    var signMap: String = "-1",


    @TableField(exist = false)
    var signGroupInfo: GroupTable? = null,

    // 发起者 User
    @TableField(exist = false)
    var userTable: UserTable = UserTable(),

    // 已完成人数
    @TableField(exist = false)
    var signHaveCompletedPeople: Int = 0,

    // 未完成人数
    @TableField(exist = false)
    var signNotCompletedPeople: Int = 0,


    // 总人数
    @TableField(exist = false)
    var signAllPeople: Int = 0,

    // 剩余时间 0 为已结束
    @TableField(exist = false)
    var signTimeRemaining: Long = 0

    ) : BaseTable(), Serializable