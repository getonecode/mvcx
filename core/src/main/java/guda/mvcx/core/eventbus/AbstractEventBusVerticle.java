package guda.mvcx.core.eventbus;

import guda.mvcx.core.eventbus.context.AppContext;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by well on 2017/3/27.
 */
public abstract class AbstractEventBusVerticle extends AbstractVerticle {

    public static final String APP_CONTEXT_KEY="APP_CONTEXT_KEY";

    public AppContext getAppContext(RoutingContext context){
        if(context==null){
            return null;
        }
        return (AppContext)context.get(APP_CONTEXT_KEY);
    }

}
