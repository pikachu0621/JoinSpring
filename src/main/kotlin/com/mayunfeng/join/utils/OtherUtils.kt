package com.mayunfeng.join.utils

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.util.DigestUtils
import java.io.IOException
import java.io.PrintWriter
import java.io.Serializable
import java.sql.Timestamp
import java.util.regex.Pattern
import javax.servlet.http.HttpServletResponse
import kotlin.random.Random

object OtherUtils {


    // 字段是否为空 string 都是空格也算  空 true  非空 false
    fun isFieldEmpty(vararg field: Any?): Boolean {
        field.forEach {
            if (it == null) {
                return true
            }
            if (it is String) {
                if (it.replace(" ", "") == "") return true
            }
        }
        return false
    }

    /**
     * 是否包含违法字符  true 包含
     */
    fun isFieldIllegal(vararg field: String): Boolean {
        val regex = "[`!#$%^&*()+=\\-|\\[\\]{}\";:'?/><,]"
        val compile = Pattern.compile(regex)
        field.forEach {
            if (compile.matcher(it).find()) {
                return true
            }
        }
        return false;
    }




    /**
     * 创建 ds
     * 客户端
     */
    fun createDS(salt: String, vararg value: String): String {
        var valueStr = ""
        value.forEach { valueStr += it }
        val t = (Timestamp(System.currentTimeMillis()).time / 1000).toString()
        val r = Random.nextInt(100001, 200000).toString()
        val m: String = DigestUtils.md5DigestAsHex(("salt=${salt}&t=$t&r=$r$valueStr").toByteArray())
        return "$t,$r,$m"
    }


    /**
     * 验证 ds
     * 服务端
     */
    fun verifyDS(salt: String, t: String, r: String, m: String, vararg value: String): Boolean {
        var valueStr = ""
        value.forEach { valueStr += it }
        return DigestUtils.md5DigestAsHex(("salt=${salt}&t=$t&r=$r$valueStr").toByteArray()) == m
    }



    /**
     * 创建 token
     */
    fun createToken(salt: String, vararg value: String): String {
        var valueStr = ""
        value.forEach { valueStr += it }
        val t = (Timestamp(System.currentTimeMillis()).time / 1000).toString()
        val r = Random.nextInt(100001, 200000).toString()
        return DigestUtils.md5DigestAsHex(("salt=${salt}&t=$t&r=$r$valueStr").toByteArray());
    }


    /**
     * 拦截器 返回数据
     */
    fun returnJson(response: HttpServletResponse, jsonResult:  JsonResult<out Serializable>, run: (writer: PrintWriter) -> Unit = {}){
        var writer: PrintWriter? = null
        response.status = 200
        response.apply {
            characterEncoding = "UTF-8"
            contentType = "application/json; charset=utf-8"
        }
        try {
            writer = response.writer
            writer.print(ObjectMapper().writeValueAsString(jsonResult))
            run(writer)
        } catch ( e: IOException){
            // loge("拦截器输出流异常: ${e.message}")
        } finally {
            writer?.close()
        }
    }



}