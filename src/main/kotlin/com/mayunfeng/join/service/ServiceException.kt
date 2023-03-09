package com.mayunfeng.join.service


// 异常类
open class ServiceException(
    var errorCode: Int = -1,
    var errorMsg: String? = "error"
) : RuntimeException()

class ParameterException : ServiceException(-1, "参数异常")

class ParameterIllegalException : ServiceException(-2, "参数包含违法字符")

// user
class UserPasswordException : ServiceException(-101, "密码或账号错误")

class UserBlacklistException : ServiceException(-102, "用户已被拉黑")

class UserPasswordLengthException : ServiceException(-103, "密码长度请保持在 (6~12)")

class UserAccountLengthException : ServiceException(-104, "账号长度请保持在 (6~2)")

// token
class TokenFailureException : ServiceException(-201, "token失效，请重新登录")



