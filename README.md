myf_join
======

下载打包好的
---
[下载](/jar/myf-join.zip)
解压后点击 start.bt 即可(要先创建好用户 用户名：`myf_join` 密码：`123456` 及启动数据库)


食用方法
---
>1. 下载安装`MySql`(版本 >`5.7.26`)并启动
>2. 创建用户 用户名：`myf_join` 密码：`123456`(与配置里对应即可，如你要自己打包可以自定义 用户名与密码，如你直接使用打包好的请直接使用上述账号密码)
>3. 注意创建用户的权限问题 以下为创建用户MySql命令
```mysql
# 命令行创建用户
# (<用户名> = <数据库名>)   <密码>
CREATE USER '<用户名>'@'localhost' IDENTIFIED BY '<密码>';
GRANT ALL ON <数据库名>.* TO '<用户名>'@'localhost' IDENTIFIED BY '<密码>' WITH GRANT OPTION;
FLUSH PRIVILEGES;

# 例子
CREATE USER 'myf_join'@'localhost' IDENTIFIED BY '123456';
GRANT ALL ON myf_join.* TO 'myf_join'@'localhost' IDENTIFIED BY '123456' WITH GRANT OPTION;
FLUSH PRIVILEGES;
```

>4. 然后启动即可


Android代码
---
[Android端](https://github.com/pikachu0621/MyfJoinAndroid)


各项配置 路径：`MyfJoinSpring\src\main\resources\config.properties`
---
```properties
# 数据安全 ==========================
# 盐
config.token.salt=myf
# token 过期时间    单位秒  -1 = 永久
# 30天   60*60*24*30  2592000
config.token.time=-1

# 用户静态资源 <图片> 路径  ==========================
config.default.pic-name=default
config.default.pic-boy-path=classpath:/static/images/default_user_boy_img.png
config.default.pic-girl-path=classpath:/static/images/default_user_girl_img.png
config.static.path=user
config.static.image-path=img
#上传图片 ==========================
# 大小限制  单位MB
config.image.size=20
# image 失效时间    单位秒    -1 关闭时间校验
config.image.time=-1
# image 时间加密解密密码   用于校验图片是否可以访问
config.image.password=myf

# 限制账号密码长度  ==========================
config.chars.max-length=12
config.chars.min-length=6

# 客户端配置 ==========================
client.config.group-type=会议组,课堂组,公司组,学校组,培训组,活动组,其它

# websocket 配置 ==================
# 路径
config-websocket-path=/ws,/ws/*
```

By: Pikachu_WeChat
===

