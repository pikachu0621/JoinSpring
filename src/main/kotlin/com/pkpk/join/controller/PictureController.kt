package com.pkpk.join.controller

import com.pkpk.join.base.BaseController
import com.pkpk.join.config.API_PICTURE
import com.pkpk.join.service.IPictureService
import com.pkpk.join.service.impl.PictureServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage


/**
 * 图片控制器
 *
 */
@RestController
@RequestMapping(API_PICTURE)
@CrossOrigin // 跨域
class PictureController : BaseController(), IPictureService {


    @Autowired
    private lateinit var pictureServiceImpl: PictureServiceImpl


    @GetMapping("/{imageName}", "/", produces = [MediaType.IMAGE_PNG_VALUE])
    override fun requestImage(
        @PathVariable("imageName", required = true) pictureMd5: String,
        @RequestParam("c", required = false) c: String?,
    ) = pictureServiceImpl.requestImage(pictureMd5, c)

    @GetMapping("/static/{imageName}", "/static/", produces = [MediaType.IMAGE_PNG_VALUE])
    override fun requestStaticImage(
        @PathVariable("imageName", required = true) pictureName: String,
        @RequestParam("c", required = false) c: String?
    ): BufferedImage = pictureServiceImpl.requestStaticImage(pictureName, c)


    @PostMapping("/up-img")
    @ResponseBody
    override fun upImage(@RequestParam("img", required = true) imageFile: MultipartFile) = pictureServiceImpl.upImage(imageFile)
}