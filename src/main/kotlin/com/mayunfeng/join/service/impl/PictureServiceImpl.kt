package com.mayunfeng.join.service.impl

import com.mayunfeng.join.base.BaseServiceException
import com.mayunfeng.join.base.BaseServiceImpl
import com.mayunfeng.join.config.AppConfig
import com.mayunfeng.join.config.TOKEN_PARAMETER
import com.mayunfeng.join.service.*
import com.mayunfeng.join.utils.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import org.springframework.util.MimeTypeUtils
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import javax.imageio.ImageIO
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Service
class PictureServiceImpl : BaseServiceImpl(), IPictureService {

    @Autowired
    private lateinit var resourceLoader: ResourceLoader

    @Autowired
    private lateinit var APPConfig: AppConfig

    @Autowired
    private lateinit var request: HttpServletRequest

    override fun requestImage(
        pictureMd5: String?,
        c: String?,
        response: HttpServletResponse
    ): BufferedImage {
        // 限制时间
        if (APPConfig.configImageTime != -1L){
            if (OtherUtils.isFieldEmpty(pictureMd5, c)) throw ParameterException()
            if (!OtherUtils.createTimeAESBCB(APPConfig.configImageTime, c!!)) throw DateTimeImageException()
        }
        val resource = resourceLoader.getResource(APPConfig.configDefaultPic)
        return try {
            ImageIO.read(
                if (pictureMd5 == null || pictureMd5 == APPConfig.configDefaultPicName) {
                    resource.inputStream
                } else {
                    val file = File("${APPConfig.configUserImageFilePath()}$pictureMd5")
                    if (!file.exists()) resource.inputStream
                    else FileInputStream(file)
                }
            )
        } catch (e: Exception) {
            ImageIO.read(resource.inputStream)
        }
    }


    override fun upImage(imageFile: MultipartFile?): JsonResult<String> {
        if (OtherUtils.isFieldEmpty(imageFile)) throw ParameterException()
        imageFile!!
        if (imageFile.isEmpty) throw FileNulException()
        imageFile.contentType ?:  throw FileTypeException()
        val parseMimeType = MimeTypeUtils.parseMimeType(imageFile.contentType!!)
        if (parseMimeType.type != MimeTypeUtils.parseMimeType("image/*").type ){
            throw FileTypeException()
        }
        if (imageFile.size > ByteUtils.mb2Byte(APPConfig.configImageSize.toBigDecimal())) throw FileMaxException()

        var imageName = "${MD5Utils.getMd5(imageFile)}${APPConfig.configImageType}"
        val nameUserImageFilePath = "${APPConfig.configUserImageFilePath()}$imageName"
        logi(nameUserImageFilePath)

        val dest = File(nameUserImageFilePath)
        if( !dest.exists() ){
            logi("没有文件进行上传")
            if (parseMimeType.subtype != "jpg" && parseMimeType.subtype != "jpeg"){
                logi("转换格式")
                imageName = ImageFileUtils.writeImageToJPG(imageFile.inputStream, dest) ?: throw FileSendTypeException()
            } else {
                logi("不转换格式")
                try {
                    imageFile.transferTo(dest)
                } catch (e: IOException){
                    throw FileSendException()
                }
            }
        }
        return JsonResult.ok(imageName)
    }

}