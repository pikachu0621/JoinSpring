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
class UserOldPasswordException : BaseServiceException(-105, "旧密码错误")
class UserEquallyPasswordException : BaseServiceException(-106, "新密码不能与旧密码相同")

// token  -201 要与前端对应
class TokenFailureException : BaseServiceException(-201, "您没有权限访问")

// 日期时间 异常
class DateTimeException : BaseServiceException(-301, "日期时间格式出错")
class DateTimeImageException : BaseServiceException(-302, "图片无效")

// 数据过长
class DataLengthMaxException : BaseServiceException(-501, "数据过长")

// 文件
class FileTypeException : BaseServiceException(-601, "文件类型错误")

class FileNulException : BaseServiceException(-602, "文件为空")

class FileMaxException : BaseServiceException(-603, "文件过大")

class FileSendException : BaseServiceException(-604, "文件上传失败")

class FileSendTypeException : BaseServiceException(-605, "文件上传失败,格式转换出错")


