package com.mayunfeng.join.model

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import com.gitee.sunchenbin.mybatis.actable.annotation.Column
import com.gitee.sunchenbin.mybatis.actable.annotation.Table
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant


// 用户
@TableName("myf_user_table")
data class MyfUserTableModel(

    @Column(
        comment = "用户头像",
        isNull = false,
        defaultValue = "default_user_img.png"
    )
    var userImg: String = "",


    @Column(
        comment = "用户性别  0=false=女   1=true=男",
        type = MySqlTypeConstant.INT,
        isNull = false,
        length = 1,
        defaultValue = "0"
    )
    var userSex: Boolean = false,


    @Column(
        comment = "用户名",
        isNull = false,
        defaultValue = "myf"
    )
    var userName: String = "",


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
    ) : BaseModel()