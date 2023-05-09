package com.mayunfeng.join.service.impl

import com.mayunfeng.join.base.BaseServiceImpl
import com.mayunfeng.join.config.AppConfig
import com.mayunfeng.join.service.FileNulException
import com.mayunfeng.join.service.FileSendException
import com.mayunfeng.join.service.IPublicService
import com.mayunfeng.join.service.ParameterException
import com.mayunfeng.join.utils.JsonResult
import com.mayunfeng.join.utils.MD5Utils
import com.mayunfeng.join.utils.MD5Utils.getFileMd5
import com.mayunfeng.join.utils.OtherUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import javax.servlet.http.HttpServletRequest


@Service
class PublicServiceImpl:  BaseServiceImpl(), IPublicService {


    @Autowired
    private lateinit var tokenServiceImpl: TokenServiceImpl

    @Autowired
    private lateinit var request: HttpServletRequest

    @Autowired
    private lateinit var APPConfig: AppConfig

    override fun test(): JsonResult<String> {
        return JsonResult.ok(OtherUtils.createTimeAESBCB(APPConfig.configSalt))
    }

    override fun testTime(aes: String?): JsonResult<Boolean> {
        if (OtherUtils.isFieldEmpty(aes))  throw ParameterException()

        return JsonResult.ok(OtherUtils.createTimeAESBCB(APPConfig.configImageTime, aes!!))
    }

    override fun testToken(token: String?): JsonResult<Boolean> {
        if (OtherUtils.isFieldEmpty(token))  throw ParameterException()
        return JsonResult.ok(tokenServiceImpl.verify(token!!))
    }

    override fun getGroupType(): JsonResult<Array<String>> =JsonResult.ok(APPConfig.clientConfigGroupType)




    override fun upFile(file: MultipartFile?): JsonResult<String> {
        if (OtherUtils.isFieldEmpty(file)) throw ParameterException()
        file!!
        if (file.isEmpty) throw FileNulException()
        val dest = File("${APPConfig.configUserImageFilePath()}${file.getFileMd5()}.zip")
        if (!dest.exists()) {
            logi("文件不存在 已上传")
            try {
                file.transferTo(dest)
            } catch (e: IOException) {
                throw FileSendException()
            }
        } else {
            logi("文件存在")
        }
        return JsonResult.ok("ok")
    }
}