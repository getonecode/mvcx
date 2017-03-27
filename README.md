#mvcx
在vertx,mybatis,guice,freemarker等框架技术上扩展的快速开发web应用的脚手架。
#工程结构说明
```
core----核心工程
demo----演示工程代码
    app
       assets -----静态资源文件
          css
          js
       templates ---- freemarker模板文件
          index.ftl
    deploy
       build.sh部署启动脚本
       sql ----数据库初始化等脚本文件
    src
       main
          guda.mvcx.demo.action ----负责web请求action
          guda.mvcx.demo.biz ----负责业务逻辑处理
          guda.mvcx.demo.dao ----负责数据库访问
       resources
          mapper  ----- mybatis配置文件
             userDAO.xml
          config.json ---全局配置文件
```
       
          