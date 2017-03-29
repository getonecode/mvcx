package guda.mvcx.demo.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import guda.mvcx.core.annotation.action.Action;
import guda.mvcx.core.annotation.action.Req;
import guda.mvcx.core.helper.PageQuery;
import guda.mvcx.demo.biz.UserService;

import guda.mvcx.demo.dao.model.UserDO;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Created by well on 2017/3/17.
 */
@Action
@Singleton
public class IndexAction {


    @Inject
    private UserService userService;

    @Req(value = "/index", method = HttpMethod.GET)
    public String getIndex(PageQuery pageQuery) {
        pageQuery.setTotalCount(100);
        UserDO index = userService.index();
        System.out.println(ReflectionToStringBuilder.toString(index));
        return "index.ftl";
    }

    @Req(value = "/index/:id/:path", method = HttpMethod.GET)
    public String index(RoutingContext context,PageQuery pageQuery) {
        System.out.println(context.request().getParam("id"));
        System.out.println(context.request().getParam("path"));
//        pageQuery.setTotalCount(100);
//        UserDO index = userService.index();
//        System.out.println(ReflectionToStringBuilder.toString(index));
        return "index.ftl";
    }


}
