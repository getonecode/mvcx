package guda.mvcx.core.handle;

import io.vertx.core.http.HttpMethod;

import java.util.regex.Pattern;

/**
 * Created by well on 2017/3/21.
 */
public class RouteAction {

    private String requestUri;

    private HttpMethod httpMethod;

    private Pattern pattern;

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    private ActionInvokeHandler actionInvokeHandler;

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public ActionInvokeHandler getActionInvokeHandler() {
        return actionInvokeHandler;
    }

    public void setActionInvokeHandler(ActionInvokeHandler actionInvokeHandler) {
        this.actionInvokeHandler = actionInvokeHandler;
    }
}
