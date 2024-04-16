package com.pkpk.join.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.pkpk.join.config.TABLE_USER_LOGIN_LOG
import com.pkpk.join.model.JoinGroupTable
import com.pkpk.join.model.UserLogTable
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Select
import org.springframework.stereotype.Component

@Component
interface UserLogMapper : BaseMapper<UserLogTable>{

    @Delete("DELETE FROM $TABLE_USER_LOGIN_LOG")
    fun deleteAll()


    @Select("SELECT * FROM $TABLE_USER_LOGIN_LOG ORDER BY `id` \${order}") // create_time  时间排序会丢失相同数据 desc
    fun queryAll(order: String = "ASC"): List<UserLogTable>?

}