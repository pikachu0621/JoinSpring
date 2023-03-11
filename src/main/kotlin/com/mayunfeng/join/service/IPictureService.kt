package com.mayunfeng.join.service

import java.awt.image.BufferedImage
import javax.servlet.http.HttpServletResponse

interface IPictureService {

    /**
     * 获取图片
     *
     *
     * produces = MediaType.IMAGE_JPEG_VALUE
     */
    fun requestImage(pictureMd5: String?,  response: HttpServletResponse) : BufferedImage

}