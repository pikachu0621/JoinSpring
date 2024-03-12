package com.pkpk.join.service

import com.pkpk.join.model.StartSignTable
import com.pkpk.join.utils.JsonResult

interface IStartSignService {

    enum class SignType(val type: Int) {
        NO_SIGN_CODE(1),    // 无密码打卡
        SIGN_CODE(2),       // 签到码打卡
        QR_CODE(3),         // 二维码打卡
        GESTURE_CODE(4),    // 手势打卡
        POSITION_CODE(5);   // 位置打卡

        fun typeToEnum(type: Int) = when (type) {
            NO_SIGN_CODE.type -> NO_SIGN_CODE
            SIGN_CODE.type -> SIGN_CODE
            QR_CODE.type -> QR_CODE
            GESTURE_CODE.type -> GESTURE_CODE
            POSITION_CODE.type -> POSITION_CODE
            else -> NO_SIGN_CODE
        }
    }

    /**
     * 发起签到
     *
     * @param groupId       组的ID-在哪个组发起的
     * @param signTitle     标题
     * @param signContent   内容
     * @param signType      类型      [1-无密码打卡] [2-签到码打卡] [3-二维码打卡] [4-手势打卡] [5-位置打卡]
     * @param signKey       密码      [-1 无密码]
     * @param signTime      有效时长   [-1 无时间限制] 单位秒(s)
     *
     * 需要验证 Token
     */
    fun startSign(
        groupId: Long,
        signTitle: String?,
        signContent: String?,
        signType: Int,
        signKey: String?,
        signTime: Long
    ): JsonResult<StartSignTable>


    /**
     * 获取已创建的签到信息
     * 根据token user_id 获取
     *
     */
    fun getSignAllInfoListByUserId(): JsonResult<Array<StartSignTable>>


    /**
     * 根据 signId 获取数据
     * @param signId id
     */
    fun queryStartSignInfoById(signId: Long): StartSignTable = StartSignTable()


    /**
     * 删除我发起的签到
     * @param signId       signId
     *
     * 需要验证 Token 和是否此用户创建的签到
     * 删除
     *
     */
    fun delSign(signId: Long): JsonResult<Array<StartSignTable>>


}