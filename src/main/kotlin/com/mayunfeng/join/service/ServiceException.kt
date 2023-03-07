package com.mayunfeng.join.service


// 异常类
open class ServiceException(
    var errorCode: Int = -1,
    var errorMsg: String? = "error"
) : RuntimeException()

class ParameterException : ServiceException(-1, "参数异常")

class ParameterIllegalException : ServiceException(-2, "参数包含违法字符")

class UserPasswordException : ServiceException(-3, "密码出错")

class UserBlacklistException : ServiceException(-4, "用户已被拉黑")






