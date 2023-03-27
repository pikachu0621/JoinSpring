package com.mayunfeng.join.model

import com.baomidou.mybatisplus.annotation.TableName
import com.gitee.sunchenbin.mybatis.actable.annotation.Column
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant
import java.io.Serializable

@TableName("myf_start_sign_table")
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
    var signTitle: String? = "default",


    @Column(
        comment = "内容",
        isNull = true,
        defaultValue = "default"
    )
    var signContent: String? = "default",


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
    var signKey: String? = "-1",


    @Column(
        comment = "有效时长 单位秒(s)   -1 永久",
        isNull = true,
        defaultValue = "-1"
    )
    var signTime: Long? = -1,


    @Column(
        comment = "map位置   [x(圆心经度),y(圆心维度),r(半径)]  -1不限位置",
        isNull = true,
        defaultValue = "-1"
    )
    var signMap: String? = "-1",


    ) : BaseTable(), Serializable