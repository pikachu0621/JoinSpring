package com.pkpk.join.utils

import com.pkpk.join.utils.JsonUtil.retGson
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec
import org.springframework.web.util.UriComponentsBuilder


/**
 *  支持同步 和 异步
 *  implementation("org.springframework.boot:spring-boot-starter-webflux")
 *  gson 解析 json  兼容性好
 *  implementation("com.google.code.gson:gson:2.8.9")
 *
 *
 */
object RequestUtil {


    private var webClient: WebClient = WebClient.builder().build()


    @JvmStatic
    fun iniBaseDefault(
        defaultBaseUrl: String? = null,
        defaultHeaders: HttpHeaders? = null,
        defaultCookies: MultiValueMap<String, String>? = null
    ) {
        val builder = WebClient.builder()
        if (!defaultBaseUrl.isNullOrEmpty()) builder.baseUrl(defaultBaseUrl)
        if (!defaultHeaders.isNullOrEmpty()) builder.defaultHeaders { it.addAll(defaultHeaders) }
        if (!defaultCookies.isNullOrEmpty()) builder.defaultCookies { it.addAll(defaultCookies) }
        webClient = builder.build()
    }


    // 同步 post
    @JvmStatic
    private fun urlBasePost(
        requestsUrl: String,
        requestsContent: Any,
        requestsContentType: MediaType,
        requestsHeaders: HttpHeaders? = null
    ): ResponseSpec = webClient.post()
        .uri(requestsUrl)
        .contentType(requestsContentType)
        .headers { hs -> requestsHeaders?.let { hs.addAll(it) } }
        .bodyValue(requestsContent)
        .retrieve()

    // 同步 get
    @JvmStatic
    private fun urlBaseGet(
        requestsUrl: String,
        requestsContent: MultiValueMap<String, String>? = null,
        requestsHeaders: HttpHeaders? = null
    ): ResponseSpec = webClient.get()
        .uri(buildUrlString(requestsUrl, requestsContent))
        .headers { hs -> requestsHeaders?.let { hs.addAll(it) } }
        .retrieve()


    // post  json 类型提交   mono.block()
    @JvmStatic
    fun <T> String.urlPost(
        requestsContent: Any,
        responseType: Class<T>,
        requestsContentType: MediaType = MediaType.APPLICATION_JSON,
        requestsHeaders: HttpHeaders? = null
    ): ResponseEntity<T>? = urlBasePost(this, requestsContent, requestsContentType, requestsHeaders)
        .toEntity(responseType)
        .block()

    // 利用Gson兼容好
    @JvmStatic
    inline fun <reified T> String.urlPost(
        requestsContent: Any,
        requestsContentType: MediaType = MediaType.APPLICATION_JSON,
        requestsHeaders: HttpHeaders? = null
    ): T? {
        val body =
            this.urlPost(requestsContent, String::class.java, requestsContentType, requestsHeaders)?.body ?: return null
        return retGson<T>(body)
    }


    // post  formData 类型提交
    @JvmStatic
    fun <T> String.urlPost(
        requestsContent: MultiValueMap<String, String>,
        responseType: Class<T>,
        requestsContentType: MediaType = MediaType.MULTIPART_FORM_DATA,
        requestsHeaders: HttpHeaders? = null
    ): ResponseEntity<T>? = urlBasePost(this, requestsContent, requestsContentType, requestsHeaders)
        .toEntity(responseType)
        .block()

    @JvmStatic
    inline fun <reified T> String.urlPost(
        requestsContent: MultiValueMap<String, String>,
        requestsContentType: MediaType = MediaType.MULTIPART_FORM_DATA,
        requestsHeaders: HttpHeaders? = null
    ): T? {
        val body =
            this.urlPost(requestsContent, String::class.java, requestsContentType, requestsHeaders)?.body ?: return null
        return retGson<T>(body)
    }


    // get 提交
    @JvmStatic
    fun <T> String.urlGet(
        responseType: Class<T>,
        requestsContent: MultiValueMap<String, String>? = null,
        requestsHeaders: HttpHeaders? = null
    ): ResponseEntity<T>? = urlBaseGet(this, requestsContent, requestsHeaders)
        .toEntity(responseType)
        .block()


    @JvmStatic
    inline fun <reified T> String.urlGet(
        requestsContent: MultiValueMap<String, String>? = null,
        requestsHeaders: HttpHeaders? = null
    ): T? {
        val body = this.urlGet(String::class.java, requestsContent, requestsHeaders)?.body ?: return null
        return retGson<T>(body)
    }


    @JvmStatic
    inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}



    @JvmStatic
    fun buildUrlString(baseUrl: String, map: MultiValueMap<String, String>?): String {
        map ?: return baseUrl
        val builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        map.forEach { (name: String, values: List<String>) ->
            builder.queryParam(name, values)
        }
        return builder.toUriString()
    }


    @JvmStatic
    fun httpHeaderForSystem(): HttpHeaders = HttpHeaders().apply {
        add("Accept", "*/*")
        //add("Accept-Encoding", "gzip, deflate, br")
        add("Connection", "keep-alive")
    }

    @JvmStatic
    fun httpHeaderForAndroid(): HttpHeaders = HttpHeaders().apply {
        addAll(httpHeaderForSystem())
        add(
            HttpHeaders.USER_AGENT,
            "Mozilla/5.0 (Linux; Android 6.0.1; Moto G (4)) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Mobile Safari/537.36"
        )
    }

    @JvmStatic
    fun httpHeaderOkHttp(code: String = "3.12.13"): HttpHeaders = HttpHeaders().apply {
        addAll(httpHeaderForSystem())
        add(
            HttpHeaders.USER_AGENT, "okhttp/$code"
        )
    }

    @JvmStatic
    fun httpHeaderForWindows(): HttpHeaders = HttpHeaders().apply {
        addAll(httpHeaderForSystem())
        add(
            HttpHeaders.USER_AGENT,
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36"
        )
    }

    @JvmStatic
    fun httpHeaderForIOS(): HttpHeaders = HttpHeaders().apply {
        addAll(httpHeaderForSystem())
        add(
            HttpHeaders.USER_AGENT,
            "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1"
        )
    }


}