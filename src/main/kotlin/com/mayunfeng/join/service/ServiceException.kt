package com.mayunfeng.join.service
import com.mayunfeng.join.base.BaseServiceException

// 异常类
class ParameterException : BaseServiceException(-1, "参数异常")

class ParameterIllegalException : BaseServiceException(-2, "参数包含违法字符")

// user
class UserPasswordException : BaseServiceException(-101, "密码或账号错误")

class UserBlacklistException : BaseServiceException(-102, "用户已被拉黑")

class UserPasswordLengthException : BaseServiceException(-103, "密码长度请保持在 (6~12)")

class UserAccountLengthException : BaseServiceException(-104, "账号长度请保持在 (6~2)")

// token
class TokenFailureException : BaseServiceException(-201, "token无效")



