package com.mayunfeng.join.controller

import com.mayunfeng.join.base.BaseController
import com.mayunfeng.join.service.IPictureService
import com.mayunfeng.join.service.impl.PictureServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.awt.image.BufferedImage
import javax.servlet.http.HttpServletResponse


/**
 * 图片控制器
 *
 */
@RestController
@RequestMapping("/myf-pic-api")
@CrossOrigin // 跨域
class PictureController : BaseController(), IPictureService {


    @Autowired
    private lateinit var pictureServiceImpl: PictureServiceImpl


    @GetMapping("/{imageName}", "/", produces = [MediaType.IMAGE_JPEG_VALUE])
    override fun requestImage(
        @PathVariable("imageName", required = false)  pictureMd5: String?,
        response: HttpServletResponse): BufferedImage = pictureServiceImpl.requestImage(pictureMd5, response)
}