package guda.mvcx.auth;



import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by well on 15/10/15.
 */
public class AuthGrant {

    public final static String MATCH_EVERYTHING = "*";

    /**
     * 特例用户名：匿名用户
     */
    public final static String ANONYMOUS_USER = "anonymous";

    private String[] users;
    private String[] roles;

    private boolean allAllow;

    private AuthPattern authPattern ;


    public boolean isAllAllow() {
        return allAllow;
    }

    public void setAllAllow(boolean allAllow) {
        this.allAllow = allAllow;
    }

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) {
        this.users = trim(users, MATCH_EVERYTHING, ANONYMOUS_USER);
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = trim(roles, MATCH_EVERYTHING);
    }


    private static boolean isEmptyArray(Object[] array) {
        if (array == null) {
            return true;
        }
        if (array.length == 0) {
            return true;
        }
        return false;
    }

    private String[] trim(String[] array, String... canonicals) {
        List<String> list = new LinkedList<String>();

        if (!isEmptyArray(array)) {
            for (String item : array) {
                item = StringUtils.trim(item);

                if (item != null) {
                    // 优化性能，避免字符串的比较，只需要用==比较即可。
                    if (canonicals != null) {
                        int i = arrayIndexOf(canonicals, item);

                        if (i >= 0) {
                            item = canonicals[i];
                        }
                    }

                    list.add(item);
                }
            }
        }

        if (!list.isEmpty()) {
            return list.toArray(new String[list.size()]);
        } else {
            return null;
        }
    }

    public boolean isUserMatched(String userName) {
        if (!isEmptyArray(users)) {
            for (String grantUser : users) {
                if (grantUser == ANONYMOUS_USER) {
                    if (userName == null) {
                        return true;
                    }
                } else if (grantUser == MATCH_EVERYTHING) {
                    if (userName != null) {
                        return true;
                    }
                } else {
                    if (grantUser.equals(userName)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean areRolesMatched(String[] roleNames) {
        if (!isEmptyArray(roles)) {
            for (String grantRole : roles) {
                if (grantRole == MATCH_EVERYTHING) {
                    boolean emptyRoleNames = true;

                    if (!isEmptyArray(roleNames)) {
                        for (String roleName : roleNames) {
                            if (roleName != null) {
                                emptyRoleNames = false;
                                break;
                            }
                        }
                    }

                    if (!emptyRoleNames) {
                        return true;
                    }
                } else {
                    if (arrayContains(roleNames, grantRole)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    public static boolean arrayContains(Object[] array, Object objectToFind) {
        return arrayIndexOf(array, objectToFind) != -1;
    }

    public static int arrayIndexOf(Object[] array, Object objectToFind) {
        if (array == null) {
            return -1;
        }
        int startIndex = 0;
        if (objectToFind == null) {
            for (int i = startIndex; i < array.length; i++) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = startIndex; i < array.length; i++) {
                if (objectToFind.equals(array[i])) {
                    return i;
                }
            }
        }

        return -1;
    }


    public AuthPattern getAuthPattern() {
        return authPattern;
    }

    public void setAuthPattern(AuthPattern authPattern) {
        this.authPattern = authPattern;
    }

    private void setUrls(Set<AuthPattern> urlSet, String[] urls) {
        urlSet.clear();
        for (String url : urls) {
            if (url == null) {
                continue;
            }
            urlSet.add(new AuthPattern(url));
        }
    }

    private boolean matches(Set<AuthPattern> actionSet, String action) {
        for (AuthPattern pattern : actionSet) {
            if (pattern.matcher(action).find()) {
                return true;
            }
        }

        return false;
    }
}
