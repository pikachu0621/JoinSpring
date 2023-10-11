package com.pkpk.join.controller

import com.pkpk.join.base.BaseController
import com.pkpk.join.service.IPictureService
import com.pkpk.join.service.impl.PictureServiceImpl
import com.pkpk.join.utils.JsonResult
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
@RequestMapping("/pk-pic-api")
@CrossOrigin // 跨域
class PictureController : BaseController(), IPictureService {


    @Autowired
    private lateinit var pictureServiceImpl: PictureServiceImpl


    @GetMapping("/{imageName}", "/", produces = [MediaType.IMAGE_PNG_VALUE])
    override fun requestImage(
        @PathVariable("imageName", required = false) pictureMd5: String?,
        @RequestParam("c", required = false) c: String?
    ): BufferedImage = pictureServiceImpl.requestImage(pictureMd5, c)


    @PostMapping("/up-img")
    @ResponseBody
    override fun upImage(
        @RequestParam("img") imageFile: MultipartFile?
    ): JsonResult<String> = pictureServiceImpl.upImage(imageFile)
}