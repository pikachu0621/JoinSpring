package com.mayunfeng.join.service.impl

import com.mayunfeng.join.service.IPictureService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import javax.imageio.ImageIO
import javax.servlet.http.HttpServletResponse


@Service
class PictureServiceImpl : IPictureService {

    @Value("\${config.user.image.path}")
    private lateinit var userImg: String

    @Value("\${config.user.static.path}")
    private lateinit var userStatic: String

    @Value("\${config.user.default-pic.path}")
    private lateinit var defaultPic: String

    @Value("\${config.user.default-pic.name}")
    private lateinit var defaultPicName: String
    private val userDir = "${System.getProperty("user.dir")}${File.separator}"

    @Autowired
    private lateinit var resourceLoader: ResourceLoader

    override fun requestImage(
        pictureMd5: String?,
        response: HttpServletResponse
    ): BufferedImage {
        val resource = resourceLoader.getResource(defaultPic)
        return try {
            ImageIO.read(
                if (pictureMd5 == null || pictureMd5 == defaultPicName) {
                    resource.inputStream
                } else {
                    val file = File("$userDir$userStatic${File.separator}$userImg${File.separator}$pictureMd5")
                    if (!file.exists()) resource.inputStream
                    else FileInputStream(file)
                }
            )
        } catch (e: Exception) {
            ImageIO.read(resource.inputStream)
        }
    }


}