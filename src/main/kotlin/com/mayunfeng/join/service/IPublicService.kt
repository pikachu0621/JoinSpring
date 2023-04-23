package com.mayunfeng.join.service

import com.mayunfeng.join.utils.JsonResult
import org.springframework.web.multipart.MultipartFile
import java.util.Arrays

interface IPublicService {


    /**
     * http  test
     */
    fun test(): JsonResult<String>



    /**
     * 验证token 可用性
     * @param aes
     */
    fun testTime(aes: String?): JsonResult<Boolean>

    /**
     * 验证token 可用性
     * @param token
     */
    fun testToken(token: String?): JsonResult<Boolean>


    /**
     * 获取group type
     */
    fun getGroupType(): JsonResult<Array<String>>


    /**
     * 上传文件
     * 压缩包密码 用RSA公钥加密 私钥解密
     * .zip
     *      md5.zip
     *      encryptSign.txt  (包含设备信息解压密码)
     */
    fun upFile(file: MultipartFile?): JsonResult<String>
}
