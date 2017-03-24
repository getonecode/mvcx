<html>
<header>
<#include "../common/head.ftl">
</header>
<body>
<#include "../common/header.ftl">
<div class="container">
    <h2>blog create</h2>
    <hr>
    <form action="/blog/doCreate" method="post">
        <div class="column is-5 is-offset-one-quarter">
            <div class="field is-horizontal">
                <div class="field-label is-normal">
                    <label class="label">标题</label>
                </div>
                <div class="field-body">
                    <div class="field">
                        <div class="control">
                            <input class="input" type="text" name="blogTitle" value="${(context._form.blogTitle)!}"
                                   placeholder="标题">
                        </div>
                        <p class="help is-danger">
                        ${(context._form.errorResult.blogTitle)!}
                        </p>
                    </div>
                </div>
            </div>
            <div class="field is-horizontal">
                <div class="field-label is-normal">
                    <label class="label">标题</label>
                </div>
                <div class="field-body">
                    <div class="field">
                        <div class="control">
                            <textarea class="textarea" type="text" name="blogContent"
                                      placeholder="内容">${(context._form.blogContent)!}</textarea>
                        </div>
                        <p class="help is-danger">
                        ${(context._form.errorResult.blogContent)!}
                        </p>
                    </div>
                </div>
            </div>

            <div class="field is-horizontal">
                <div class="field-label is-normal">
                    <label class="label">状态</label>
                </div>
                <div class="field-body">
                    <div class="field">
                        <div class="control">
                        <@enum2map class="guda.mvcx.demo.biz.enums.BooleanEnum" keyMethod="getVal" valueMethod="getDesc">
                            <#list inner_map?keys as key>
                                <label class="radio">
                                    <input type="radio" name="blogStatus" value="${key}"
                                        <#if context._form?exists && key == (context._form.blogStatus)! >
                                           checked
                                        </#if>
                                            >
                                ${inner_map[key]}
                                </label>
                            </#list>
                        </@enum2map>
                        </div>
                        <p class="help is-danger">
                        ${(context._form.errorResult.blogStatus)!}
                        </p>
                    </div>
                </div>
            </div>

            <div class="field is-horizontal">
                <div class="field-label">
                    <!-- Left empty for spacing -->
                </div>
                <div class="field-body">
                    <div class="field">
                        <div class="control">
                            <button class="button is-primary" type="submit">保存</button>
                        </div>
                    </div>
                </div>
            </div>

    </form>
</div>
</body>
</html>
