package guda.mvcx.core.handle;


import guda.mvcx.core.annotation.action.ReqParam;
import guda.mvcx.core.annotation.action.View;
import guda.mvcx.core.enums.ViewTypeEnum;
import guda.mvcx.core.helper.Form;
import guda.mvcx.core.helper.PageQuery;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.TemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by well on 2017/3/21.
 */
public class ActionInvokeHandler implements Handler<RoutingContext> {

    Logger log = LoggerFactory.getLogger(getClass());
    protected Method targetMethod;
    protected Object targetAction;
    private Parameter[] parameters;
    private TemplateEngine templateEngine;
    private String templateDir;
    private ViewTypeEnum viewType = ViewTypeEnum.template;

    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String NEXT_PREFIX = "next:";


    public ActionInvokeHandler(Object action, Method method) {
        if (action == null || method == null) {
            throw new RuntimeException("init action error targe or metho cannot null");
        }
        targetAction = action;
        targetMethod = method;
        parameters = method.getParameters();
        View declaredAnnotation = method.getDeclaredAnnotation(View.class);
        if (declaredAnnotation != null) {
            viewType = declaredAnnotation.type();
        }
    }


    @Override
    public void handle(RoutingContext routingContext) {
        if (routingContext.response().ended()) {
            return;
        }
        if (routingContext.failed()) {
            return;
        }
        //resolve param;
        Object[] resolveParam = resolveParam(routingContext);
        try {
            Object invokeResult = targetMethod.invoke(targetAction, resolveParam);
            if (invokeResult == null) {
                throw new RuntimeException(targetAction.getClass().getName() + " action must return object");
            }
            if (invokeResult.getClass() == String.class) {
                String result = String.valueOf(invokeResult);
                if (result.startsWith(REDIRECT_PREFIX)) {
                    routingContext.response().putHeader("location", result.substring((REDIRECT_PREFIX.length()))).setStatusCode(302).end();
                    return;
                } else if (result.startsWith(NEXT_PREFIX)) {
                    routingContext.next();
                    return;
                }
            }
            if (viewType == ViewTypeEnum.template) {
                templateEngine.render(routingContext, normalTemplatePath(invokeResult.toString()), res -> {
                    if (res.succeeded()) {
                        routingContext.response().end(res.result());
                    } else {
                        routingContext.fail(res.cause());
                    }
                });
                return;
            } else if (viewType == ViewTypeEnum.json) {
                HttpServerResponse response = routingContext.response();
                response.putHeader("content-type", "application/json");
                response.end(Json.encode(invokeResult));
                return;
            }
        } catch (IllegalAccessException e) {
            log.error("action exec error", e);
            routingContext.fail(e.getCause());
        } catch (Exception e) {
            log.error("action exec error", e);
            routingContext.fail(e.getCause());
        }

    }

    private String normalTemplatePath(String path) {
        if (templateDir == null) {
            return path;
        }
        if (path == null) {
            return null;
        }
        if (templateDir.endsWith(File.pathSeparator)) {
            return templateDir + path;
        } else {
            return templateDir + File.pathSeparator + path;
        }
    }

    private Object[] resolveParam(RoutingContext routingContext) {
        if (parameters == null) {
            return null;
        }
        Object[] valueArray = new Object[parameters.length];
        int i = 0;
        Map requestData = getRequestData(routingContext.request());
        for (Parameter parameter : parameters) {
            if (parameter.getType() == RoutingContext.class) {
                valueArray[i++] = routingContext;
                continue;
            }
            if (parameter.getType() == HttpServerRequest.class) {
                valueArray[i++] = routingContext.request();
                continue;
            }
            if (parameter.getType() == HttpServerResponse.class) {
                valueArray[i++] = routingContext.response();
                continue;
            }
            try {
                ReqParam declaredAnnotation = parameter.getDeclaredAnnotation(ReqParam.class);
                if (declaredAnnotation != null) {
                    Class clazz = parameter.getType();
                    if (ReflectTool.isSimpleClass(clazz)) {
                        valueArray[i++] = ReflectTool.resolveSimpleVal(parameter.getType(),requestData.get(declaredAnnotation.value()));
                    } else {
                        Object obj = ReflectTool.resolveCustomField(requestData, parameter.getType());
                        putInnerData(routingContext, obj);
                        valueArray[i++] = obj;
                    }

                } else {
                    Class clazz = parameter.getType();
                    if (ReflectTool.isSimpleClass(clazz)) {
                        valueArray[i++] = ReflectTool.resolveSimpleVal(parameter.getType(), requestData.get(declaredAnnotation.value()));
                    } else {
                        Object obj = ReflectTool.resolveCustomField(requestData, parameter.getType());
                        putInnerData(routingContext, obj);
                        valueArray[i++] = obj;
                    }
                }

            } catch (Exception e) {
                log.error("register route error", e);
                throw new RuntimeException(e);
            }


        }
        return valueArray;
    }

    private void putInnerData(RoutingContext routingContext, Object obj) {
        if (obj == null || routingContext == null) {
            return;
        }
        if (Form.class.isAssignableFrom(obj.getClass())) {
            routingContext.put("_form", obj);
        } else if ( PageQuery.class.isAssignableFrom(obj.getClass())) {
            routingContext.put("_query", obj);
        }
    }

    private Map getRequestData(HttpServerRequest request) {
        Map<String, String> map = new HashMap<>();
        Iterator<Map.Entry<String, String>> iterator = request.params().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            map.put(next.getKey(), next.getValue());
        }
        Iterator<Map.Entry<String, String>> iterator1 = request.formAttributes().iterator();
        while (iterator1.hasNext()) {
            Map.Entry<String, String> next = iterator1.next();
            map.put(next.getKey(), next.getValue());
        }
        return map;
    }

    public String getTemplateDir() {
        return templateDir;
    }

    public void setTemplateDir(String templateDir) {
        this.templateDir = templateDir;
    }

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object getTargetAction() {
        return targetAction;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public ViewTypeEnum getViewType() {
        return viewType;
    }
}
