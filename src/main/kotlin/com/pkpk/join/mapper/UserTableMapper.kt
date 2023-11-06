package com.pkpk.join.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.pkpk.join.model.UserTable
import org.apache.ibatis.annotations.Select
import org.springframework.stereotype.Component

@Component
interface UserTableMapper : BaseMapper<UserTable>{

    @Select("SELECT * FROM pk_user_table") // 最好不用这个    用分页查询   这里为了速度就不改了
    fun queryAllUser(): ArrayList<UserTable>?
}