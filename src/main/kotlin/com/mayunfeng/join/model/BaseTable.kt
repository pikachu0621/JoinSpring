package com.mayunfeng.join.model

import com.baomidou.mybatisplus.annotation.*
import com.gitee.sunchenbin.mybatis.actable.annotation.Column
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNativeDefValue
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant
import java.io.Serializable


open  class BaseTable(): Serializable {
    @TableId(type = IdType.AUTO)
    @IsKey
    @IsAutoIncrement
    @IsNotNull
    @Column
    var id: Long = 0


    @IsNativeDefValue
    @Column(
        name = "create_time",
        type = MySqlTypeConstant.DATETIME,
        defaultValue = "CURRENT_TIMESTAMP",
        isNull = true,
        comment = "创建时间")
    var createTime: String? = null



    @IsNativeDefValue
    @Column(
        name = "update_time",
        type = MySqlTypeConstant.DATETIME,
        defaultValue = "CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP",
        isNull = true,
        comment = "更新时间")
    var updateTime: String? =  null

    @TableField(exist = false)
    var baseTag: Any? = null
}