package guda.mvcx.core.eventbus.context;

import guda.mvcx.core.factory.AppBeanFactory;
import guda.mvcx.core.handle.RouteAction;
import guda.mvcx.core.handle.RouteRequest;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * Created by well on 2017/3/27.
 */
public interface AppContext {

    public AppBeanFactory getAppBeanFactory() ;

    public Map<RouteRequest, RouteAction> getFullMatchActionMap();

    public List<RouteAction> getPatternRouteActionList() ;

    public List<RouteAction> getAllRouteActionList() ;

    public static AppContext create(JsonObject config){
        return new AppContextImpl(config);
    }


}
