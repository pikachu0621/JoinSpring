package com.mayunfeng.join.utils

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.util.DigestUtils
import org.springframework.util.MultiValueMap
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.util.UriComponentsBuilder
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.*
import java.sql.Timestamp
import java.util.regex.Pattern
import javax.imageio.ImageIO
import javax.servlet.http.HttpServletResponse
import kotlin.random.Random
import kotlin.reflect.typeOf

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
        response.status = 200
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


}