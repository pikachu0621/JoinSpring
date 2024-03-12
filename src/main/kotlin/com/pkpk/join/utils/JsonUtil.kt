package com.pkpk.join.utils
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson


// 解析 json
// 动态解析
//
object JsonUtil {


    /**
     * 解析任意节点json数据
     * @param classPath 解析路径
     *
     * ObjectMapper写法： ObjectMapper().readTree(s)["data"]["list"][0]["l"]
     * 此方法写法: jsonValue<泛型>("data.list[0].l")
     * 语法介绍
     *    .  路径符                   data.name
     *    [] 数组索引符                data.list[0].name
     *    :: 嵌套json数据访问符         data.list[0].name::data.pp[0]::value.key[0]::oop
     *
     * {"p1": "kk", "p2": 22, "p3":{"k": 0, "j":"{\"d\": 1, \"c1\": [0,2,3]}"}}
     * 解析 p1 字段       this.jsonValue<String>("p1")
     * 解析 p3 字段       this.jsonValue<String>("p3")
     * 解析 k 字段        this.jsonValue<Int>("p3.k")
     * 解析 j 字段        this.jsonValue<String>("p3.j")
     * 解析 d 字段        this.jsonValue<Int>("p3.j::d")
     * 解析 c1 字段       this.jsonValue<List<Int>>("p3.j::c1")
     * 解析 c1[0] 字段    this.jsonValue<Int>("p3.j::c1[0]")
     * 默认解析全部
     */
    inline fun <reified T> String.jsonValue(classPath: String? = null): T? {
        if (classPath == null) return retGson<T>(this)
        val strPaths = classPath.split("::")
        if (strPaths.isEmpty()) return null

        val objectMapper = ObjectMapper()
        var jsonNode = objectMapper.readTree(this)

        strPaths.forEachIndexed { i , v ->
            if (i > 0) jsonNode = objectMapper.readTree(jsonNode.asText())
            v.split(".").forEach {
                if (it.contains("[") && it.contains("]")) {
                    val kk = it.substring(0, it.indexOf("["))
                    val ki = it.substring(it.indexOf("[") + 1, it.indexOf("]")).toInt()
                    if (kk.isNotEmpty()) jsonNode = jsonNode[kk] ?: return null
                    jsonNode = jsonNode[ki] ?: return null
                } else jsonNode = jsonNode[it] ?: return null
            }
        }
        val jsonStr = jsonNode.toString().trim('"')
        return retGson<T>(jsonStr)
    }

    fun String.jsonValue(classPath: String? = null): String? {
        return this.jsonValue<String>(classPath)
    }


    @JvmStatic
    inline fun <reified T> retGson(body: String): T? {
        return if (T::class.java.isAssignableFrom(String::class.java)) {
            T::class.java.cast(body)
        } else {
            try {
                Gson().fromJson(body, T::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

}