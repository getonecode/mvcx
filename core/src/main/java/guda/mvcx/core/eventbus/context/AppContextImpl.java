package guda.mvcx.core.eventbus.context;

import guda.mvcx.core.annotation.action.Req;
import guda.mvcx.core.factory.AppBeanFactory;
import guda.mvcx.core.factory.GuiceBeanFactory;
import guda.mvcx.core.handle.ActionInvokeHandler;
import guda.mvcx.core.handle.RouteAction;
import guda.mvcx.core.handle.RouteRequest;
import guda.mvcx.core.util.PatternUtil;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Created by well on 2017/3/27.
 */
public class AppContextImpl implements AppContext {

    private Logger log= LoggerFactory.getLogger(AppContextImpl.class);

    private Map<RouteRequest, RouteAction> fullMatchActionMap = new ConcurrentHashMap<>();
    private List<RouteAction> patternRouteActionList = new ArrayList<>();

    private AppBeanFactory appBeanFactory;

    private JsonObject contextConfig;

    private List<RouteAction> allRouteActionList = new ArrayList<>();

    public AppContextImpl(JsonObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        contextConfig = jsonObject;
        appBeanFactory = new GuiceBeanFactory(jsonObject);
        resolveAction(appBeanFactory.getActionClassList());
    }

    public JsonObject getContextConfig() {
        return contextConfig;
    }

    public void setContextConfig(JsonObject contextConfig) {
        this.contextConfig = contextConfig;
    }

    public AppBeanFactory getAppBeanFactory() {
        return appBeanFactory;
    }

    public void setAppBeanFactory(AppBeanFactory appBeanFactory) {
        this.appBeanFactory = appBeanFactory;
    }

    public Map<RouteRequest, RouteAction> getFullMatchActionMap() {
        return fullMatchActionMap;
    }

    public void setFullMatchActionMap(Map<RouteRequest, RouteAction> fullMatchActionMap) {
        this.fullMatchActionMap = fullMatchActionMap;
    }

    public List<RouteAction> getPatternRouteActionList() {
        return patternRouteActionList;
    }

    @Override
    public List<RouteAction> getAllRouteActionList() {
        return allRouteActionList;
    }

    @Override
    public JsonObject getConfig() {
        return contextConfig;
    }

    public void setPatternRouteActionList(List<RouteAction> patternRouteActionList) {
        this.patternRouteActionList = patternRouteActionList;
    }

    private void resolveAction(List<Class> clazzList) {
        if (clazzList == null) {
            return;
        }
        clazzList.forEach(clazz -> {
            Object instance = appBeanFactory.getBean(clazz);
            Req actionAnnotation = instance.getClass().getAnnotation(Req.class);
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                Req methodAnno = method.getAnnotation(Req.class);
                if (methodAnno != null) {
                    String path = normalPath(actionAnnotation, methodAnno);
                    ActionInvokeHandler actionInvokeHandler = new ActionInvokeHandler(instance, method);
                    RouteAction routeAction = new RouteAction();
                    routeAction.setRequestUri(path);
                    routeAction.setActionInvokeHandler(actionInvokeHandler);
                    if (methodAnno.method() != null) {
                        routeAction.setHttpMethod(methodAnno.method());
                    }

                    if (PatternUtil.isPattern(path)) {
                        routeAction.setPattern(Pattern.compile(path));
                        patternRouteActionList.add(routeAction);
                        if(log.isInfoEnabled()){
                            log.info("register route:uri[" + path + "]to action[" + routeAction.getActionInvokeHandler().getTargetAction().getClass() + "."
                                    + routeAction.getActionInvokeHandler().getTargetMethod().getName() + "]");
                        }
                    } else {
                        if (methodAnno == null) {
                            RouteRequest routeRequest = new RouteRequest(path, HttpMethod.GET);
                            fullMatchActionMap.put(routeRequest, routeAction);

                            routeRequest = new RouteRequest(path, HttpMethod.POST);
                            fullMatchActionMap.put(routeRequest, routeAction);

                            routeRequest = new RouteRequest(path, HttpMethod.HEAD);
                            fullMatchActionMap.put(routeRequest, routeAction);

                            routeRequest = new RouteRequest(path, HttpMethod.DELETE);
                            fullMatchActionMap.put(routeRequest, routeAction);

                            routeRequest = new RouteRequest(path, HttpMethod.PUT);
                            fullMatchActionMap.put(routeRequest, routeAction);

                            routeRequest = new RouteRequest(path, HttpMethod.CONNECT);
                            fullMatchActionMap.put(routeRequest, routeAction);

                            routeRequest = new RouteRequest(path, HttpMethod.OTHER);
                            fullMatchActionMap.put(routeRequest, routeAction);

                            routeRequest = new RouteRequest(path, HttpMethod.TRACE);
                            fullMatchActionMap.put(routeRequest, routeAction);


                            if(log.isInfoEnabled()){
                                log.info("register route:uri[" + path + "]to action[" + routeAction.getActionInvokeHandler().getTargetAction().getClass() + "."
                                        + routeAction.getActionInvokeHandler().getTargetMethod().getName() + "]");
                            }

                        } else {
                            RouteRequest routeRequest = new RouteRequest(path, methodAnno.method());
                            fullMatchActionMap.put(routeRequest, routeAction);

                            if(log.isInfoEnabled()){
                                log.info("register route:uri[" + path +"]method:["+methodAnno.method()+ "]to action[" + routeAction.getActionInvokeHandler().getTargetAction().getClass() + "."
                                        + routeAction.getActionInvokeHandler().getTargetMethod().getName() + "]");
                            }
                        }
                    }

                    allRouteActionList.add(routeAction);
                }
            }
        });

    }

    private String normalPath(Req actionAnno, Req methodAnno) {
        String actionPath = "";
        String methodPath = "";
        if (actionAnno != null) {
            actionPath = actionAnno.value();
            if (actionPath.endsWith("/")) {
                actionPath = actionPath.substring(0, actionPath.length() - 1);
            }

            if (!actionPath.startsWith("/")) {
                actionPath = "/" + actionPath;
            }
        }
        if (methodAnno != null) {
            methodPath = methodAnno.value();
            if (actionPath.length() == 0) {
                if (methodPath.startsWith("/")) {
                    return methodPath;
                } else {
                    return "/" + methodPath;
                }
            } else {
                if (methodPath.startsWith("/")) {
                    return actionPath + methodPath;
                } else {
                    return actionPath + "/" + methodPath;
                }
            }

        }
        return actionPath;
    }
}
