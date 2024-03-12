package com.pkpk.join.service.impl

import com.pkpk.join.base.BaseServiceImpl
import com.pkpk.join.config.AppConfig
import com.pkpk.join.config.AppConfigEdit
import com.pkpk.join.service.*
import com.pkpk.join.utils.JsonResult
import com.pkpk.join.utils.MD5Utils.getFileMd5
import com.pkpk.join.utils.OtherUtils
import com.pkpk.join.utils.RequestUtil.urlGet
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
    private lateinit var appConfig: AppConfig

    override fun test(appConfigEdit: AppConfigEdit?): JsonResult<AppConfigEdit> {
        return JsonResult.ok(appConfigEdit)
    }

    override fun testIp(): JsonResult<String> {
        return  JsonResult.ok(OtherUtils.getRemoteIP(request))
    }

    override fun testTime(aes: String?): JsonResult<Boolean> {
        if (OtherUtils.isFieldEmpty(aes))  throw ParameterException()

        return JsonResult.ok(OtherUtils.createTimeAESBCB(appConfig.appConfigEdit.imageTime, aes!!))
    }

    override fun testToken(token: String?): JsonResult<Boolean> {
        if (OtherUtils.isFieldEmpty(token))  throw ParameterException()
        return JsonResult.ok(tokenServiceImpl.verify(token!!))
    }

    override fun getGroupType(): JsonResult<ArrayList<String>> = JsonResult.ok(appConfig.appConfigEdit.groupTypeArr)


    override fun getGithubCommitLogs(project: String?): Any? {
        return "https://api.github.com/repos/pikachu0621/${project ?: "JoinSpring"}/commits".urlGet<Any>()
    }


    override fun upFile(file: MultipartFile?): JsonResult<String> {
        if (OtherUtils.isFieldEmpty(file)) throw ParameterException()
        file!!
        if (file.isEmpty) throw FileNulException()
        val dest = File("${appConfig.configUserImageFilePath()}${file.getFileMd5()}.zip")
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