package guda.mvcx.core.session;


import guda.mvcx.core.util.BlowFishUtil;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.impl.CookieImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static io.vertx.core.http.HttpHeaders.SET_COOKIE;

/**
 * Created by well on 2017/3/22.
 */
public class CookieStoreSessionImpl implements Handler<RoutingContext> {


    private  Logger log= LoggerFactory.getLogger(getClass());

    private String domain;
    private String path;
    private long maxAge = Long.MIN_VALUE;
    private boolean secure;
    private boolean httpOnly;

    private String sessionKey;
    private String checkKey;
    private String encryptSalt;

    private BlowFishUtil blowFishUtil;

    public CookieStoreSessionImpl(String domain,String path,boolean secure,boolean httpOnly,long maxAge,String sessionKey,String checkKey,String salt){
        this.domain=domain;
        this.path=path;
        this.secure=secure;
        this.httpOnly=httpOnly;
        this.maxAge=maxAge;
        this.sessionKey=sessionKey;
        this.checkKey=checkKey;
        this.encryptSalt=salt;
        blowFishUtil=new BlowFishUtil(encryptSalt);

    }



    @Override
    public void handle(RoutingContext context) {
        context.addHeadersEndHandler(v -> {
            Map<String, Object> data = context.session().data();
            if(data!=null){
                JsonObject jsonObject=new JsonObject(data);
                String jsonString=jsonObject.toString();
                String checkValue=CookieCheck.mdCheck(jsonString);
                Cookie cookie=new CookieImpl(checkKey,checkValue);
                context.addCookie(cookie);

                Cookie sessionCookieVal=new CookieImpl(sessionKey,blowFishUtil.encrypt(jsonString));
                context.addCookie(sessionCookieVal);
            }

            for (io.vertx.ext.web.Cookie cookie: context.cookies()) {
                appendSecureProps(cookie);
                context.response().headers().add(SET_COOKIE, cookie.encode());
            }


        });

        Cookie sessionCookie = context.getCookie(sessionKey);
        Cookie checkCookie = context.getCookie(checkKey);
        if(checkCookie!=null&& sessionCookie!=null){
            String value = sessionCookie.getValue();
            value=  blowFishUtil.decrypt(value);
            if(!CookieCheck.mdCheck(value).equals(checkCookie.getValue())){
                log.warn("cookie check invalid,"+ (value));
                context.next();
                return ;
            }
            JsonObject jsonObject=new JsonObject(value);
            context.session().data().putAll(jsonObject.getMap());
        }


        context.next();
    }

    private void appendSecureProps(Cookie cookie){
        if(cookie==null){
            return;
        }
        if(domain!=null){
            cookie.setDomain(domain);
        }
        if(path!=null){
            cookie.setPath(path);
        }
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(secure);

    }
}
