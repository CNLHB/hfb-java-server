# 韩府帮后台
**开发要求**

+ IEDA
+ Java8
+ Redis
+ Mysql&通用mapper
+ QQ邮箱服务
+ 腾讯短信服务
+ 七牛云，云存储
+ 详情配置看yml文件

## 1、起步

+ 1、生成秘钥,执行/Users/aiwa/project/java/HFB-Server/src/main/java/com/ssk/hfb/common/utils/RsaUtilsTest文件

  + 直接使用可跳过（1、2，有错误则检查重新开始），项目已有密钥对

  ![image-20210731161827357](/Users/aiwa/Library/Application Support/typora-user-images/image-20210731161827357.png)



+ 2、确保秘钥路径引用正确

  + 确认yml文件中秘钥路径

  + 缺认authSerivce文件中路径

    ![image-20210731162204429](/Users/aiwa/Library/Application Support/typora-user-images/image-20210731162204429.png)

+ 3、确认mysql数据库

+ 4、确认Redis数据库

+ 5、确认有七牛云存储，需配置地址

  ![image-20210731165239355](/Users/aiwa/Library/Application Support/typora-user-images/image-20210731165239355.png)

+ 6、确认qq邮箱服务

+ 7、确认腾讯云服务

+ 8、获取登录地点需配置百度的接口，（获取登录接口地址会报错，可正常使用），详情自行百度

  ![image-20210731165500991](/Users/aiwa/Library/Application Support/typora-user-images/image-20210731165500991.png)

+ 8、启动服务

