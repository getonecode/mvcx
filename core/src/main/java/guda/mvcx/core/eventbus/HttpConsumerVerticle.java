package guda.mvcx.core.eventbus;

import guda.mvcx.core.eventbus.context.AppContext;
import guda.mvcx.core.eventbus.helper.EventAddressConstants;
import guda.mvcx.core.eventbus.msg.HttpEventMsg;
import guda.mvcx.core.handle.ActionInvokeHandler;
import guda.mvcx.core.handle.RouteAction;
import guda.mvcx.core.handle.RouteRequest;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static java.util.Collections.sort;

/**
 * Created by well on 2017/3/25.
 */
public class HttpConsumerVerticle extends AbstractEventBusVerticle {

    private Logger log = LoggerFactory.getLogger(HttpConsumerVerticle.class);


    @Override
    public void start() throws Exception {
        MessageConsumer<Object> consumer = vertx.eventBus().consumer(EventAddressConstants.ACTION_ADDRESS);

        consumer.handler(message -> {

            HttpEventMsg httpEventMsg = (HttpEventMsg) message.body();
            //找到对应的路由
            HttpServerRequest httpServerRequest = httpEventMsg.getRoutingContext().request();
            String path = httpServerRequest.path();
            HttpMethod method = httpServerRequest.method();
            AppContext appContext=getAppContext(httpEventMsg.getRoutingContext());
            RouteRequest routeRequest = new RouteRequest(path, method);
            ActionInvokeHandler action = findAction(appContext,routeRequest);
            if (action == null) {
                httpEventMsg.getRoutingContext().next();
                return;
            }
            action.handle(httpEventMsg.getRoutingContext());
        });
    }

    private ActionInvokeHandler findAction(AppContext appContext,RouteRequest routeRequest) {
        RouteAction routeAction = appContext.getFullMatchActionMap().get(routeRequest);
        if (routeAction != null) {
            return routeAction.getActionInvokeHandler();
        }
        RouteAction mostMatch = findMostMatch(appContext,routeRequest);
        if (mostMatch == null) {
            return null;
        }
        appContext.getFullMatchActionMap().put(routeRequest, mostMatch);
        return mostMatch.getActionInvokeHandler();
    }


    private RouteAction findMostMatch(AppContext appContext,RouteRequest routeRequest) {
        List<PathMatchResult> results = new ArrayList<PathMatchResult>();
        List<RouteAction> patternRouteActionList = appContext.getPatternRouteActionList();
        // 匹配所有，注意，这里按倒序匹配，这样长度相同的匹配，以后面的为准。
        for (RouteAction routeAction : patternRouteActionList) {
            Matcher matcher = routeAction.getPattern().matcher(routeRequest.getRequestUri());
            if (matcher.find()) {
                if (routeAction.getHttpMethod() != null && routeAction.getHttpMethod() != routeRequest.getHttpMethod()) {
                    continue;
                }
                PathMatchResult result = new PathMatchResult();
                result.matchLength = matcher.end() - matcher.start();
                result.routeAction = routeAction;
                results.add(result);
            }
        }

        if (results.size() == 0) {
            return null;
        }
        sort(results);
        return results.get(0).getRouteAction();

    }


    private static class PathMatchResult implements Comparable<PathMatchResult> {
        private int matchLength = -1;
        private RouteAction routeAction;

        public int compareTo(PathMatchResult o) {
            return o.matchLength - matchLength;
        }

        public RouteAction getRouteAction() {
            return routeAction;
        }

        public void setRouteAction(RouteAction routeAction) {
            this.routeAction = routeAction;
        }
    }
}
