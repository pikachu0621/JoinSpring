package com.mayunfeng.join.controller

import com.mayunfeng.join.base.BaseController
import com.mayunfeng.join.service.IPictureService
import com.mayunfeng.join.service.impl.PictureServiceImpl
import com.mayunfeng.join.utils.JsonResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import javax.servlet.http.HttpServletRequest
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