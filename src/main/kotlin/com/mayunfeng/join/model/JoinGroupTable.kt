package com.mayunfeng.join.model

import com.baomidou.mybatisplus.annotation.TableName
import com.gitee.sunchenbin.mybatis.actable.annotation.Column
import java.io.Serializable

@TableName("myf_join_group_table")
data class JoinGroupTable (

    @Column(
        comment = "用户ID-属于哪个用户的数据",
        isNull = false,
        defaultValue = "0"
    )
    var userId: Long = 0,

    @Column(
        comment = "组的ID-加入到哪个组",
        isNull = false,
    )
    var groupId: Long = 0,

    @Column(
        comment = "是否是该组管理员",
        isNull = false,
    )
    var groupAdministrator: Boolean = false,

): BaseTable(), Serializable


data class LGroupBean<T>(
    // 是否为此组创建者
    val groupUserIsFounder: Boolean = false,
    var result: T?
): Serializable
