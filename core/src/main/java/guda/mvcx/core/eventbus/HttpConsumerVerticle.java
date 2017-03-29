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
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.impl.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            RouteAction routeAction = findAction(appContext,routeRequest);
            if (routeAction == null) {
                httpEventMsg.getRoutingContext().next();
                log.warn("无法找到对应的路由:"+httpServerRequest.path());
                return;
            }
            //处理路径参数
            if(routeAction.getPathParamNameList()!=null&&routeAction.getPathParamNameList().size()==0){
                processPathParam(routeAction,httpEventMsg.getRoutingContext());
            }
            routeAction.getActionInvokeHandler().handle(httpEventMsg.getRoutingContext());
        });
    }

    private void processPathParam(RouteAction routeAction,RoutingContext context){

        Matcher m = routeAction.getPattern().matcher(context.request().path());
        if (m.matches()) {
            if (m.groupCount() > 0) {
                Map<String, String> params = new HashMap<>(m.groupCount());
                if (routeAction.getPathParamNameList() != null) {
                    for (int i = 0; i < routeAction.getPathParamNameList().size(); i++) {
                        final String k = routeAction.getPathParamNameList().get(i);
                        final String value = Utils.urlDecode(m.group("p" + i), false);
                        if (!context.request().params().contains(k)) {
                            params.put(k, value);
                        } else {
                            context.pathParams().put(k, value);
                        }
                    }
                } else {
                    // Straight regex - un-named params
                    // decode the path as it could contain escaped chars.
                    for (int i = 0; i < m.groupCount(); i++) {
                        String group = m.group(i + 1);
                        if(group != null) {
                            final String k = "param" + i;
                            final String value = Utils.urlDecode(group, false);
                            if (!context.request().params().contains(k)) {
                                params.put(k, value);
                            } else {
                                context.pathParams().put(k, value);
                            }
                        }
                    }
                }
                context.request().params().addAll(params);
                context.pathParams().putAll(params);
            }
        }

    }

    private RouteAction findAction(AppContext appContext,RouteRequest routeRequest) {
        RouteAction routeAction = appContext.getFullMatchActionMap().get(routeRequest);
        if (routeAction != null) {
            return routeAction;
        }
        RouteAction mostMatch = findMostMatch(appContext,routeRequest);
        if (mostMatch == null) {
            return null;
        }
        //路径带参数的不应该放入缓存
        if(mostMatch.getPathParamNameList()!=null&&mostMatch.getPathParamNameList().size()==0){
            appContext.getFullMatchActionMap().put(routeRequest, mostMatch);
        }
        return mostMatch;
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
