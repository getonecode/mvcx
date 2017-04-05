# mvcx
需要jdk8及以上版本，在vertx,mybatis,guice,freemarker等框架技术上扩展的快速开发web应用的脚手架。
# 框架特点
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
@Singleton
@Req("blog")
public class BlogAction {

    @Inject
    private UserBiz userBiz;
    @Inject
    private BlogBiz blogBiz;

    @Req(value = "list")
    public String list(RoutingContext context,BlogQuery blogQuery){
        blogQuery.setUserId(AuthUser.getCurrentUser().getUserId());
        blogQuery.setPageSize(10);
        BizResult bizResult = userBiz.queryBlogForAdmin(blogQuery);
        context.data().putAll(bizResult.data);
        return "blog/list.ftl";
    }

    @Req(value = "edit")
    public String edit(BlogEditForm blogEditForm,@ReqParam("blogId")Long blogId){
        if(blogId==null){
            return "error.ftl";
        }
        BizResult detail = blogBiz.queryForEdit(blogId);
        blogEditForm.initForm((BlogDO)detail.data.get("blogDO"));
        return "blog/edit.ftl";
    }

    @Req(value = "create")
    public String create(){
        return "blog/create.ftl";
    }

    @Req(value = "create",method = HttpMethod.POST)
    public String doCreate(BlogForm blogForm){
        if(blogForm.validateError()){
            return "blog/create.ftl";
        }
        BlogDO blogDO=blogForm.toDO();
        blogDO.setUserId(AuthUser.getCurrentUser().getUserId());
        blogDO.setBlogStatus(BlogStatusEnum.init.getVal());
        blogDO.setCountView(0);
        blogDO.setTagBest(0);
        blogDO.setTagTop(0);
        BizResult bizResult = blogBiz.create(blogDO);
        if(bizResult.success){
            return "blog/saveSuccess.ftl";
        }
        return "error.ftl";
    }


    @Req(value = "update",method = HttpMethod.POST)
    public String update(BlogEditForm blogEditForm){
        if(blogEditForm.validateError()){
            return "blog/edit.ftl";
        }
        BlogDO blogDO=blogEditForm.toDO();
        BizResult bizResult = blogBiz.update(blogDO);
        if(bizResult.success){
            return "blog/saveSuccess.ftl";
        }
        return "error.ftl";
    }
}

```
   
          
