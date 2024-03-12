package com.pkpk.join.utils

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.gitee.sunchenbin.mybatis.actable.annotation.Column
import com.google.common.base.CaseFormat
import java.io.File
import java.util.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation

object SqlUtils {

    /**
     *
     * 获取字段在数据库中的名字
     *
     * 如有变动修改字段泛型
     *
     * [com.gitee.sunchenbin.mybatis.actable.annotation.Column]
     */
    fun <K, F> KProperty1<K, F>.F(): String {
        val findAnnotation =
            this.findAnnotation<Column>() ?: return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name)
                .lowercase(Locale.getDefault())
        return findAnnotation.name
    }


    /**
     * 字段数据是否存在
     * @param baseMapper    数据库持久层
     * @param field         数据库字段
     * @param fieldValue    字段值
     * @return true 存在    false 不存在
     */
    fun <T> BaseMapper<T>.isDataUnique(
        field: String,
        fieldValue: Any
    ): Boolean = !this.queryByFieldList(field, fieldValue).isNullOrEmpty()
    fun <K, F, T> BaseMapper<T>.isDataUnique(
        field: KProperty1<K, F>,
        fieldValue: Any
    ) = this.isDataUnique(field.F(), fieldValue)


    /**
     * 根据字段查询 内容
     * @param baseMapper  数据库持久层
     * @param field 数据库字段
     * @param fieldValue 字段值
     * @return list<t>
     */
    fun <T> BaseMapper<T>.queryByFieldList(
        field: String,
        fieldValue: Any
    ): List<T>? {
        return this.selectList(QueryWrapper<T>().apply {
            eq(field, fieldValue)
        })
    }
    fun <K, F, T> BaseMapper<T>.queryByFieldList(
        field: KProperty1<K, F>,
        fieldValue: Any
    ) = this.queryByFieldList(field.F(), fieldValue)





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
    fun <K, F, T> BaseMapper<T>.queryByFieldOne(
        field: KProperty1<K, F>,
        fieldValue: Any
    ) = this.queryByFieldOne(field.F(), fieldValue)




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
    fun <K, F, T> BaseMapper<T>.deleteByField(
        field: KProperty1<K, F>,
        fieldValue: Any
    ) = this.deleteByField(field.F(), fieldValue)



    /**
     * 删除未绑定的图片
     *
     */
    fun <t> BaseMapper<t>.delImageFile(
        field: String,
        fieldValue: Any,
        filePath: String
    ) {
        val queryByFieldList = this.queryByFieldList(field, fieldValue)
        if (!queryByFieldList.isNullOrEmpty()) {
            if (queryByFieldList.size <= 1) {
                File(filePath).delete()
            }
        }
    }
    fun <K, F, T> BaseMapper<T>.delImageFile(
        field: KProperty1<K, F>,
        fieldValue: Any,
        filePath: String
    ) = this.delImageFile(field.F(), fieldValue, filePath)


}