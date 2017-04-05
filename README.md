# mvcx
需要jdk8及以上版本，在vertx,mybatis,guice,freemarker等框架技术上扩展的快速开发web应用的脚手架。
#框架特点
 1. 基于guice容器管理bean.通过config.json配置action.package，biz.package等包路径控制容器管理bean的范围
 2. 基于vertx开发http框架，请求与业务逻辑通过event机制解耦，并且业务逻辑块支持多实例部署，提高业务处理容量
 3. 基于cookie的session共享机制，默认支持分布式部署,不需要单独的提供共享的session存储
 4. 集成了基于角色，用户的权限控制模块（页面访问，不涉及数据权限）

# 工程结构说明

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

# demo说明
  一个简单的action样例如下，通过@Action（注解在类上），@Req(注解在方法上)注解，来标记请求的路由。action方法默认支持RoutingContext,
PageQuery(分页)，Form(表单提交，如下面例子中的blogForm,继承自Form即可)

```
@Action
@Req(value = "/blog")
@Singleton
public class BlogAction {

    @Inject
    private BlogBiz blogBiz;

    @Req(value = "/index", method = HttpMethod.GET)
    public String index(RoutingContext context, PageQuery pageQuery) {
        pageQuery.setTotalCount(100);
        BizResult list = blogBiz.list(pageQuery);
        context.data().putAll(list.data);
        return "blog/index.ftl";
    }

    @Req(value = "/create", method = HttpMethod.GET)
    public String create() {
        return "blog/create.ftl";
    } 


    @Req(value = "/doCreate", method = HttpMethod.POST)
    public String doCreate(BlogForm blogForm) {
        if (blogForm.validate()) {
            return "blog/create.ftl";
        }
        BizResult bizResult = blogBiz.create(blogForm.toDO());
        if (bizResult.success) {
            return "redirect:/blog/index";
        } else {
            return "error.ftl";
        }

    }

    @Req(value = "/edit", method = HttpMethod.GET)
    public String edit(@ReqParam("blogId") Long blogId,BlogEditForm blogEditForm) {
        BizResult detail = blogBiz.detail(blogId);
        blogEditForm.init((BlogDO)detail.data.get("blogDO"));
        return "blog/edit.ftl";
    }

}

```
   
          
