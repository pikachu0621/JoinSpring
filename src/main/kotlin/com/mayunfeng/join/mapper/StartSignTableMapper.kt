package com.mayunfeng.join.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.mayunfeng.join.model.JoinGroupTable
import com.mayunfeng.join.model.StartSignTable
import org.apache.ibatis.annotations.Select
import org.springframework.stereotype.Component

@Component
interface StartSignTableMapper: BaseMapper<StartSignTable>{

    @Select("SELECT * FROM myf_start_sign_table WHERE user_id=\${userId} ORDER BY `id` \${order}") // desc 倒叙
    fun querySignAllInfoUserId(userId: Long, order: String = "asc"): ArrayList<StartSignTable>?

}