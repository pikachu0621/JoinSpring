package com.pkpk.join.utils

import java.io.Serializable

const val ERROR = -1 // 其他错误
const val OK = 200

/**
 * 数据返回
 */
data class JsonResult<T: Serializable>(
    var reason: String? = "",
    var error_code: Int? = ERROR,
    var result: T? = null
) : Serializable {

    companion object {

        fun <t: Serializable> ok(data: t?, reason: String = "ok", error_code: Int = OK): JsonResult<t> {
            return JsonResult(reason, error_code, data)
        }

        fun err(reason: String?, error_code: Int): JsonResult<Serializable> {
            return JsonResult(reason, error_code, null)
        }

        fun isContainsForbid(vararg field: Any?): Boolean {
            field.forEach {
                if (it == null) {
                    return true
                }
            }
            return false
        }


    }
}