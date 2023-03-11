package com.mayunfeng.join.utils

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.core.mapper.BaseMapper

object SqlUtils {


    /**
     * 字段数据是否存在
     * @param baseMapper  数据库持久层
     * @param field 数据库字段
     * @param fieldValue 字段值
     * @return true 存在    false 不存在
     */
    fun <t> isDataUnique(
        baseMapper: BaseMapper<t>,
        field: String,
        fieldValue: String
    ): Boolean {
        val selectList = queryByFieldList(baseMapper, field, fieldValue)
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
    fun <t> queryByFieldList(
        baseMapper: BaseMapper<t>,
        field: String,
        fieldValue: Any
    ): List<t>? {
        return baseMapper.selectList(QueryWrapper<t>().apply {
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
    fun <t> queryByFieldOne(
        baseMapper: BaseMapper<t>,
        field: String,
        fieldValue: Any
    ): t? {
        return baseMapper.selectOne(QueryWrapper<t>().apply {
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
    fun <t> deleteByField(
        baseMapper: BaseMapper<t>,
        field: String,
        fieldValue: Any
    ) {
        baseMapper.delete(QueryWrapper<t>().apply {
            eq(field, fieldValue)
        })
    }


}