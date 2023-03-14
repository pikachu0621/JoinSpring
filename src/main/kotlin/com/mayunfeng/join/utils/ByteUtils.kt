package com.mayunfeng.join.utils

import java.math.BigDecimal





object ByteUtils {

    private const val KB_SIZE = 2 shl 9
    private const val MB_SIZE = 2 shl 19
    private const val GB_SIZE = 2 shl 29

    fun bytes2Unit(bytes: Long, unit: Int?): BigDecimal {
        val size = BigDecimal(bytes)
        val u = BigDecimal(unit!!)
        return size.divide(u, 2, BigDecimal.ROUND_DOWN)
    }

    fun unit2Byte(decimal: BigDecimal, unit: Int): Long {
        return decimal.multiply(BigDecimal.valueOf(unit.toLong())).toLong()
    }

    fun kb2Byte(decimal: BigDecimal): Long {
        return decimal.multiply(BigDecimal.valueOf(KB_SIZE.toLong())).toLong()
    }

    fun mb2Byte(decimal: BigDecimal): Long {
        return decimal.multiply(BigDecimal.valueOf(MB_SIZE.toLong())).toLong()
    }

    fun gb2Byte(decimal: BigDecimal): Long {
        return decimal.multiply(BigDecimal.valueOf(GB_SIZE.toLong())).toLong()
    }

    fun bytes2Kb(bytes: Long): BigDecimal {
        return bytes2Unit(bytes, KB_SIZE)
    }

    fun bytes2Mb(bytes: Long): BigDecimal {
        return bytes2Unit(bytes, MB_SIZE)
    }

    fun bytes2Gb(bytes: Long): BigDecimal {
        return bytes2Unit(bytes, GB_SIZE)
    }


}