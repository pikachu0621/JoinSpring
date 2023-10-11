package com.pkpk.join.utils

import org.springframework.web.multipart.MultipartFile
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import javax.imageio.ImageIO


object ImageFileUtils {

    /**
     * 获取文件后缀
     * 有 .
     */
    @JvmStatic
    fun getSuffix(fileName: String): String {
        return fileName.substring(fileName.lastIndexOf("."))
    }

    @JvmStatic
    fun getSuffix(file: MultipartFile): String {
        return getSuffix(file.originalFilename!!)
    }

    @JvmStatic
    fun getSuffix(file: File): String {
        return getSuffix(file.name)
    }


    /**
     * 获取文件名 不包含 后缀 .*
     */
    @JvmStatic
    fun getFileNameNotSuffix(fileName: String): String {
        return fileName.substring(fileName.lastIndexOf("/") + 1, fileName.lastIndexOf("."))
    }
    @JvmStatic
    fun getFileNameNotSuffix(file: MultipartFile): String {
        return getFileNameNotSuffix(file.originalFilename!!)
    }

    @JvmStatic
    fun getFileNameNotSuffix(file: File): String {
        return getFileNameNotSuffix(file.name)
    }



    @JvmStatic
    fun writeImageToJPG(input: File, output: File, type: String = "jpg"): String? {
        return writeImageToJPG(FileInputStream(input), output)
    }

    @JvmStatic
    fun writeImageToJPG(input: InputStream, output: String, type: String = "jpg"): String? {
        return writeImageToJPG(input, File(output))
    }

    /**
     * null 为 写入失败
     * 返回写入名
     */
    @JvmStatic
    fun writeImageToJPG(input: InputStream, output: File, type: String = "jpg"): String? {
        val read = ImageIO.read(input)
        if (read.width <= 0 || read.height <= 0 ) return null
        val newBufferedImage: BufferedImage = BufferedImage(read.width, read.height, BufferedImage.TYPE_INT_RGB).apply {
            createGraphics().drawImage(read, 0, 0, Color.WHITE, null)
        }
        val nameNotSuffix = File("${output.parent}${File.separator}${getFileNameNotSuffix(output)}.$type")
        val write = ImageIO.write(newBufferedImage, type, nameNotSuffix)
        input.close()
        return if (write) nameNotSuffix.name else null
    }

    @JvmStatic
    fun writeImageToPNG(input: InputStream, output: File, type: String = "png"): String? {
        val read = ImageIO.read(input)
        if (read.width <= 0 || read.height <= 0 ) return null
        val newBufferedImage: BufferedImage = BufferedImage(read.width, read.height, BufferedImage.TYPE_INT_ARGB).apply {
            createGraphics()
        }
        val nameNotSuffix = File("${output.parent}${File.separator}${getFileNameNotSuffix(output)}.$type")
        val write = ImageIO.write(newBufferedImage, type, nameNotSuffix)
        input.close()
        return if (write) nameNotSuffix.name else null
    }


    /// 读取文件到 bytes
    fun readImageBytes(file: File): ByteArray{
        val inputStream = FileInputStream(file)
        val bytes = ByteArray(inputStream.available())
        inputStream.read(bytes, 0, inputStream.available())
        return bytes
    }


}