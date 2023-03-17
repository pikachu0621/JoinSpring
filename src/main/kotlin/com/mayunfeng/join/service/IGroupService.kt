package com.mayunfeng.join.service

import com.mayunfeng.join.model.GroupTable
import com.mayunfeng.join.utils.JsonResult
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage


/**
 * 组表
 */
interface IGroupService {


    /**
     * 创建组
     *
     * @param img 头像
     * @param name 名字
     * @param type 类型
     * @param ird 介绍
     *
     */
    fun createGroup(img: MultipartFile?,  name: String?, type: String?, ird: String?): JsonResult<GroupTable>


    /**
     * 获取已创建的组
     * 需要token
     *
     *
     *
     *  todo 创建用户加入表    人数还没完成
     */
    fun userCreateGroup(): JsonResult<Array<GroupTable>>


    /**
     * 删除组
     * 需要token
     * @param id 组id
     */
    fun deleteUserGroup(id: Long?): JsonResult<Array<GroupTable>>


    /**
     * 编辑组
     * 一次性的全改
     * null 不修改
     *
     * 需要token
     *
     * @param id 组id
     *
     */
    fun editUserGroup(id: Long?, img: MultipartFile?, name: String?, type: String?, ird: String?): JsonResult<GroupTable>


}