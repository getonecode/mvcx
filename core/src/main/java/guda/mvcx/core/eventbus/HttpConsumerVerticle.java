package guda.mvcx.core.eventbus;

import guda.mvcx.core.handle.ActionInvokeHandler;
import guda.mvcx.core.handle.RouteAction;
import guda.mvcx.core.handle.RouteRequest;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import static java.util.Collections.sort;

/**
 * Created by well on 2017/3/25.
 */
public class HttpConsumerVerticle extends AbstractVerticle {

    private Logger log = LoggerFactory.getLogger(HttpConsumerVerticle.class);

    private Map<RouteRequest, RouteAction> fullMatchActionMap;
    private List<RouteAction> patternRouteActionList;


    public HttpConsumerVerticle(Map<RouteRequest, RouteAction> matchActionMap, List<RouteAction> patternActionList) {
        fullMatchActionMap = matchActionMap;
        patternRouteActionList = patternActionList;
    }

    @Override
    public void start() throws Exception {
        MessageConsumer<Object> consumer = vertx.eventBus().consumer("http.act");

        consumer.handler(message -> {

            HttpEventContext httpEventContext = (HttpEventContext) message.body();
            //找到对应的路由
            HttpServerRequest httpServerRequest = httpEventContext.getRoutingContext().request();
            String path = httpServerRequest.path();
            HttpMethod method = httpServerRequest.method();

            RouteRequest routeRequest = new RouteRequest(path, method);
            ActionInvokeHandler action = findAction(routeRequest);
            if (action == null) {
                httpEventContext.setResponse(ActionInvokeHandler.NEXT_PREFIX);
                message.reply(httpEventContext);
                return;
            }

            action.handle(httpEventContext.getRoutingContext());

            httpEventContext.setResponse("success");
            message.reply(httpEventContext);
        });
    }

    private ActionInvokeHandler findAction(RouteRequest routeRequest) {
        RouteAction routeAction = fullMatchActionMap.get(routeRequest);
        if (routeAction != null) {
            return routeAction.getActionInvokeHandler();
        }
        RouteAction mostMatch = findMostMatch(routeRequest);
        if (mostMatch == null) {
            return null;
        }
        fullMatchActionMap.put(routeRequest, mostMatch);
        return mostMatch.getActionInvokeHandler();
    }


    private RouteAction findMostMatch(RouteRequest routeRequest) {
        List<PathMatchResult> results = new ArrayList<PathMatchResult>();

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
