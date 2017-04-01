package guda.mvcx.core.util;

import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.impl.CSRFHandlerImpl;

/**
 * Created by well on 2017/4/1.
 */
public class CsrfTokenHelper {

    public static String fetchToken(RoutingContext context){
        if(context==null){
            return null;
        }
        Object o = context.get(CSRFHandlerImpl.DEFAULT_HEADER_NAME);
        if(o!=null){
            return String.valueOf(o);
        }
        return context.request().getFormAttribute(CSRFHandlerImpl.DEFAULT_HEADER_NAME);
    }
}
