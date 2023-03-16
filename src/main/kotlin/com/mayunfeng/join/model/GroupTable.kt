package com.mayunfeng.join.model

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import com.gitee.sunchenbin.mybatis.actable.annotation.Column
import java.io.Serializable


/**
 * 用户创建的组表
 *
 *
 */
@TableName("myf_group_table")
data class GroupTable(

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
    var groupPeople: Int = 0

): BaseTable(), Serializable