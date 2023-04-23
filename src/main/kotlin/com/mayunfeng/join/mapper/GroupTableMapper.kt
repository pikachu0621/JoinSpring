package com.mayunfeng.join.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.mayunfeng.join.model.GroupTable
import com.mayunfeng.join.model.JoinGroupTable
import com.mayunfeng.join.model.UserTable
import org.apache.ibatis.annotations.Select
import org.springframework.stereotype.Component

@Component
interface GroupTableMapper : BaseMapper<GroupTable>{

    @Select("SELECT * FROM myf_group_table WHERE id=\${groupId} OR group_name LIKE '%\${groupName}%' ORDER BY `id` \${order}") // create_time  时间排序会丢失相同数据 desc
    fun queryLikeGroup(groupId : Long = -1, groupName: String, order: String = "asc"): ArrayList<GroupTable>?



    @Select("SELECT * FROM myf_group_table") // 最好不用这个    用分页查询   这里为了速度就不改了
    fun queryAllData(): ArrayList<GroupTable>?
}