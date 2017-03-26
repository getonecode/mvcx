package guda.mvcx.core.eventbus;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by well on 2017/3/25.
 */
public class HttpEventContext {

    private HttpServerRequest httpServerRequest;

    private RoutingContext routingContext;

    private Object response;

    public RoutingContext getRoutingContext() {
        return routingContext;
    }

    public void setRoutingContext(RoutingContext routingContext) {
        this.routingContext = routingContext;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public HttpServerRequest getHttpServerRequest() {
        return httpServerRequest;
    }

    public void setHttpServerRequest(HttpServerRequest httpServerRequest) {
        this.httpServerRequest = httpServerRequest;
    }
}
