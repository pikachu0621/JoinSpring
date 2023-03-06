package com.mayunfeng.join.service

open class ServiceException: RuntimeException()

// 参数异常
class ParameterException: ServiceException()

// 用户名占用异常
class UsernameDuplicateException: ServiceException()




