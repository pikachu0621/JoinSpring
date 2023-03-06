package com.mayunfeng.join.utils

import java.io.Serializable


const val ERROR_PARAMETER = -1  // 参数错误
const val ERROR_SQL_CONNECT = -2 // 数据库连接错误
const val ERROR = -200 // 其他错误
const val OK = 200

/**
 * 数据返回
 */
data class JsonResult<T>(
    var reason: String? = "",
    var error_code: Int? = ERROR,
    var result: T? = null
) : Serializable {

    companion object {

        fun <t> ok(data: t?, reason: String = "ok", error_code: Int = OK): JsonResult<t> {
            return JsonResult(reason, error_code, data)
        }

        fun err(reason: String?, error_code: Int): JsonResult<Void> {
            return JsonResult(reason, error_code, null)
        }


        // 字段是否为空 string   空 true  非空 false
        fun isFieldEmpty(vararg field: Any?): Boolean {
            field.forEach {
                if (it == null /*|| (it == String && (it as String).isEmpty())*/) {
                    return true
                }
            }
            return false
        }
    }
}