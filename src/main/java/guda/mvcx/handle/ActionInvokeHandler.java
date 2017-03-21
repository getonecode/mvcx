package guda.mvcx.handle;

import guda.mvcx.annotation.action.ReqParam;
import guda.mvcx.annotation.action.View;
import guda.mvcx.enums.ViewTypeEnum;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.TemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

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
            if(viewType == ViewTypeEnum.template){
                if(invokeResult == null){
                    throw new RuntimeException(targetAction.getClass().getName()+" action must return template name");
                }
                templateEngine.render(routingContext, normalTemplatePath(invokeResult.toString()), res -> {
                    if (res.succeeded()) {
                        routingContext.response().end(res.result());
                    } else {
                        routingContext.fail(res.cause());
                    }
                });
                return;
            }else if(viewType == ViewTypeEnum.json){
                if(invokeResult == null){
                    throw new RuntimeException(targetAction.getClass().getName()+" action must return object");
                }
                HttpServerResponse response = routingContext.response();
                response.putHeader("content-type", "application/json");
                response.end(Json.encode(invokeResult));
                return;
            }
        } catch (IllegalAccessException e) {
            log.error("action exec error",e);
            routingContext.fail(e.getCause());
        } catch (Exception e) {
            log.error("action exec error",e);
            routingContext.fail(e.getCause());
        }

    }

    private String normalTemplatePath(String path){
        if(templateDir== null){
            return path;
        }
        if(path==null){
            return null;
        }
        if(templateDir.endsWith("/")){
            return templateDir+path;
        }else{
            return templateDir+"/"+path;
        }
    }

    private Object[] resolveParam(RoutingContext routingContext) {
        if (parameters == null) {
            return null;
        }
        Object[] valueArray = new Object[parameters.length];
        int i=0;
        Map requestData = getRequestData(routingContext.request());
        for (Parameter parameter : parameters) {
            if (parameter.getType() == RoutingContext.class) {
                valueArray[i++]=routingContext;
                continue;
            }
            if (parameter.getType() == HttpServerRequest.class) {
                valueArray[i++]=routingContext.request();
                continue;
            }
            if (parameter.getType() == HttpServerResponse.class) {
                valueArray[i++]=routingContext.response();
                continue;
            }
            try {
                ReqParam declaredAnnotation = parameter.getDeclaredAnnotation(ReqParam.class);
                if(declaredAnnotation!=null){
                    valueArray[i++]=ReflectTool.resolveField(parameter, requestData,declaredAnnotation.value());
                }else{
                    valueArray[i++]=ReflectTool.resolveField(parameter, requestData,null);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }
        return valueArray;
    }

    private Map getRequestData(HttpServerRequest request) {
        Map<String, String> map = new HashMap<>();
        Iterator<Map.Entry<String, String>> iterator = request.params().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
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