package com.pkpk.join.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.pkpk.join.config.TABLE_USER
import com.pkpk.join.config.TABLE_USER_SIGN
import com.pkpk.join.model.UserSignTable
import com.pkpk.join.model.UserTable
import com.pkpk.join.utils.SqlUtils.F
import org.apache.ibatis.annotations.Select
import org.springframework.stereotype.Component



@Component
interface UserTableMapper : BaseMapper<UserTable>{

    @Select("SELECT * FROM $TABLE_USER") // 最好不用这个    用分页查询   这里为了速度就不改了
    fun queryAllUser(): ArrayList<UserTable>?
}