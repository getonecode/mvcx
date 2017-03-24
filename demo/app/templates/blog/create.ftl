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

            <div class="field">
                <p class="control">
                    <input class="input" type="text" name="blogTitle" value="${(context._form.blogTitle)!}"
                           placeholder="标题">

                </p>

                <p class="help is-danger">${(context._form.errorResult.blogTitle)!}</p>
            </div>
            <div class="field">
                <p class="control">
                    <textarea class="textarea" type="text" name="blogContent"
                           placeholder="内容">${(context._form.blogContent)!}</textarea>

                </p>

                <p class="help is-danger">${(context._form.errorResult.blogContent)!}</p>
            </div>
            <div class="field is-grouped">
                <p class="control">
                    <button class="button is-primary" type="submit">保存</button>
                </p>

            </div>


        </div>
    </form>
</div>
</body>
</html>
