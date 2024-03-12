package com.pkpk.join.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.pkpk.join.config.TABLE_JOIN_GROUP
import com.pkpk.join.model.JoinGroupTable
import com.pkpk.join.utils.SqlUtils.F
import org.apache.ibatis.annotations.Select
import org.springframework.stereotype.Component

@Component
interface JoinGroupTableMapper : BaseMapper<JoinGroupTable>{

    /**
     * ${JoinGroupTable::groupId.F()}
     */
    @Select("SELECT * FROM $TABLE_JOIN_GROUP WHERE group_id=#{groupId} ORDER BY `id` #{order}") // create_time  时间排序会丢失相同数据 desc
    fun queryAllJoinGroupUser(groupId: Long, order: String = "asc"): ArrayList<JoinGroupTable>?
}