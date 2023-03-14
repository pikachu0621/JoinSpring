package com.mayunfeng.join.utils


import org.springframework.util.DigestUtils
import org.springframework.util.ResourceUtils
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object MD5Utils {

    @JvmStatic
    fun getMd5(file: File): String? {
        return try {
            DigestUtils.md5DigestAsHex(FileInputStream(file));
        } catch (_: Exception) { null }
    }


    @JvmStatic
    fun getMd5(path: String): String? {
        return getMd5(ResourceUtils.getFile(path))
    }

    @JvmStatic
    fun getMd5(file: MultipartFile): String? {
        return try {
            val digest = MessageDigest.getInstance("MD5").digest(file.bytes)
            BigInteger(1, digest).toString(16);
        } catch (_: Exception) {
            null
        }
    }


    /**
     * md5
     *
     * @param sourceStr 字符串
     * @param is16      是否为16位长度
     * @return MD5
     */
    fun md5(sourceStr: String, is16: Boolean): String? {
        var md532: String? = null
        var md516: String? = null
        val result: String
        try {
            val md: MessageDigest = MessageDigest.getInstance("MD5")
            md.update(sourceStr.toByteArray())
            val b: ByteArray = md.digest()
            var i: Int
            val buf = StringBuilder()
            for (value in b) {
                i = value.toInt()
                if (i < 0) i += 256
                if (i < 16) buf.append("0")
                buf.append(Integer.toHexString(i))
            }
            result = buf.toString()
            md532 = result
            md516 = buf.substring(8, 24)
        } catch (_: NoSuchAlgorithmException) {
        }
        return if (is16) md516 else md532
    }

}