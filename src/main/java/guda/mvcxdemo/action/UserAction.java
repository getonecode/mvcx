package guda.mvcxdemo.action;

import com.google.inject.Inject;
import guda.mvcx.annotation.action.Action;
import guda.mvcx.annotation.action.Req;
import guda.mvcx.annotation.action.ReqParam;
import guda.mvcx.annotation.action.View;
import guda.mvcx.enums.ViewTypeEnum;
import guda.mvcxdemo.biz.UserService;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by well on 2017/3/20.
 */
@Action
public class UserAction {


    @Inject
    private UserService userService;


    @Req(value = "/index.htm",method = HttpMethod.GET)
    public String index(RoutingContext context,@ReqParam("name") String name){
        System.out.println("context"+context);
        System.out.println("name"+ name);
        context.put("username", "gezhigang"+userService.index());
        return "user.hbs";
    }
}
