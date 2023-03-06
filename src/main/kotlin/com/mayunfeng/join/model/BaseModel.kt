package com.mayunfeng.join.model

import com.baomidou.mybatisplus.annotation.*
import com.fasterxml.jackson.annotation.JsonFormat
import com.gitee.sunchenbin.mybatis.actable.annotation.Column
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNativeDefValue
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant
import com.mayunfeng.join.utils.TimeUtils
import org.springframework.format.annotation.DateTimeFormat


open class BaseModel {
    @TableId(type = IdType.AUTO)
    @IsKey
    @IsAutoIncrement
    @IsNotNull
    @Column
    var id: Long = 0

    @IsNotNull
    @IsNativeDefValue
    @Column(
        name = "create_time",
        type = MySqlTypeConstant.DATETIME,
        defaultValue = "CURRENT_TIMESTAMP",
        comment = "创建时间")
    var createTime: String = ""


    @IsNotNull
    @IsNativeDefValue
    @Column(
        name = "update_time",
        type = MySqlTypeConstant.DATETIME,
        defaultValue = "CURRENT_TIMESTAMP",
        comment = "更新时间")
    var updateTime: String = TimeUtils.getCurrentTime()
}