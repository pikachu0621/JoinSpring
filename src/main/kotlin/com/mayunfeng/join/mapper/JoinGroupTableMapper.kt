package com.mayunfeng.join.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.mayunfeng.join.model.JoinGroupTable
import com.mayunfeng.join.model.UserTable
import org.apache.ibatis.annotations.Select
import org.springframework.stereotype.Component

@Component
interface JoinGroupTableMapper : BaseMapper<JoinGroupTable>{

    @Select("SELECT * FROM myf_join_group_table WHERE group_id=\${groupId} ORDER BY create_time \${order}")
    fun queryAllJoinGroupUser(groupId: Long, order: String = "asc"): ArrayList<JoinGroupTable>?

}