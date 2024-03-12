package com.pkpk.join.model

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import com.gitee.sunchenbin.mybatis.actable.annotation.Column
import com.pkpk.join.config.TABLE_GROUP
import java.io.Serializable


/**
 * 用户创建的组表
 *
 *  以后更新
 *  可以加个密码  防止用户乱加入
 *
 */
@TableName(TABLE_GROUP)
data class GroupTable @JvmOverloads constructor(

    @Column(
        comment = "用户ID-谁是创建的",
        isNull = false,
        defaultValue = "0"
    )
    var userId: Long = 0,

    @Column(
        comment = "组图头像",
        isNull = false,
    )
    var groupImg: String = "",

    @Column(
        comment = "名字",
        isNull = false,
        defaultValue = "default"
    )
    var groupName: String = "default",


    @Column(
        comment = "介绍",
        isNull = true,
        defaultValue = "default"
    )
    var groupIntroduce: String? = "default",

    @Column(
        comment = "类型",
        isNull = true,
        defaultValue = "default"
    )
    var groupType: String? = "default",

    // 当前群里的人数
    @TableField(exist = false)
    var groupPeople: Int = 0,

    // 当前群里前4名人的数据
    @TableField(exist = false)
    var groupTopFourPeople: List<UserTable> = arrayListOf(),

    // 当前用户与此群的关系    0 未加入   1 加入不是组管理   2 加入是组管理
    @TableField(exist = false)
    var groupAndUser: Int = 0


): BaseTable(), Serializable