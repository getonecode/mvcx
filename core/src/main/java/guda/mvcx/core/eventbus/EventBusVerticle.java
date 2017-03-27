package guda.mvcx.core.eventbus;

import guda.mvcx.core.eventbus.context.AppContext;
import guda.mvcx.core.eventbus.helper.EventAddressConstants;
import guda.mvcx.core.eventbus.msg.HttpEventMsg;
import guda.mvcx.core.factory.GuiceBeanFactory;
import guda.mvcx.core.ext.freemarker.ExtFreeMarkerEngineImpl;
import guda.mvcx.core.handle.DefaultFailureHandler;
import guda.mvcx.core.handle.DefaultNotFoundHandler;
import guda.mvcx.core.handle.PageAuthCheckHandler;
import guda.mvcx.core.handle.RouteAction;
import guda.mvcx.core.session.CookieStoreSessionImpl;
import guda.mvcx.core.session.DefaultCookieHandlerImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import io.vertx.ext.web.templ.TemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by well on 2017/3/25.
 */
public class EventBusVerticle extends AbstractEventBusVerticle {


    private Logger log = LoggerFactory.getLogger(getClass());

    private AppContext appContext;

    public EventBusVerticle(AppContext context) {
        appContext = context;
    }


    @Override
    public void start() throws Exception {

        Router router = Router.router(vertx);
        router.route().handler(event -> {
            event.put(APP_CONTEXT_KEY, appContext);
            event.next();
        });
        router.route().handler(new DefaultCookieHandlerImpl());


        SessionStore store = LocalSessionStore.create(vertx);
        SessionHandler sessionHandler = SessionHandler.create(store);
        router.route().handler(sessionHandler);

        JsonObject cookieConfig = config().getJsonObject("cookie");
        router.route().handler(new CookieStoreSessionImpl(cookieConfig.getString("domain"), cookieConfig.getString("path"),
                cookieConfig.getBoolean("secure"), cookieConfig.getBoolean("httpOnly"), cookieConfig.getLong("maxAge"),
                cookieConfig.getString("sessionKey"), cookieConfig.getString("checkKey"), cookieConfig.getString("encryptSalt")));

        StaticHandler staticHandler = StaticHandler.create();
        staticHandler.setAllowRootFileSystemAccess(true);
        staticHandler.setWebRoot(config().getString("assets.dir"));
        staticHandler.setCachingEnabled(false);
        router.route("/assets/*").handler(staticHandler);

        //page auth
        if (config().getBoolean("usePageAuth") && config().getString("pageAuthFailUrl") != null) {
            router.route().handler(new PageAuthCheckHandler(config().getString("pageAuthFailUrl")));
        }
        router.route().handler(BodyHandler.create());

        //page auth end
        TemplateEngine engine = new ExtFreeMarkerEngineImpl(config());
        List<RouteAction> routeList = appContext.getAllRouteActionList();
        routeList.forEach(routeAction -> {
            routeAction.getActionInvokeHandler().setTemplateEngine(engine);
        });
        router.route().handler(event -> {
            HttpServerRequest request = event.request();
            HttpEventMsg httpEventMsg =new HttpEventMsg();
            httpEventMsg.setHttpServerRequest(request);
            httpEventMsg.setRoutingContext(event);
            event.vertx().eventBus().send(EventAddressConstants.ACTION_ADDRESS, httpEventMsg);
        });
        router.route("/*").handler(new DefaultNotFoundHandler(engine, "404.ftl"));
        router.route().failureHandler(new DefaultFailureHandler(engine, "error.ftl"));

        HttpServer server = vertx.createHttpServer();
        server.requestHandler(router::accept).listen(config().getInteger("http.port"));
    }
}
