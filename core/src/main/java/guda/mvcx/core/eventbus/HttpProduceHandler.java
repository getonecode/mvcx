package guda.mvcx.core.eventbus;

import guda.mvcx.core.handle.ActionInvokeHandler;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by well on 2017/3/25.
 */
public class HttpProduceHandler implements Handler<RoutingContext> {



    @Override
    public void handle(RoutingContext event) {


        HttpServerRequest request = event.request();
        HttpEventContext httpEventContext =new HttpEventContext();
        httpEventContext.setHttpServerRequest(request);
        httpEventContext.setRoutingContext(event);

        event.vertx().eventBus().send("http.act", httpEventContext, replay -> {
            if (replay.succeeded()) {
                HttpEventContext httpEventContext1 = (HttpEventContext)replay.result().body();
                if(ActionInvokeHandler.NEXT_PREFIX.equals(String.valueOf(httpEventContext1.getResponse()))){
                    event.next();
                }else{

                }
            } else {
                event.next();
            }
        });

    }
}
