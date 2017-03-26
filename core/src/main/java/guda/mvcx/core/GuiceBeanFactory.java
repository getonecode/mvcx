package guda.mvcx.core;

import com.google.inject.*;
import guda.mvcx.core.annotation.action.Action;
import guda.mvcx.core.annotation.action.Req;
import guda.mvcx.core.annotation.biz.Biz;
import guda.mvcx.core.handle.ActionInvokeHandler;
import guda.mvcx.core.handle.RouteAction;
import guda.mvcx.core.handle.RouteRequest;
import guda.mvcx.core.util.PatternUtil;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import org.mybatis.guice.XMLMyBatisModule;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by well on 2017/3/20.
 */
public class GuiceBeanFactory {

    private Logger log = LoggerFactory.getLogger(getClass());

    private JsonObject config;
    private Injector injector;
    private List<RouteAction> routeActionList = new ArrayList<>();
    private List<Class> actionClassList = new ArrayList<>();
    private Map<RouteRequest, RouteAction> fullMatchActionMap = new HashMap<>();
    private List<RouteAction> patternRouteActionList = new ArrayList<>();


    public GuiceBeanFactory(JsonObject jsonObject) {
        config = jsonObject;
        setupGuice();
        routeActionList = resolveAction(actionClassList);
    }


    public void setupGuice() {
        JsonObject dbConfig = config.getJsonObject("db");
        List<Module> moduleList = new ArrayList<>();
        if (dbConfig != null) {
            Module mybatisModule = createMybatisModule(dbConfig);
            if (mybatisModule != null) {
                moduleList.add(mybatisModule);
            }
        }
        String bizPackage = config.getString("biz.package");
        Module bizModule = createBizModule(bizPackage);
        if (bizModule != null) {
            moduleList.add(bizModule);
        }

        String actionPackage = config.getString("action.package");
        Module actionModule = createActionModule(actionPackage);
        if (actionModule != null) {
            moduleList.add(actionModule);
        }

        injector = Guice.createInjector(Stage.PRODUCTION, moduleList);
    }

    private Module createMybatisModule(JsonObject dbConfig) {
        if (dbConfig == null) {
            return null;
        }
        return new XMLMyBatisModule() {
            @Override
            protected void initialize() {
                setEnvironmentId(dbConfig.getString("environment.id"));
            }

        };


    }

    private Module createBizModule(String bizPackage) {
        if (bizPackage == null) {
            return null;
        }
        return new Module() {
            @Override
            public void configure(Binder binder) {
                String[] ps = bizPackage.split(",");
                for (String s : ps) {
                    try {
                        Reflections reflections = new Reflections(s);
                        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Biz.class, true);
                        classes.forEach(clazz -> {
                            if (log.isInfoEnabled()) {
                                log.info("bind class:" + clazz.getName());
                            }
                            binder.bind(clazz);
                        });
                    } catch (Throwable e) {
                        throw new UnsupportedOperationException("can't add biz classes");
                    }
                }
            }
        };
    }

    private Module createActionModule(String actionPackage) {
        if (actionPackage == null) {
            return null;
        }
        return new Module() {
            @Override
            public void configure(Binder binder) {
                String[] ps = actionPackage.split(",");
                for (String s : ps) {
                    try {
                        Reflections reflections = new Reflections(s);
                        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Action.class, true);
                        classes.forEach(clazz -> {
                            if (log.isInfoEnabled()) {
                                log.info("bind class:" + clazz.getName());
                            }
                            binder.bind(clazz);
                            actionClassList.add(clazz);
                        });
                    } catch (Throwable e) {
                        throw new UnsupportedOperationException("can't add action classes");
                    }
                }
            }
        };
    }


    private List<RouteAction> resolveAction(List<Class> clazzList) {
        if (clazzList == null) {
            return null;
        }
        List<RouteAction> routeActions = new ArrayList<>(clazzList.size());
        clazzList.forEach(clazz -> {
            Object instance = injector.getInstance(clazz);
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
                        } else {
                            RouteRequest routeRequest = new RouteRequest(path, methodAnno.method());
                            fullMatchActionMap.put(routeRequest, routeAction);
                        }
                    }

                    routeActions.add(routeAction);
                }
            }
        });
        return routeActions;
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

    public Injector getInjector() {
        return injector;
    }

    public List<RouteAction> getRouteActionList() {
        return routeActionList;
    }

    public Map<RouteRequest, RouteAction> getFullMatchActionMap() {
        return fullMatchActionMap;
    }

    public List<RouteAction> getPatternRouteActionList() {
        return patternRouteActionList;
    }


}