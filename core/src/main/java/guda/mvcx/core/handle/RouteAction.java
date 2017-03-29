package guda.mvcx.core.handle;

import io.vertx.core.http.HttpMethod;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by well on 2017/3/21.
 */
public class RouteAction {

    private String requestUri;

    private String originalPath;

    private HttpMethod httpMethod;

    private Pattern pattern;

    private List<String> pathParamNameList;

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

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

    public List<String> getPathParamNameList() {
        return pathParamNameList;
    }

    public void setPathParamNameList(List<String> pathParamNameList) {
        this.pathParamNameList = pathParamNameList;
    }
}
