package guda.mvcx.session;

import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.impl.CookieImpl;

import java.util.Set;

import static io.vertx.core.http.HttpHeaders.COOKIE;

/**
 * Created by well on 2017/3/22.
 */
public class DefaultCookieHandlerImpl implements Handler<RoutingContext> {




    @Override
    public void handle(RoutingContext context) {

        String cookieHeader = context.request().headers().get(COOKIE);

        if (cookieHeader != null) {
            Set<Cookie> nettyCookies = ServerCookieDecoder.STRICT.decode(cookieHeader);
            for (io.netty.handler.codec.http.cookie.Cookie cookie : nettyCookies) {
                io.vertx.ext.web.Cookie ourCookie = new CookieImpl(cookie);
                context.addCookie(ourCookie);
            }
        }


        context.next();

    }
}
