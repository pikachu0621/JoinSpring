package com.mayunfeng.join.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.mayunfeng.join.model.GroupTable
import com.mayunfeng.join.model.JoinGroupTable
import org.apache.ibatis.annotations.Select
import org.springframework.stereotype.Component

@Component
interface GroupTableMapper : BaseMapper<GroupTable>{

    @Select("SELECT * FROM myf_group_table WHERE id=\${groupIdOrGroupName} OR group_name LIKE '%\${groupIdOrGroupName}%' ORDER BY create_time \${order}")
    fun queryLikeGroup(groupIdOrGroupName: String, order: String = "asc"): ArrayList<GroupTable>?


}