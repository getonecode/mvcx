<html>
<header>
<#include "common/head.ftl">
</header>
<body>
<#include "common/header.ftl">
<section class="section">
    <div class="container">
        <h2 class="subtitle">登录</h2>
        <hr>
    </div>
</section>

<div class="container">
    <form action="/login" method="post">
        <div class="column is-3 is-offset-one-quarter">

            <div class="field">
                <p class="control has-icon">
                    <input class="input" type="text" name="userName" value="${(context._form.userName)!}"
                           placeholder="用户名">
            <span class="icon is-small">
              <i class="fa fa-user-circle-o"></i>
            </span>
                </p>

                <p class="help is-danger">${(context._form.errorResult.userName)!}</p>
            </div>
            <div class="field">
                <p class="control has-icon">
                    <input class="input" type="password" name="password" value="${(context._form.password)!}"
                           placeholder="密码">
                <span class="icon is-small">
                  <i class="fa fa-lock"></i>
                </span>
                </p>

                <p class="help is-danger">${(context._form.errorResult.password)!}</p>
            </div>
            <div class="field is-grouped">
                <p class="control">
                    <button class="button is-primary" type="submit">登录</button>
                </p>
                <p class="control">
                    <button class="button">忘记密码</button>
                </p>
            </div>


        </div>
    </form>
</div>

</body>
</html>



