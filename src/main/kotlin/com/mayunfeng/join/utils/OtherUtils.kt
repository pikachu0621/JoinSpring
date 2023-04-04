package com.mayunfeng.join.utils

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.server.ServerHttpRequest
import org.springframework.util.DigestUtils
import org.springframework.web.util.UriComponentsBuilder
import java.io.*
import java.net.URI
import java.sql.Timestamp
import java.util.regex.Pattern
import javax.servlet.http.HttpServletRequest
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


    fun String.isNumber(): Long = try {
        this.toLong()
    } catch (e: NumberFormatException) {
        e.printStackTrace()
        -1L
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
        val m: String = DigestUtils.md5DigestAsHex(("configSalt=${salt}&t=$t&r=$r$valueStr").toByteArray())
        return "$t,$r,$m"
    }


    /**
     * 验证 ds
     * 服务端
     */
    fun verifyDS(salt: String, t: String, r: String, m: String, vararg value: String): Boolean {
        var valueStr = ""
        value.forEach { valueStr += it }
        return DigestUtils.md5DigestAsHex(("configSalt=${salt}&t=$t&r=$r$valueStr").toByteArray()) == m
    }


    /**
     * 创建 token
     */
    fun createToken(salt: String, vararg value: String): String {
        var valueStr = ""
        value.forEach { valueStr += it }
        val t = (Timestamp(System.currentTimeMillis()).time / 1000).toString()
        val r = Random.nextInt(100001, 200000).toString()
        return DigestUtils.md5DigestAsHex(("configSalt=${salt}&t=$t&r=$r$valueStr").toByteArray())
    }


    /**
     * 创建 时间MD5 - 8
     */
    fun createTimeMd5(): String {
        val t = (Timestamp(System.currentTimeMillis()).time / 1000).toString()
        val r = Random.nextInt(100001, 200000).toString()
        return DigestUtils.md5DigestAsHex(("t=$t&r=$r").toByteArray()).substring(8, 16)
    }



    /**
     * 创建 加密时间
     * @param isForever == true 为永久  默认  false
     */
    fun createTimeAESBCB(salt: String): String {
        val t = (Timestamp(System.currentTimeMillis()).time / 1000).toString()
        val r = Random.nextInt(100001, 200000).toString()
        return AESBCBUtils.encrypt("s=${salt}&t=$t&r=$r")!!
    }

    /**
     * 验证时间
     * 有效时长
     */
    fun createTimeAESBCB(time: Long, aesStr: String): Boolean {
        val atTime = Timestamp(System.currentTimeMillis()).time / 1000
        val decrypt = AESBCBUtils.decrypt(aesStr) ?: return false
        val parsingUrlParameter = getParsingUrlParameter("?$decrypt", "t") ?: return false
        return try {
            atTime - parsingUrlParameter.toLong() <= time
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 解析  url数据   ?salt=dddd&t=1222222&r=121121
     */
    fun getParsingUrlParameter(urlStr: String, key: String): String? {
        return UriComponentsBuilder.fromUriString(urlStr).build().queryParams.getFirst(key)
    }


    /**
     * 拦截器 返回数据
     */
    fun returnJson(
        response: HttpServletResponse,
        jsonResult: JsonResult<out Serializable>,
        run: (writer: PrintWriter) -> Unit = {}
    ) {
        var writer: PrintWriter? = null
        response.status = HttpStatus.OK.value()
        response.apply {
            characterEncoding = "UTF-8"
            contentType = "application/json; charset=utf-8"
        }
        try {
            writer = response.writer
            writer.print(ObjectMapper().writeValueAsString(jsonResult))
            run(writer)
        } catch (e: IOException) {
            // loge("拦截器输出流异常: ${e.message}")
        } finally {
            writer?.close()
        }
    }


    /**
     * 获取 请求头 或 参数上必须的参数
     *
     */
    fun getMustParameter(request: HttpServletRequest, k: String): String? {
        var token = request.getParameter(k)
        if(isFieldEmpty(token)){
            token = request.getHeader(k)
        }
        return token
    }


    /**
     * 获取 请求头 或 参数上必须的参数
     *
     */
    fun getMustParameter(uri: URI?, httpHeaders: HttpHeaders, k: String): String? {
        var token: String? = httpHeaders[k]?.component1()
        if (token == null && uri != null){
            if(uri.path.isNullOrEmpty()) return null
            token = uri.path.substringAfterLast("/")
        }
        return token
    }




    /**
     * 获取请求客户真实IP
     */
    fun getRemoteIP(request: HttpServletRequest): String? {
        return if (request.getHeader("x-forwarded-for") == null) {
            request.remoteAddr
        } else request.getHeader("x-forwarded-for")
    }

    fun getRemoteIP(request: ServerHttpRequest): String? {
        return if (request.headers["x-forwarded-for"] == null) {
            request.remoteAddress.hostString
        } else request.headers["x-forwarded-for"].toString()
    }

    /**
     * 获取请求客户真实IP
     */
    fun getRemoteHost(request: HttpServletRequest): String? {
        var ip = request.getHeader("x-forwarded-for")
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("Proxy-Client-IP")
        }
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("WL-Proxy-Client-IP")
        }
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.remoteAddr
        }
        return if (ip == "0:0:0:0:0:0:0:1") "127.0.0.1" else ip
    }


}