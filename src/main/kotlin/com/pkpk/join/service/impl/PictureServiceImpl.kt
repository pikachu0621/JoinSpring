package com.pkpk.join.service.impl

import com.pkpk.join.base.BaseServiceImpl
import com.pkpk.join.config.AppConfig
import com.pkpk.join.service.*
import com.pkpk.join.utils.*
import com.pkpk.join.utils.MD5Utils.getFileMd5
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import org.springframework.util.MimeTypeUtils
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO


@Service
class PictureServiceImpl : BaseServiceImpl(), IPictureService {


    @Autowired
    private lateinit var appConfig: AppConfig

    @Autowired
    private lateinit var userLogServiceImpl: UserLogServiceImpl

    @Autowired
    private lateinit var resourceLoader: ResourceLoader

    override fun requestImage(
        pictureMd5: String,
        c: String?
    ): BufferedImage {
        // 限制时间
        if (appConfig.appConfigEdit.imageTime != -1L) {
            if (OtherUtils.isFieldEmpty(pictureMd5, c)) throw ParameterException()
            if (!OtherUtils.createTimeAESBCB(appConfig.appConfigEdit.imageTime, c!!)) throw DateTimeImageException()
        }
        val file = File("${appConfig.configUserImageFilePath()}$pictureMd5")
        // logi(file.name)
        if (!file.exists()) throw FileNulException()
        return ImageIO.read(ImageIO.createImageInputStream(file))
    }

    override fun requestStaticImage(pictureName: String, c: String?): BufferedImage {
        // 限制时间
        if (appConfig.appConfigEdit.imageTime != -1L) {
            if (OtherUtils.isFieldEmpty(c)) throw ParameterException()
            if (!OtherUtils.createTimeAESBCB(appConfig.appConfigEdit.imageTime, c!!)) throw DateTimeImageException()
        }
        try {
            return ImageIO.read(resourceLoader.getResource(appConfig.configStaticPicPath + pictureName).inputStream)
        } catch (e: Exception){
            throw DateTimeImageException()
        }
    }


    override fun upImage(imageFile: MultipartFile): JsonResult<String> {
        if (OtherUtils.isFieldEmpty(imageFile)) throw ParameterException()
        if (imageFile.isEmpty) throw FileNulException()
        imageFile.contentType ?: throw FileTypeException()
        val parseMimeType = MimeTypeUtils.parseMimeType(imageFile.contentType!!)
        // logi("${ imageFile.contentType } -------")
        if (parseMimeType.type != MimeTypeUtils.parseMimeType("image/*").type) throw FileTypeException()
        if (imageFile.size > ByteUtils.mb2Byte(appConfig.appConfigEdit.imageSize.toBigDecimal())) throw FileMaxException()
        val imageName = "${imageFile.getFileMd5()}.png"
        val nameUserImageFilePath = "${appConfig.configUserImageFilePath()}$imageName"
        // logi(nameUserImageFilePath)
        val dest = File(nameUserImageFilePath)
        if (!dest.exists()) {
            logi("文件不存在 已上传")
            try {
                imageFile.transferTo(dest)
            } catch (e: IOException) {
                throw FileSendException()
            }
        } else {
            logi("文件存在")
        }
        userLogServiceImpl.addLogLogin("后台修改Spring配置")
        return JsonResult.ok(imageName)
    }

}