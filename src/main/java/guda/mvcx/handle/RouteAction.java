package guda.mvcx.handle;

import io.vertx.core.http.HttpMethod;

/**
 * Created by well on 2017/3/21.
 */
public class RouteAction {

    private String requestUri;

    private HttpMethod httpMethod;

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
