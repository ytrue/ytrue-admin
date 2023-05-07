# 1.简介

后端 ytrue-admin 基于springboot 开发后台<br>
前端 ytrue-admin 基于vue3 + js + element plus 开发

# 2. 目录结构

```text
ytrue-admin                                                                      # 后端代码
├── ytrue-application                                                            # 应用层
│     └── ytrue-application-monitor                                              # spring-boot-admin监控
│     └── ytrue-application-admin                                                # 后台应用
│            └── modules                                                         # 后台应用模块
│            └── generator                                                       # 后台专属-代码生成器模块
│            └── quartz                                                          # 后台专属-定时任务模块
│            └── system                                                          # 后台专属-后台rbac模块
│				    					
├── ytrue-infra                                                                  # 基础设施层                     
│     └── ytrue-infra-db                                                         # 数据库 mysql,es,mongodb
│     └── ytrue-infra-common                                                     # 公共代码
│
├── ytrue-service                                                                # 业务逻辑层
│
├── ytrue-manager                                                                # 通用处理层
│              └──GoodsServiceManager
│
├── ytrue-bean                                                                   # java bean
│              ├── po                                                            #  
│              ├── req                                                           #  
│              ├── res                                                           #  
│              ├── enums                                                         #  
│              └── query                                                         #  
│                     
├── ytrue-tools                                                                  # 工具库-独立模块该模块都是spring start          
│     └── ytrue-tools-security                                                   # spring security + jwt 安全
│     └── ytrue-tools-log                                                        # 操作日志
│     └── ytrue-tools-query                                                      # 查询增强库
│     └── ytrue-tools-sms                                                        # 短信
│     └── ytrue-tools-storage                                                    # 存储
```

# 3.截图

![image-01](https://php-yangyi-images.oss-cn-shenzhen.aliyuncs.com/ytrue-admin-docs/0.png)
![image-01](https://php-yangyi-images.oss-cn-shenzhen.aliyuncs.com/ytrue-admin-docs/1.png)
![image-01](https://php-yangyi-images.oss-cn-shenzhen.aliyuncs.com/ytrue-admin-docs/2.png)
![image-01](https://php-yangyi-images.oss-cn-shenzhen.aliyuncs.com/ytrue-admin-docs/3.png)
![image-01](https://php-yangyi-images.oss-cn-shenzhen.aliyuncs.com/ytrue-admin-docs/4.png)
![image-01](https://php-yangyi-images.oss-cn-shenzhen.aliyuncs.com/ytrue-admin-docs/5.png)
![image-01](https://php-yangyi-images.oss-cn-shenzhen.aliyuncs.com/ytrue-admin-docs/6.png)
![image-01](https://php-yangyi-images.oss-cn-shenzhen.aliyuncs.com/ytrue-admin-docs/7.png)
![image-01](https://php-yangyi-images.oss-cn-shenzhen.aliyuncs.com/ytrue-admin-docs/8.png)
