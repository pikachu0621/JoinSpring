package com.pkpk.join.utils

import com.pkpk.join.utils.MD5Utils.getStringMd5
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object AESBCBUtils {


    private lateinit var ivs: IvParameterSpec
    private lateinit var keys: SecretKeySpec
    private lateinit var cipher: Cipher
    private const val hexStr = "0123456789ABCDEF"
    private val hexCode = hexStr.toCharArray()

    fun init(aesKey: String) {
        val coding = "UTF-8"
        val key = aesKey.getStringMd5(true)
        val iv = key!!.substring(0, 8).getStringMd5(true)
        try {
            ivs = IvParameterSpec(iv!!.toByteArray(charset(coding)))
            keys = SecretKeySpec(key.toByteArray(charset(coding)), "AES")
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        } catch (_: Exception) { }
    }


    /**
     * 加密
     *
     * @param value str
     * @return str
     */
    fun String.aesEncrypt(): String? {
        return try {
            cipher.init(Cipher.ENCRYPT_MODE, keys, ivs)
            bytesToHexStr(cipher.doFinal(this.toByteArray(StandardCharsets.UTF_8))).lowercase()
        } catch (e: Exception) {
            null
        }

    }


    /**
     * 解密
     *
     * @param encrypted str
     * @return str
     */
    fun String.aesDecrypt(): String? {
        return try {
            cipher.init(Cipher.DECRYPT_MODE, keys, ivs)
            val decode = hexStrToBytes(this.uppercase())
           String(cipher.doFinal(decode))
        } catch (_: Exception){
            null
        }
    }





    /**
     * 2 to 16
     *
     * @param data bytes
     * @return string
     */
    fun bytesToHexStr(data: ByteArray): String {
        val r = StringBuilder(data.size * 2)
        for (b in data) {
            r.append(hexCode[b.toInt() shr 4 and 0xF])
            r.append(hexCode[b.toInt() and 0xF])
        }
        return r.toString()
    }


    /**
     * 16 to 2
     *
     * @param hex 16 string
     * @return bytes
     */
    fun hexStrToBytes(hex: String): ByteArray {
        val len = hex.length / 2
        val result = ByteArray(len)
        val abhor = hex.toCharArray()
        for (i in 0 until len) {
            val pos = i * 2
            result[i] = (toByte(abhor[pos]) shl 4 or toByte(abhor[pos + 1])).toByte()
        }
        return result
    }

    private fun toByte(c: Char): Int {
        return hexStr.indexOf(c).toByte().toInt()
    }

}