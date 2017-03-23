package guda.mvcx.core.auth;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.sort;

/**
 * Created by well on 15/10/15.
 */
public class PageAuthService {

    public static final String EMPTY_STRING = "";
    public static final String SPLIT_CHAR = ",";
    public static final String SPLIT_INROW_CHAR = ":";
    public static final String USER_PREFIX = "users";
    public static final String ROLE_PREFIX = "roles";
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    private List<AuthGrant> authGrantList = new ArrayList<AuthGrant>();

    public PageAuthService() {

        this("auth.properties");
    }

    public PageAuthService(String resourceName) {

        InputStream resourceAsStream = PageAuthService.class.getClassLoader().getResourceAsStream(resourceName);
        if(resourceAsStream==null){
            return;
        }
        try {
            Properties properties = new Properties();
            properties.load(resourceAsStream);
            if (properties.size() == 0) {
                return;
            }
            Iterator<Map.Entry<Object, Object>> iterator = properties.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, Object> next = iterator.next();
                Object key = next.getKey();
                Object value = next.getValue();
                if (key == null || value == null) {
                    continue;
                }
                String[] split = String.valueOf(key).split(SPLIT_CHAR);
                String[] split1 = String.valueOf(value).split(SPLIT_CHAR);
                for (String s : split) {
                    AuthGrant authGrant = new AuthGrant();
                    authGrant.setAuthPattern(new AuthPattern(s));
                    List<String> users = new ArrayList<String>();
                    List<String> roles = new ArrayList<String>();
                    for (String rol : split1) {
                        if (AuthGrant.ANONYMOUS_USER.equals(rol)) {
                            authGrant.setAllAllow(true);
                        }
                        String[] userArray = getUserArray(rol);
                        if (userArray != null) {
                            users.addAll(Arrays.asList(userArray));
                        }
                        String[] roleArray = getRoleArray(rol);
                        if (roleArray != null) {
                            roles.addAll(Arrays.asList(roleArray));
                        }
                    }
                    authGrant.setRoles(roles.toArray(new String[roles.size()]));
                    authGrant.setUsers(users.toArray(new String[users.size()]));
                    authGrantList.add(authGrant);

                }

            }

            Collections.sort(authGrantList, new Comparator<AuthGrant>() {
                @Override
                public int compare(AuthGrant o1, AuthGrant o2) {
                    return o2.getAuthPattern().getPatternName().length() - o1.getAuthPattern().getPatternName().length();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] getUserArray(String s) {
        if (s == null) {
            return null;
        }
        if (s.startsWith(USER_PREFIX)) {
            String substring = s.substring(USER_PREFIX.length() + 1, s.length() - 1);
            return substring.split(SPLIT_INROW_CHAR);
        }
        return null;
    }

    private String[] getRoleArray(String s) {
        if (s == null) {
            return null;
        }
        if (s.startsWith(ROLE_PREFIX)) {
            String substring = s.substring(ROLE_PREFIX.length() + 1, s.length() - 1);
            return substring.split(SPLIT_INROW_CHAR);
        }
        return null;
    }

    public void setAuthGrantList(List<AuthGrant> authGrantList) {
        this.authGrantList = authGrantList;
    }

    public boolean isAllow(String requestUri, String userName, String[] roleNames) {
        PageAuthResult result = authorize(requestUri, userName, roleNames);
        switch (result) {
            case ALLOWED:
                return true;
            case DENIED:
                return false;
            default:
                return true;
        }

    }

    public PageAuthResult authorize(String url, String userName, String[] roleNames) {
        userName = StringUtils.trim(userName);


        if (roleNames == null) {
            roleNames = EMPTY_STRING_ARRAY;
        }

        // 找出所有匹配的pattern，按匹配长度倒排序。
        MatchResult[] results = getMatchResults(url);

        if (results == null || results.length == 0) {
            return PageAuthResult.TARGET_NOT_MATCH;
        } else {

            Boolean actionAllowed = isUrlAllowed(results, userName, roleNames);
            if (actionAllowed == null) {
                return PageAuthResult.TARGET_NOT_MATCH;
            }

            if (actionAllowed.booleanValue()) {
                return PageAuthResult.ALLOWED;
            }
            return PageAuthResult.DENIED;

        }

    }

    private Boolean isUrlAllowed(MatchResult[] results, String userName, String[] roleNames) {
        // 按顺序检查授权，直到role或user被allow或deny
        for (MatchResult result : results) {
            AuthGrant grant = result.authGrant;
            boolean userMatch = grant.isUserMatched(userName);
            boolean roleMatch = grant.areRolesMatched(roleNames);
            if (userMatch || roleMatch) {
                return TRUE;
            }
            if (grant.isAllAllow()) {
                return TRUE;
            }
            return FALSE;

        }
        return null;
    }

    public String toString() {
        Iterator<AuthGrant> iterator = authGrantList.iterator();
        StringBuilder builder = new StringBuilder();
        while (iterator.hasNext()) {
            builder.append(ReflectionToStringBuilder.toString(iterator.next())).append("/n");
        }
        return builder.toString();
    }

    private MatchResult[] getMatchResults(String target) {
        List<MatchResult> results = new ArrayList<MatchResult>();

        // 匹配所有，注意，这里按倒序匹配，这样长度相同的匹配，以后面的为准。
        for (AuthGrant authGrant : authGrantList) {
            Matcher matcher = authGrant.getAuthPattern().matcher(target);
            if (matcher.find()) {
                MatchResult result = new MatchResult();
                result.matchLength = matcher.end() - matcher.start();
                result.authGrant = authGrant;
                result.target = target;

                results.add(result);
            }
        }


        // 按匹配长度倒排序，注意，这是稳定排序，对于长度相同的匹配，顺序不变。
        sort(results);

        // 除去重复的匹配
        Map<AuthGrant, MatchResult> grantsSet = new LinkedHashMap<AuthGrant, MatchResult>();

        for (MatchResult result : results) {
            AuthGrant grants = result.authGrant;

            if (!grantsSet.containsKey(grants)) {
                grantsSet.put(grants, result);
            }
        }

        return grantsSet.values().toArray(new MatchResult[grantsSet.size()]);
    }

    private static class MatchResult implements Comparable<MatchResult> {
        private int matchLength = -1;
        private AuthGrant authGrant;
        private String target;

        public int compareTo(MatchResult o) {
            return o.matchLength - matchLength;
        }

        @Override
        public String toString() {
            return "Match length=" + matchLength + ", target=" + target + ", " + authGrant;
        }
    }

    public static enum PageAuthResult {
        /**
         * 代表页面被许可访问。
         */
        ALLOWED,

        /**
         * 代表页面被拒绝访问。
         */
        DENIED,

        /**
         * 代表当前的target未匹配。
         */
        TARGET_NOT_MATCH,

        /**
         * 代表当前的grant未匹配，也就是user/roles/actions未匹配。
         */
        GRANT_NOT_MATCH
    }

    public static void main(String args[]) {
        PageAuthService pageAuthService = new PageAuthService("auth-demo2.properties");
        System.out.println(pageAuthService);
        System.out.println(pageAuthService.authorize("/assets/", "abc", new String[]{}));
        System.out.println(pageAuthService.authorize("/assets/2", "", new String[]{}));
        System.out.println(pageAuthService.authorize("/docs/", "abc3", new String[]{}));
        System.out.println(pageAuthService.authorize("/admin/index", "3", new String[]{"user"}));
        System.out.println(pageAuthService.authorize("/admin/index", "ad", new String[]{"admin", "user"}));
    }
}
