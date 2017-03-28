package guda.mvcx.core.eventbus;

import guda.mvcx.core.eventbus.helper.EventAddressConstants;
import guda.mvcx.core.eventbus.msg.HttpEventMsg;
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
        HttpEventMsg httpEventMsg =new HttpEventMsg();
        httpEventMsg.setHttpServerRequest(request);
        httpEventMsg.setRoutingContext(event);
        event.vertx().eventBus().send(EventAddressConstants.ACTION_ADDRESS, httpEventMsg);

    }
}
