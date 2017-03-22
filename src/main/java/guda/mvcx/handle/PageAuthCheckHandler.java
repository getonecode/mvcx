package guda.mvcx.handle;

import guda.mvcx.auth.PageAuthService;
import guda.mvcx.constants.SessionConstants;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by well on 2017/3/22.
 */
public class PageAuthCheckHandler implements Handler<RoutingContext> {

    private String authFailUrl;

    private PageAuthService pageAuthService = new PageAuthService();

    public PageAuthCheckHandler(String failUrl){
        authFailUrl=failUrl;
    }


    @Override
    public void handle(RoutingContext context) {
        Object roleObj = context.session().get(SessionConstants.ROLE_KEY);
        String role = String.valueOf(roleObj);

        Object userObj = context.session().get(SessionConstants.USER_ID_KEY);
        String userId = String.valueOf(userObj);

        String path=context.request().path();
        boolean result=pageAuthService.isAllow(path, userId, new String[]{role});
        if(result){
            context.next();
            return;
        }
        context.response().putHeader("location", authFailUrl).setStatusCode(302).end();



    }
}
