JoinSpring
======

食用方法
---
>1. 下载安装`MySql`(版本 >`5.7.26`)并启动
>2. 创建用户 用户名：`pk_join` 密码：`123456`(与配置里对应即可，如你要自己打包可以自定义 用户名与密码，如你直接使用打包好的请直接使用上述账号密码)
>3. 注意创建用户的权限问题 以下为创建用户mysql命令
```mysql
# 命令行创建用户
# (<用户名> = <数据库名>)   <密码>
CREATE USER '<用户名>'@'localhost' IDENTIFIED BY '<密码>';
GRANT ALL ON <数据库名>.* TO '<用户名>'@'localhost' IDENTIFIED BY '<密码>' WITH GRANT OPTION;
FLUSH PRIVILEGES;

# 例子
CREATE USER 'pk_join'@'localhost' IDENTIFIED BY '123456';
GRANT ALL ON pk_join.* TO 'pk_join'@'localhost' IDENTIFIED BY '123456' WITH GRANT OPTION;
FLUSH PRIVILEGES;
```
>4. [下载](/jar/JoinSpring-0.0.3.jar)打包好的jar ```java -jar 'JoinSpring-0.0.3.jar```' 即可

---

API文档
-------
[API文档](https://console-docs.apipost.cn/preview/9e608885058d3ede/38f1d8c9f866c1c9)

Android端代码
---
[Android端](https://github.com/pikachu0621/JoinAndroid)

vue管理端代码
---
[vue管理端](https://github.com/pikachu0621/JoinVue)

SpringBoot服务端代码
---
[SpringBoot服务端](https://github.com/pikachu0621/JoinSpring)

---


各项配置 路径：`JoinSpring\src\main\resources\config.properties`
---
```properties
# 数据安全 ==========================
# 盐
config.token.salt=pk
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
config.image.password=pkpk

# 限制账号密码长度  ==========================
config.chars.max-length=12
config.chars.min-length=6

# 客户端配置 ==========================
client.config.group-type=会议组,课堂组,公司组,学校组,培训组,活动组,其它

# websocket 配置 ==================
# 路径
config-websocket-path=/ws,/ws/*
```

---

后台管理地址
---
127.0.0.1:8012/admin/index.html

---

WechatBy: pkpk-run
===

