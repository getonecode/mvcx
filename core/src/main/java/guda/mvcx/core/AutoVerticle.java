package guda.mvcx.core;


import guda.mvcx.core.eventbus.context.AppContext;
import guda.mvcx.core.ext.freemarker.ExtFreeMarkerEngineImpl;
import guda.mvcx.core.handle.DefaultFailureHandler;
import guda.mvcx.core.handle.DefaultNotFoundHandler;
import guda.mvcx.core.handle.PageAuthCheckHandler;
import guda.mvcx.core.handle.RouteAction;
import guda.mvcx.core.session.CookieStoreSessionImpl;
import guda.mvcx.core.session.DefaultCookieHandlerImpl;
import guda.mvcx.core.util.JsonConfigUtil;
import guda.mvcx.core.util.PatternUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import io.vertx.ext.web.templ.TemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by well on 2017/3/20.
 */
public class AutoVerticle extends AbstractVerticle {

    private Logger log = LoggerFactory.getLogger(getClass());

    private AppContext appContext;

    public AutoVerticle(AppContext context) {
        appContext = context;
    }

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

        router.route().handler(CorsHandler.create("\\.(eot|ttf|woff|woff2)$").allowedMethod(HttpMethod.GET));

        StaticHandler staticHandler = StaticHandler.create();
        staticHandler.setAllowRootFileSystemAccess(true);
        staticHandler.setWebRoot(config().getString("assets.dir"));
        if("env".equals(config().getString(JsonConfigUtil.envKey))||"test".equals(config().getString(JsonConfigUtil.envKey))){
            staticHandler.setCachingEnabled(false);
        }

        router.route("/assets/*").handler(staticHandler);

        router.route().handler(new DefaultCookieHandlerImpl());

        SessionStore store = LocalSessionStore.create(vertx);
        SessionHandler sessionHandler = SessionHandler.create(store);
        router.route().handler(sessionHandler);

        JsonObject cookieConfig = config().getJsonObject(JsonConfigUtil.cookieKey);
        router.route().handler(new CookieStoreSessionImpl(cookieConfig.getString("domain"), cookieConfig.getString("path"),
                cookieConfig.getBoolean("secure"), cookieConfig.getBoolean("httpOnly"), cookieConfig.getLong("maxAge"),
                cookieConfig.getString("sessionKey"), cookieConfig.getString("checkKey"), cookieConfig.getString("encryptSalt"),getCookieExcludePath(cookieConfig)));

        //page auth
        JsonObject authConfig = config().getJsonObject(JsonConfigUtil.authKey);
        if (authConfig!=null&&authConfig.getBoolean(JsonConfigUtil.usePageAuthKey) && authConfig.getString(JsonConfigUtil.pageAuthFailUrlKey) != null) {
            router.route().handler(new PageAuthCheckHandler(authConfig.getString(JsonConfigUtil.pageAuthFailUrlKey),getAuthExcludePath(authConfig)));
        }
        //page auth end

        router.route().handler(BodyHandler.create());
        TemplateEngine engine = new ExtFreeMarkerEngineImpl(config());
        List<RouteAction> routeList = appContext.getAllRouteActionList();
        routeList.forEach(routeAction -> {
            routeAction.getActionInvokeHandler().setTemplateEngine(engine);
            String requestUri = routeAction.getOriginalPath();
            if (PatternUtil.isPattern(requestUri)) {
                if (routeAction.getHttpMethod() == null) {
                    router.route().pathRegex(requestUri).handler(routeAction.getActionInvokeHandler());
                    if (log.isInfoEnabled()) {
                        log.info("register route:uri[" + requestUri + "]to action[" + routeAction.getActionInvokeHandler().getTargetAction().getClass() + "."
                                + routeAction.getActionInvokeHandler().getTargetMethod().getName() + "]");
                    }
                } else {
                    router.routeWithRegex(routeAction.getHttpMethod(), requestUri).handler(routeAction.getActionInvokeHandler());
                    if (log.isInfoEnabled()) {
                        log.info("register route:uri[" + requestUri + "]method[" + routeAction.getHttpMethod() + "]to action[" + routeAction.getActionInvokeHandler().getTargetAction().getClass() + "."
                                + routeAction.getActionInvokeHandler().getTargetMethod().getName() + "]");
                    }
                }
            } else {
                if (routeAction.getHttpMethod() == null) {
                    router.route(requestUri).handler(routeAction.getActionInvokeHandler());
                    if (log.isInfoEnabled()) {
                        log.info("register route:uri[" + requestUri + "]to action[" + routeAction.getActionInvokeHandler().getTargetAction().getClass() + "."
                                + routeAction.getActionInvokeHandler().getTargetMethod().getName() + "]");
                    }
                } else {
                    router.route(routeAction.getHttpMethod(), requestUri).handler(routeAction.getActionInvokeHandler());
                    if (log.isInfoEnabled()) {
                        log.info("register route:uri[" + requestUri + "]method[" + routeAction.getHttpMethod() + "]to action[" + routeAction.getActionInvokeHandler().getTargetAction().getClass() + "."
                                + routeAction.getActionInvokeHandler().getTargetMethod().getName() + "]");
                    }
                }
            }

        });

        router.route("/*").handler(new DefaultNotFoundHandler(engine, "404.ftl"));
        router.route().failureHandler(new DefaultFailureHandler(engine, "error.ftl"));

        HttpServer server = vertx.createHttpServer();
        server.requestHandler(router::accept).listen(config().getInteger("http.port"));
    }

    private List<String> getCookieExcludePath(JsonObject cookieConfig){
        if(cookieConfig==null){
            return Collections.emptyList();
        }
        JsonArray jsonArray = cookieConfig.getJsonArray(JsonConfigUtil.cookieExcludeKey);
        if(jsonArray==null){
            return Collections.emptyList();
        }
        return jsonArray.getList();

    }

    private List<String> getAuthExcludePath(JsonObject authConfig){
        if(authConfig==null){
            return Collections.emptyList();
        }
        JsonArray jsonArray = authConfig.getJsonArray(JsonConfigUtil.authExcludeKey);
        if(jsonArray==null){
            return Collections.emptyList();
        }
        return jsonArray.getList();

    }


}
