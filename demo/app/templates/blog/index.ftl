<html>
<header>
<#include "../common/head.ftl">
</header>
<body>
<#include "../common/header.ftl">
<div class="container">
    <h2>blog list</h2>
    <hr>
    <div class="block">

        <a class="button is-primary" href="/blog/create">新增</a>

    </div>

    <table class="table">
        <thead>
        <tr>
            <th>标题</th>
            <th>内容</th>
            <th>状态</th>
            <th>创建时间</th>
            <th>最近修改时间</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <#list (context.blogList)! as blog>
          <tr>
              <td>${blog.blogTitle!}</td>
              <td>${blog.blogContent!}</td>
              <td>${blog.blogStatus!}</td>
              <td>${blog.gmtCreate?string("yyyy-MM-dd HH:mm:ss")}</td>
              <td>${blog.gmtUpdate?string("yyyy-MM-dd HH:mm:ss")}</td>
              <td>
                  <a href="/blog/edit?blogId=${blog.blogId!}">编辑</a>
                  <a href="/blog/doDel?blogId=${blog.blogId!}">删除</a>
              </td>

          </tr>
        </#list>
        </tbody>
        </table>


<#include "../common/pager.ftl">
<@pager pageQuery=context._query baseUrl="/index" paramString="abc=123"/>
</div>
</body>
</html>
