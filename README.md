#mvcx
在vertx,mybatis,guice,freemarker等框架技术上扩展的快速开发web应用的脚手架。
#框架特点
 1. 基于guice容器管理bean.通过config.json配置action.package，biz.package等包路径控制容器管理bean的范围
 2. 基于vertx开发http框架，请求与业务逻辑通过event机制解耦，并且业务逻辑块支持多实例部署，提高业务处理容量
 3. 基于cookie的session共享机制，默认支持分布式部署,不需要单独的提供共享的session存储
 4. 集成了基于角色，用户的权限控制模块（页面访问，不涉及数据权限）
#工程结构说明

```
core----核心工程
demo----演示工程代码
 ---app
    --assets -----静态资源文件
        js
        css
        img
    --temples ---- freemarker模板文件
        index.ftl
    --deploy
        build.sh部署启动脚本
        sql ----数据库初始化等脚本文件
 ---src
    --main
        guda.mvcx.demo.action ----负责web请求action
        guda.mvcx.demo.biz ----负责业务逻辑处理
        guda.mvcx.demo.dao ----负责数据库访问
    --resources
        auth.properties页面权限配置文件
        mapper  ----- mybatis配置文件
          userDAO.xml
        config.json ---全局配置文件
```

#说明
  
          