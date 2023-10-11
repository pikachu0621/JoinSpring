package com.pkpk.join.service

import com.pkpk.join.utils.JsonResult
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage

interface IPictureService {

    /**
     * 获取图片
     *
     * produces = MediaType.IMAGE_JPEG_VALUE
     * @param pictureMd5 图片名
     * @return 图片流
     */
    fun requestImage(pictureMd5: String?, c: String?) : BufferedImage


    /**
     * 上传图片
     *
     * @param  imageFile 图片文件
     * @return 返回MD5命名后的图片名字
     */
    fun upImage(imageFile: MultipartFile?): JsonResult<String>


}