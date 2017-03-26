package guda.mvcx.core.handle;

import io.vertx.core.http.HttpMethod;

/**
 * Created by well on 2017/3/26.
 */
public class RouteRequest {

    private String requestUri;

    private HttpMethod httpMethod;

    public RouteRequest(){

    }

    public RouteRequest(String uri,HttpMethod method){
        requestUri=uri;
        httpMethod=method;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RouteRequest)) return false;

        RouteRequest that = (RouteRequest) o;

        if (httpMethod != that.httpMethod) return false;
        if (!requestUri.equals(that.requestUri)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = requestUri.hashCode();
        result = 31 * result + httpMethod.hashCode();
        return result;
    }
}
