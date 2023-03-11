
API 文档
=====

###>1. 登录API
HTTP 方法：`POST`
<br>
请求 URL：`/myf-user-api/login-user`
<br>
参数提交类型：`Body -> form-data`
<br>
请求参数：

|    参数    | 是否必选 |   类型   | 可选值范围 |  说明  |
|:--------:|:----:|:------:|:-----:|:----:|
| account  |  是   | string |   -   | 用户账号 |
| password |  是   | string |   -   | 用户密码 |

返回示例：
```json
{
    "reason": "ok",
    "error_code": 200,
    "result": {
        "userAccount": "123456",
        "userPassword": "123456",
        "userImg": "default_user_img.png",
        "userSex": false,
        "userName": "x-x-x",
        "userUnit": "x-x-x-x",
        "userAge": 0,
        "userIntroduce": "这个家伙很懒，什么都没留下 >_<",
        "userGrade": 0,
        "userLimit": false,
        "loginToken": "9ed1c12f7ff112c76f4f6e38e15c78b1",
        "id": 1,
        "createTime": "2023-03-08 22:02:03",
        "updateTime": "2023-03-08 22:02:03",
        "baseTag": null
    }
}
```



###>2. 获取签到历史记录API
HTTP 方法：`GET`
<br>
请求 URL：`/myf-user-api/history-record`
<br>
参数提交类型：`Params`                           
<br>
请求参数：

|     参数     | 是否必选 |   类型   | 可选值范围 |   说明    |
|:----------:|:----:|:------:|:-----:|:-------:|
| loginToken |  是   | string |   -   | 登录token |

返回示例：
```json
{
    "reason": "ok",
    "error_code": 200,
    "result": {
        "userAccount": "123456",
        "userPassword": "123456",
        "userImg": "default_user_img.png",
        "userSex": false,
        "userName": "x-x-x",
        "userUnit": "x-x-x-x",
        "userAge": 0,
        "userIntroduce": "这个家伙很懒，什么都没留下 >_<",
        "userGrade": 0,
        "userLimit": false,
        "loginToken": "9ed1c12f7ff112c76f4f6e38e15c78b1",
        "id": 1,
        "createTime": "2023-03-08 22:02:03",
        "updateTime": "2023-03-08 22:02:03",
        "baseTag": null
    }
}
```























数据库 myf_join
======
所有表都存在
---

|     字段名     |    类型    | 长度  | 默认值  |  介绍  |
|:-----------:|:--------:|:---:|:----:|:----:|
|     id      |   long   | 11  | auto |  id  |
| create_time | datetime |  0  | 当前时间 | 创建时间 |
| update_time | datetime |  0  | 当前时间 | 更新时间 |

<br/><br/><br/>

*myf_user_table* 用户表
------------------------

|      字段名       |           介绍           |
|:--------------:|:----------------------:|
|       id       |           id           |
|  user_account  |          用户账号          |
| user_password  |          用户密码          |
|    user_img    |          用户头像          |
|    user_sex    |          用户性别          |
|   user_name    |          用户姓名          |
|   user_unit    |        用户 学校/单位        |
|    user_age    |          用户年龄          |
| user_introduce |          用户简介          |
|   user_grade   | 0 普通用户 1 管理员 2 root管理员 |
|   user_limit   |         用户是否拉黑         |

*myf_token_table* 登录token表
------------------------

|      字段名      |      介绍      |
|:-------------:|:------------:|
|      id       |      id      | 
|    user_id    |     属于谁      | 
|  token_login  |    MD5 唯一    | 
|  token_time   | 有效时长单位秒（20s） |
| token_failure |     是否失效     | 

<br/><br/><br/>





*myf_group_table* 用户创建的组表
------------------------

|       字段名       |     介绍     |
|:---------------:|:----------:|
|       id        |     id     | 
|     user_id     | 用户ID-谁是创建的 | 
|    group_img    |    组图标     | 
|   group_name    |     名字     | 
| group_introduce |     介绍     |

*myf_join_group_table* 用户加入的组表
------------------------

|   字段名    |       介绍       |
|:--------:|:--------------:|
|    id    |       id       | 
| user_id  | 用户ID-属于哪个用户的数据 | 
| group_id |    组的ID-哪个组    | 

<br/><br/><br/>



*myf_start_sign_table* 发起签到表
------------------------

|     字段名      |                  介绍                  |
|:------------:|:------------------------------------:|
|      id      |                  id                  | 
|   user_id    |             用户ID-哪个用户发起的             | 
|   group_id   |             组的ID-在哪个组发起              |
|  sign_title  |                 组-标题                 | 
| sign_content |                  内容                  | 
|  sign_type   | 签到类型- 0 无密码打卡 1 签到码打卡 2 二维码打卡 3 手势打卡 | 
|   sign_key   |               签到key-密码               | 

*myf_user_sign_table* 用户签到表
------------------------

|      字段名      |       介绍       |
|:-------------:|:--------------:|
|      id       |       id       | 
|    user_id    | 用户ID-属于哪个用户的数据 | 
|    sign_id    |      签到ID      |
| sign_complete |     签到是否完成     | 
|   sign_time   |      签到时间      | 

*myf_user_sign_history_table* 签到记录表
------------------------

|      字段名      |       介绍       |
|:-------------:|:--------------:|
|      id       |       id       | 
|    user_id    | 用户ID-属于哪个用户的数据 | 
|    sign_id    |      签到ID      |
| sign_complete |     签到是否完成     | 
|   sign_time   |      签到时间      | 

<br/><br/><br/>

