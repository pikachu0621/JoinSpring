package com.pkpk.join.base

open class BaseServiceException(
    var errorCode: Int = -1,
    var errorMsg: String? = "error"
) : RuntimeException()