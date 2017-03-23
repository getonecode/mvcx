package guda.mvcx.demo.action;

import com.google.inject.Singleton;
import guda.mvcx.core.annotation.action.Action;
import guda.mvcx.core.annotation.action.Req;
import guda.mvcx.core.helper.PageQuery;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.TemplateEngine;

/**
 * Created by well on 2017/3/17.
 */
@Action
@Singleton
public class IndexAction {


    @Req(value = "/index", method = HttpMethod.GET)
    public String getIndex(PageQuery pageQuery) {
        pageQuery.setTotalCount(100);

        return "index.ftl";
    }


}
