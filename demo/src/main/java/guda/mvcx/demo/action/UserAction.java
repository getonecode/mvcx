package guda.mvcx.demo.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import guda.mvcx.core.annotation.action.Action;
import guda.mvcx.core.annotation.action.Req;
import guda.mvcx.core.annotation.action.ReqParam;
import guda.mvcx.demo.biz.UserService;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by well on 2017/3/20.
 */
@Action
@Singleton
public class UserAction {


    @Inject
    private UserService userService;


    @Req(value = "/index.htm",method = HttpMethod.GET)
    public String index(RoutingContext context,@ReqParam("name") String name){
        System.out.println("context"+context);
        System.out.println("name"+ name);
        context.put("username", "gezhigang"+userService.index());
        Object userid = context.session().get("userid");
        System.out.println("index1 from session userid"+ userid);
         context.session().put("userid","123");
        if(true){
            throw new RuntimeException("custom");
        }

        return "user.hbs";
    }


    @Req(value = "/index2.htm",method = HttpMethod.GET)
    public String index2(RoutingContext context,@ReqParam("name") String name){
        System.out.println("context"+context);
        System.out.println("name"+ name);
        context.put("username", "gezhigang"+userService.index());
        Object userid = context.session().get("userid");

        System.out.println("from session userid"+ userid);
        return "user.hbs";
    }
}
