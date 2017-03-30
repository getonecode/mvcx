package guda.mvcx.core.auth.security;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by well on 15/10/15.
 */
public class AuthUser implements java.io.Serializable {

    @JsonIgnore
    public static final String sessionKey="_session_user";

    @JsonIgnore
    private static final ThreadLocal<AuthUser> authHolder = new ThreadLocal<AuthUser>();

    private Long userId;
    private String[] roles;
    private String loginName;
    private String phone;
    private String userName;
    private String userImg;


    @JsonIgnore
    public static final AuthUser getCurrentUser() {
        return authHolder.get();
    }

    @JsonIgnore
    public static final void setCurrentUser(AuthUser buyer) {
        authHolder.set(buyer);
    }

    @JsonIgnore
    public static final void cleanThreadLocal() {
        authHolder.remove();
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
