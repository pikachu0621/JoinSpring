package com.mayunfeng.join.model

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
        isNull = true,
        defaultValue = "x-x-x"
    )
    var groupName: String? = "x-x-x",


    @Column(
        comment = "介绍",
        isNull = true,
        defaultValue = "x-x-x"
    )
    var groupIntroduce: String? = "x-x-x",

): BaseTable(), Serializable