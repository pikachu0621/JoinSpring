package com.mayunfeng.join.service.impl

import com.mayunfeng.join.base.BaseServiceImpl
import com.mayunfeng.join.config.AppConfig
import com.mayunfeng.join.config.TOKEN_PARAMETER
import com.mayunfeng.join.mapper.UserTableMapper
import com.mayunfeng.join.service.*
import com.mayunfeng.join.utils.*
import com.mayunfeng.join.utils.MD5Utils.getFileMd5
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import org.springframework.util.MimeTypeUtils
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.awt.image.BufferedImageFilter
import java.awt.image.BufferedImageOp
import java.io.File
import java.io.IOException
import javax.annotation.Resource
import javax.imageio.ImageIO
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Service
class PictureServiceImpl : BaseServiceImpl(), IPictureService {


    @Autowired
    private lateinit var APPConfig: AppConfig


    override fun requestImage(
        pictureMd5: String?,
        c: String?
    ): BufferedImage {
        // 限制时间
        if (APPConfig.configImageTime != -1L) {
            if (OtherUtils.isFieldEmpty(pictureMd5, c)) throw ParameterException()
            if (!OtherUtils.createTimeAESBCB(APPConfig.configImageTime, c!!)) throw DateTimeImageException()
        }
        val file = File("${APPConfig.configUserImageFilePath()}$pictureMd5")
        logi(file.name)
        if (!file.exists()) throw FileNulException()
        return ImageIO.read(ImageIO.createImageInputStream(file))
    }


    override fun upImage(imageFile: MultipartFile?): JsonResult<String> {
        if (OtherUtils.isFieldEmpty(imageFile)) throw ParameterException()
        imageFile!!
        if (imageFile.isEmpty) throw FileNulException()
        imageFile.contentType ?: throw FileTypeException()
        val parseMimeType = MimeTypeUtils.parseMimeType(imageFile.contentType!!)
        // logi("${ imageFile.contentType } -------")
        if (parseMimeType.type != MimeTypeUtils.parseMimeType("image/*").type) {
            throw FileTypeException()
        }
        if (imageFile.size > ByteUtils.mb2Byte(APPConfig.configImageSize.toBigDecimal())) throw FileMaxException()

        val imageName = "${imageFile.getFileMd5()}.png"
        val nameUserImageFilePath = "${APPConfig.configUserImageFilePath()}$imageName"
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
        return JsonResult.ok(imageName)
    }

}