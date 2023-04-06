package com.mayunfeng.join.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.mayunfeng.join.model.JoinGroupTable
import com.mayunfeng.join.model.UserSignTable
import org.apache.ibatis.annotations.Select
import org.springframework.stereotype.Component

@Component
interface UserSignTableMapper : BaseMapper<UserSignTable>{

    @Select("SELECT COUNT(*) FROM myf_user_sign_table WHERE sign_id=\${signId} AND sign_complete=1") // create_time  时间排序会丢失相同数据 desc
    fun queryPeopleCompleted(signId: Long): Int


    @Select("SELECT * FROM myf_user_sign_table WHERE user_id=\${userId} AND sign_complete=0 ORDER BY `id` \${order}") // create_time  时间排序会丢失相同数据 desc
    fun queryToBeCompleted(userId: Long, order: String = "desc"): ArrayList<UserSignTable>?

    @Select("SELECT * FROM myf_user_sign_table WHERE user_id=\${userId} ORDER BY `id` \${order}") // create_time  时间排序会丢失相同数据 desc
    fun queryToBeAll(userId: Long, order: String = "desc"): ArrayList<UserSignTable>?

    @Select("SELECT * FROM myf_user_sign_table WHERE user_id=\${userId} AND sign_id=\${signId}")
    fun queryToSign(userId: Long, signId: Long): UserSignTable?
}