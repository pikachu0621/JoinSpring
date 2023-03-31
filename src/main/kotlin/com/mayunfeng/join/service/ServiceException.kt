package com.mayunfeng.join.service
import com.mayunfeng.join.base.BaseServiceException

// 异常类
class ParameterException : BaseServiceException(-1, "参数异常")
class ParameterIllegalException : BaseServiceException(-2, "参数包含违法字符")
// token  -3 要与前端对应
class TokenFailureException : BaseServiceException(-3, "您没有权限访问")

// user
class UserPasswordException : BaseServiceException(-101, "密码或账号错误")
class UserBlacklistException : BaseServiceException(-102, "用户已被拉黑")
class UserPasswordLengthException : BaseServiceException(-103, "密码长度请保持在 (6~12)")
class UserAccountLengthException : BaseServiceException(-104, "账号长度请保持在 (6~12)")
class UserOldPasswordException : BaseServiceException(-105, "旧密码错误")
class UserEquallyPasswordException : BaseServiceException(-106, "新密码不能与旧密码相同")
class UserNulException : BaseServiceException(-107, "此用户不存在")



// 日期时间 异常
class DateTimeException : BaseServiceException(-301, "日期时间格式出错")
class DateTimeImageException : BaseServiceException(-302, "图片无效")

// 数据过长
class DataLengthMaxException : BaseServiceException(-501, "数据过长")

// 文件
class FileTypeException : BaseServiceException(-601, "文件类型错误")
class FileNulException : BaseServiceException(-602, "文件不存在")
class FileMaxException : BaseServiceException(-603, "文件过大")
class FileSendException : BaseServiceException(-604, "文件上传失败")
class FileSendTypeException : BaseServiceException(-605, "文件上传失败,格式转换出错")


// 创建 group
class GroupNameLengthException : BaseServiceException(-701, "名字长度请保持在 (1~20)")
class GroupIrdLengthException : BaseServiceException(-702, "介绍长度请保持在 (1~100)")
class GroupTypeException : BaseServiceException(-703, "此类型不存在")
class GroupUserAuthorityEditException : BaseServiceException(-704, "此组您没有操作权限")
class GroupNulException : BaseServiceException(-705, "此组不存在")
class GroupByToMyException : BaseServiceException(-706, "不能自己踢自己")
class GroupUserNotJoinException : BaseServiceException(-707, "该用户未加入此组，无法操作")


// 加入 group
class JoinGroupOkException : BaseServiceException(-801, "您已加入该组，无需重复操作")
class JoinGroupNoException : BaseServiceException(-802, "您未加入该组，无法进行此操作")


// 签到
class StartSignNulException : BaseServiceException(-901, "此签到不存在")
class StartSignUserAuthorityEditException : BaseServiceException(-902, "此签到您没有操作权限")
class StartSignNulAddUserEditException : BaseServiceException(-904, "此组未加入成员无法发起签到")


