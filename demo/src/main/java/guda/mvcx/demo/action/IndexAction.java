package guda.mvcx.demo.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import guda.mvcx.core.annotation.action.Action;
import guda.mvcx.core.annotation.action.Req;
import guda.mvcx.core.helper.PageQuery;
import guda.mvcx.demo.biz.UserService;
import guda.mvcx.demo.model.UserDO;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.TemplateEngine;
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


}
