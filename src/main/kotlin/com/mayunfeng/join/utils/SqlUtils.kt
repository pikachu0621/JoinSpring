package com.mayunfeng.join.utils

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import java.io.File

object SqlUtils {


    /**
     * 字段数据是否存在
     * @param baseMapper  数据库持久层
     * @param field 数据库字段
     * @param fieldValue 字段值
     * @return true 存在    false 不存在
     */
    fun <t> BaseMapper<t>.isDataUnique(
        field: String,
        fieldValue: String
    ): Boolean {
        val selectList = this.queryByFieldList(field, fieldValue)
        if (selectList.isNullOrEmpty()) return false
        return true
    }


    /**
     * 根据字段查询 内容
     * @param baseMapper  数据库持久层
     * @param field 数据库字段
     * @param fieldValue 字段值
     * @return list<t>
     */
    fun <t> BaseMapper<t>.queryByFieldList(
        field: String,
        fieldValue: Any
    ): List<t>? {
        return this.selectList(QueryWrapper<t>().apply {
            eq(field, fieldValue)
        })
    }

    /**
     * 根据字段查询 内容
     * @param baseMapper  数据库持久层
     * @param field 数据库字段
     * @param fieldValue 字段值
     * @return <t>
     */
    fun <t> BaseMapper<t>.queryByFieldOne(
        field: String,
        fieldValue: Any
    ): t? {
        return this.selectOne(QueryWrapper<t>().apply {
            eq(field, fieldValue)
        })
    }


    /**
     * 根据字段删除 内容
     * @param baseMapper  数据库持久层
     * @param field 数据库字段
     * @param fieldValue 字段值
     * @return <t>
     */
    fun <t> BaseMapper<t>.deleteByField(
        field: String,
        fieldValue: Any
    ) {
        this.delete(QueryWrapper<t>().apply {
            eq(field, fieldValue)
        })
    }


    /**
     * 删除未绑定的图片
     *
     */
    fun <t> BaseMapper<t>.delImageFile(
        field: String,
        fieldValue: Any,
        filePath: String
    ){
        val queryByFieldList = this.queryByFieldList(field, fieldValue)
        if (!queryByFieldList.isNullOrEmpty()) {
            if(queryByFieldList.size <= 1){
                File(filePath).delete()
            }
        }
    }


}