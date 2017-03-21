package guda.mvcxdemo.action;

import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.TemplateEngine;

/**
 * Created by well on 2017/3/17.
 */
public class IndexAction {

    private TemplateEngine engine;
    public IndexAction(TemplateEngine templateEngine){
        engine=templateEngine;
    }

    public void getIndex(RoutingContext context){

        context.put("name", "fffffddddd");
        engine.render(context, "webroot/templates/index.hbs", res -> {
            if (res.succeeded()) {
                context.response().end(res.result());
            } else {
                context.fail(res.cause());
            }
        });
    }

    public void getUser(RoutingContext context){
        context.put("username", "gezhigang");
        engine.render(context, "webroot/templates/user.hbs", res -> {
            if (res.succeeded()) {
                context.response().end(res.result());
            } else {
                context.fail(res.cause());
            }
        });
    }
}
