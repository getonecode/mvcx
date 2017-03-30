package guda.mvcx.core.auth.security;

/**
 * Created by well on 16/7/6.
 */
public class SecurityHelper {


    public static AuthUser getLoginUser(){
        return AuthUser.getCurrentUser();
    }

    public static boolean login(){
        if(AuthUser.getCurrentUser()==null){
            return false;
        }
        return AuthUser.getCurrentUser().getUserId()!=null;
    }
}
