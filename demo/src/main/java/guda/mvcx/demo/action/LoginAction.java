package guda.mvcx.demo.action;

import com.google.inject.Singleton;
import guda.mvcx.core.annotation.action.Action;
import guda.mvcx.core.annotation.action.Req;
import guda.mvcx.demo.form.LoginForm;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by well on 2017/3/23.
 */
@Action
@Singleton
public class LoginAction {

    @Req(value = "/login", method = HttpMethod.GET)
    public String login() {
        return "login.ftl";
    }

    @Req(value = "/login", method = HttpMethod.POST)
    public String doLogin(RoutingContext context, LoginForm loginForm) {
        if (loginForm.validate()) {
            return "login.ftl";
        }
        if ("test".equals(loginForm.getUserName()) && "test".equals(loginForm.getPassword())) {
            return "redirect:/index";
        }

        loginForm.reject("password", "用户名或者密码错误");
        return "login.ftl";
    }
}
